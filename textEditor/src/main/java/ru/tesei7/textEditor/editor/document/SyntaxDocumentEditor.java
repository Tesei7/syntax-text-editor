package ru.tesei7.textEditor.editor.document;

import java.util.LinkedList;
import java.util.List;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxDocumentEditor extends SyntaxCaretObservable implements DocumentEditListener {

	private SyntaxDocument document;

	public SyntaxDocumentEditor(SyntaxDocument document) {
		this.document = document;
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
		document.setCurrentLine(newLine);
		newLine.setOffset(0);
		
		notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.DOWN));
	}

	void delete() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLenght()) {
			concatLines(currentLine, currentLine.getNext());
		} else {
			currentLine.delete();
		}
	}

	void backspace() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			concatLines(currentLine.getPrevious(), currentLine);
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.UP));
		} else {
			currentLine.backspace();
		}
	}

	void concatLines(Line l1, Line l2) {
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
		document.setCurrentLine(l1);
		l1.setOffset(l1_lenght);
	}

}
