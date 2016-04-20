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
			Line currentLine = document.getCurrentLine();
			lineEditor.setLine(currentLine);
			lineEditor.printChar(c);
		}
	}

	private void addNewLine() {
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
	}

	public void delete() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLenght()) {
			// TODO concat lines
			return;
		}
		
		lineEditor.setLine(currentLine);
		lineEditor.delete();
	}

	public void backspace() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			// TODO concat lines
			return;
		}
		lineEditor.setLine(currentLine);
		lineEditor.backspace();
	}
}
