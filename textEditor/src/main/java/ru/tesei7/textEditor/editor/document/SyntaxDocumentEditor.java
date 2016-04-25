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
		Line nextLine = currentLine.getNext();
		Line newLine = new Line();

		currentLine.linkWith(newLine);
		newLine.linkWith(nextLine);

		int offset = currentLine.getOffset();
		List<Character> curLineText = currentLine.getChars();
		newLine.setChars(curLineText.subList(offset, curLineText.size()));
		currentLine.setChars(curLineText.subList(0, offset));

		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		document.setCurrentLine(newLine);
		newLine.setOffset(0);
		caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_DOWN, false));
	}

	void delete() {
		if (document.getSelection().notSelected()) {
			deleteChar();
		} else {
			document.removeSelection();
		}
	}

	void deleteChar() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLength()) {
			concatLines(currentLine, currentLine.getNext(), false);
		} else {
			currentLine.delete();
			dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
		}
	}

	void backspace() {
		if (document.getSelection().notSelected()) {
			backspaceChar();
		} else {
			document.removeSelection();
		}
	}

	void backspaceChar() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			concatLines(currentLine.getPrevious(), currentLine, true);
		} else {
			currentLine.backspace();
			// dimensions should be changed first
			dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
			caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_LEFT, false));
		}
	}

	void concatLines(Line l1, Line l2, boolean moveCaretUp) {
		if (l1 == null || l2 == null) {
			return;
		}
		if (!l1.hasNext() || !l1.getNext().equals(l2)) {
			return;
		}

		Line next = l2.getNext();
		l1.linkWith(next);

		int l1_lenght = l1.getLength();
		LinkedList<Character> concat = new LinkedList<>();
		concat.addAll(l1.getChars());
		concat.addAll(l2.getChars());
		l1.setChars(concat);

		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		l1.setOffset(l1_lenght);
		if (moveCaretUp) {
			document.setCurrentLine(l1);
			caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOVED_UP, false));
		}

	}

}
