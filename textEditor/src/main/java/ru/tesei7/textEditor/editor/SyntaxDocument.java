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

	public Line getCurrentLine() {
		return currentLine;
	}

	public int getCurrentLineRow() {
		int row = 0;
		Line line = firstLine;
		while (line.hasNext()) {
			line = line.getNext();
			row++;
		}
		return row;
	}

	public void addChar(char c) {
		currentLine.addChar(c);
	}

	public void backspaceChar() {
		currentLine.backspaceChar();
	}

}
