package ru.tesei7.textEditor.editor;

import java.util.LinkedList;
import java.util.List;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentEditor {

	private SyntaxDocument document;

	public SyntaxDocumentEditor(SyntaxDocument document) {
		this.document = document;
	}

	public void printChar(char c) {
		if (c == '\n') {
			addNewLine();
		} else {
			document.getCurrentLine().printChar(c);
		}
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
	}

	public void delete() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLenght()) {
			concatLines(currentLine, currentLine.getNext(), false);
		} else {
			currentLine.delete();
		}
	}

	public void backspace() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			concatLines(currentLine.getPrevious(), currentLine, true);
		} else {
			currentLine.backspace();
		}
	}

	void concatLines(Line l1, Line l2, boolean isBackspace) {
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
		if (isBackspace) {
			l1.setOffset(l1_lenght);
		}
	}
}
