package ru.tesei7.textEditor.editor.document;

import java.util.ArrayList;
import java.util.List;

public class SyntaxDocument {

	private Line firstLine;
	private Line firstVisibleLine;
	private Line currentLine;

	public SyntaxDocument() {
		firstLine = new Line();
		firstVisibleLine = firstLine;
		currentLine = firstVisibleLine;
	}
	
	public Line getFirstLine() {
		return firstLine;
	}

	public Line getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Line currentLine) {
		this.currentLine = currentLine;
	}

	public List<Line> getVisibleLines() {
		List<Line> lines = new ArrayList<>();
		int rows = 10;
		Line line = firstVisibleLine;
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

	public int getCurrentLineY() {
		Line tmp = firstVisibleLine;
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
