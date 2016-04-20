package ru.tesei7.textEditor.editor.document;

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

}
