package ru.tesei7.textEditor.editor.document;

import java.util.LinkedList;
import java.util.List;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.scroll.DimensionType;
import ru.tesei7.textEditor.editor.scroll.DimensionsEvent;
import ru.tesei7.textEditor.editor.scroll.DimensionsObservable;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;

public class SyntaxDocumentEditor implements DocumentEditListener {

	private SyntaxDocument document;
	SyntaxCaretObservable caretObservable;
	DimensionsObservable dimensionsObservable;
	FrameObserverable frameObserverable;

	public SyntaxDocumentEditor(SyntaxDocument document, SyntaxCaretObservable syntaxCaretObservable,
			DimensionsObservable dimensionsObservable) {
		this.document = document;
		this.caretObservable = syntaxCaretObservable;
		this.dimensionsObservable = dimensionsObservable;
	}

	@Override
	public void onDocumentEdited(DocumentEditEvent e) {
		switch (e.getType()) {
		case BACKSPACE:
			backspace();
			break;
		case DELETE:
			delete();
			break;
		case PRINT_CHAR:
			printChar(e.getChar());
			break;
		case NEW_LINE:
			addNewLine();
			break;
		default:
			break;
		}
	}

	void printChar(char c) {
		switch (document.getCaretType()) {
		case NORMAL:
			document.getCurrentLine().printChar(c);
			break;
		case INSERT:
			document.getCurrentLine().insertChar(c);
			break;
		default:
			break;
		}

		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
		caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_RIGHT, false));
	}

	void addNewLine() {
		Line currentLine = document.getCurrentLine();
		// Line nextLine = currentLine.getNext();
		Line newLine = new Line();

		document.addLineAfter(document.getCurLineIndex(), newLine);
		// currentLine.linkWith(newLine);
		// newLine.linkWith(nextLine);

		int offset = currentLine.getOffset();
		List<Character> curLineText = currentLine.getChars();
		newLine.setChars(curLineText.subList(offset, curLineText.size()));
		currentLine.setChars(curLineText.subList(0, offset));

		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		// document.setCurrentLine(newLine);
		document.setCurLineIndex(document.getCurLineIndex() + 1);
		newLine.setOffset(0);
		caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_DOWN, false));
	}

	void delete() {
		if (document.getSelection().notSelected()) {
			deleteChar();
		} else {
			removeSelection();
		}
	}

	void deleteChar() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLength()) {
			int curLineIndex = document.getCurLineIndex();
			concatLines(curLineIndex, curLineIndex + 1, false);
		} else {
			currentLine.delete();
			dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
		}
	}

	void backspace() {
		if (document.getSelection().notSelected()) {
			backspaceChar();
		} else {
			removeSelection();
		}
	}

	void backspaceChar() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			int curLineIndex = document.getCurLineIndex();
			concatLines(curLineIndex - 1, curLineIndex, true);
		} else {
			currentLine.backspace();
			// dimensions should be changed first
			dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
			caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_LEFT, false));
		}
	}
	
	void removeSelection(){
		document.removeSelection();
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_UP, false));
	}

	void concatLines(int l1Index, int l2Index, boolean moveCaretUp) {
		if (!document.isCorrectLineIndex(l1Index) || !document.isCorrectLineIndex(l2Index)) {
			return;
		}
		Line l1 = document.getLineByIndex(l1Index);
		Line l2 = document.getLineByIndex(l2Index);
		document.addLineAfter(l1Index, l2);

		int l1_lenght = l1.getLength();
		LinkedList<Character> concat = new LinkedList<>();
		concat.addAll(l1.getChars());
		concat.addAll(l2.getChars());
		l1.setChars(concat);

		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		l1.setOffset(l1_lenght);
		if (moveCaretUp) {
			document.moveCurLineIndex(-1);
			caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_UP, false));
		}

	}

}
