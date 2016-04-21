package ru.tesei7.textEditor.editor;

import java.util.List;

import javax.inject.Inject;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentEditor {

	private SyntaxDocument document;
	@Inject
	private LineEditor lineEditor;

	public void setDocument(SyntaxDocument document) {
		this.document = document;
	}

	public void printChar(char c) {
		if (c == '\n') {
			addNewLine();
		} else {
			lineEditor.printChar(c, document.getCurrentLine());
		}
	}

	void addNewLine() {
		Line currentLine = document.getCurrentLine();
		Line newLine = new Line();
		newLine.setPrevious(currentLine);
		newLine.setNext(currentLine.getNext());
		currentLine.setNext(newLine);

		int offset = currentLine.getOffset();
		List<Character> curLineText = currentLine.getText();
		newLine.setText(curLineText.subList(offset, curLineText.size()));
		currentLine.setText(curLineText.subList(0, offset));
		document.setCurrentLine(newLine);
		newLine.setOffset(0);
	}

	public void delete() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLenght()) {
			concatLines(currentLine, currentLine.getNext());
		} else {
			lineEditor.delete(currentLine);
		}
	}

	public void backspace() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			concatLines(currentLine.getPrevious(), currentLine);
		} else {
			lineEditor.backspace(currentLine);
		}
	}

	void concatLines(Line l1, Line l2) {
		if (l1 == null || l2 == null) {
			return;
		}
		if (!l1.hasNext() || !l1.getNext().equals(l2)){
			return;
		}

		Line next = l2.getNext();
		l1.setNext(next);
		if (next != null) {
			next.setPrevious(l1);
		}
		l1.getText().addAll(l2.getText());
		document.setCurrentLine(l1);
	}
}
