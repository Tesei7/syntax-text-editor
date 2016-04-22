package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.List;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class SyntaxDocument {

	private Line firstLine;
	private Line firstVisibleLine;
	private Line currentLine;
	private SyntaxTextEditor editor;

	public SyntaxDocument(SyntaxTextEditor editor) {
		this.editor = editor;
		firstLine = new Line();
		firstVisibleLine = firstLine;
		currentLine = firstVisibleLine;
	}

	public Line getFirstLine() {
		return firstLine;
	}

	public Line getFirstVisibleLine() {
		return firstVisibleLine;
	}

	public void setFirstVisibleLine(Line firstVisibleLine) {
		this.firstVisibleLine = firstVisibleLine;
	}

	public Line getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Line currentLine) {
		this.currentLine = currentLine;
	}

	public List<Line> getVisibleLines() {
		List<Line> lines = new ArrayList<>();
		int rows = editor.getRows();
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
		for (int i = 0; i < editor.getRows(); i++) {
			if (tmp.equals(currentLine)) {
				return i;
			}
			if (!tmp.hasNext()) {
				break;
			}
			tmp = tmp.getNext();
		}
		return -1;
	}

}
