package ru.tesei7.textEditor.editor;

import java.util.List;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentEditor {

	private SyntaxDocument document;
	private LineEditor lineEditor;

	public SyntaxDocumentEditor(SyntaxDocument document) {
		this.document = document;
		this.lineEditor = new LineEditor();
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
		Line nextLine = currentLine.getNext();
		Line newLine = new Line();
		
		currentLine.linkWith(newLine);
		newLine.linkWith(nextLine);

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
			concatLines(currentLine, currentLine.getNext(), false);
		} else {
			lineEditor.delete(currentLine);
		}
	}

	public void backspace() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			concatLines(currentLine.getPrevious(), currentLine, true);
		} else {
			lineEditor.backspace(currentLine);
		}
	}

	void concatLines(Line l1, Line l2, boolean isBackspace) {
		if (l1 == null || l2 == null) {
			return;
		}
		if (!l1.hasNext() || !l1.getNext().equals(l2)){
			return;
		}

		Line next = l2.getNext();
		l1.linkWith(next);
		
		int l1_lenght = l1.getLenght();
		l1.getText().addAll(l2.getText());
		document.setCurrentLine(l1);
		if (isBackspace){
			l1.setOffset(l1_lenght);
		}
	}
}
