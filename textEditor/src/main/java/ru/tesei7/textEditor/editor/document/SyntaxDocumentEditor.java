package ru.tesei7.textEditor.editor.document;

import java.util.LinkedList;
import java.util.List;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.scroll.bar.DimensionsEvent;
import ru.tesei7.textEditor.editor.scroll.bar.DimensionsObservable;
import ru.tesei7.textEditor.editor.scroll.bar.FrameObserverable;

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
		document.getCurrentLine().printChar(c);
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

		caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.DOWN));

		newLine.setOffset(0);
		
		dimensionsObservable.notifyListeners(new DimensionsEvent(-1));
	}

	void delete() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLenght()) {
			concatLines(currentLine, currentLine.getNext(), false);
		} else {
			currentLine.delete();
		}
	}

	void backspace() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			concatLines(currentLine.getPrevious(), currentLine, true);
		} else {
			currentLine.backspace();
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

		int l1_lenght = l1.getLenght();
		LinkedList<Character> concat = new LinkedList<>();
		concat.addAll(l1.getChars());
		concat.addAll(l2.getChars());
		l1.setChars(concat);

		if (moveCaretUp) {
			caretObservable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.UP));
		}
		l1.setOffset(l1_lenght);
		
		dimensionsObservable.notifyListeners(new DimensionsEvent(-1));
	}

}
