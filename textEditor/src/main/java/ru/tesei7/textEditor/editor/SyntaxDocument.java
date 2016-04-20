package ru.tesei7.textEditor.editor;

import java.util.ArrayList;
import java.util.List;

public class SyntaxDocument {

	private Line firstLine;
	private Line currentLine;

	public SyntaxDocument() {
		firstLine = new Line();
		currentLine = firstLine;
	}

	public Line getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Line currentLine) {
		this.currentLine = currentLine;
	}

	public List<Line> getLines() {
		List<Line> lines = new ArrayList<>();
		int rows = 10;
		Line line = firstLine;
		do {
			lines.add(line);
			if (!line.hasNext()) {
				break;
			} else {
				line = line.getNext();
				rows--;
			}

		} while (rows > 0);
		return lines;
	}

	public int getCurrentLineRow() {
		Line tmp = firstLine;
		for (int i=0; i<10;i++) {
			if (tmp.equals(currentLine)){
				return i;
			}
			if (!tmp.hasNext()){
				break;
			}
			tmp = tmp.getNext();
		}
		return -1;
	}

	public void addChar(char c) {
		if (c == '\n') {
			addNewLine();
		} else {
			currentLine.addChar(c);
		}
	}

	private void addNewLine() {
		Line newLine = new Line();
		newLine.setPrevious(currentLine);
		newLine.setNext(currentLine.getNext());
		currentLine.setNext(newLine);

		int offset = currentLine.getOffset();
		List<Character> curLineText = currentLine.getText();
		newLine.setText(curLineText.subList(offset, curLineText.size()));
		currentLine.setText(curLineText.subList(0, offset));
		currentLine = newLine;
	}

	public void backspaceChar() {
		currentLine.backspaceChar();
	}

}
