package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.List;

import ru.tesei7.textEditor.editor.scroll.bar.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.bar.FrameObserverable;

/**
 * Stores text data in linked list of {@link Line}s.
 * 
 * @author Ilya
 *
 */
public class SyntaxDocument {
	static final int DEFAULT_ROWS = 40;
	static final int DEFAULT_COLS = 180;

	private int rows = DEFAULT_ROWS;
	private int cols = DEFAULT_COLS;
	Line firstLine;
	Line firstVisibleLine;
	Line currentLine;
	private FrameObserverable frameObserverable;

	public SyntaxDocument(FrameObserverable frameObserverable) {
		this.frameObserverable = frameObserverable;
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
		frameObserverable.notifyListeners(new FrameEvent(firstVisibleLine));
	}

	public Line getCurrentLine() {
		return currentLine;
	}

	public void setCurrentLine(Line currentLine) {
		this.currentLine = currentLine;
	}

	public List<Line> getVisibleLines() {
		List<Line> lines = new ArrayList<>();
		Integer i = new Integer(rows);
		Line line = firstVisibleLine;
		do {
			lines.add(line);
			if (!line.hasNext()) {
				break;
			} else {
				line = line.getNext();
				i--;
			}
		} while (i > 0);
		return lines;
	}

	public int getCurrentLineY() {
		Line tmp = firstVisibleLine;
		for (int i = 0; i < rows; i++) {
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

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getSize() {
		int size = 1;
		Line l = firstLine;
		while (l.hasNext()) {
			l = l.getNext();
			size++;
		}
		return size;
	}

	public int getLineIndex(Line line) {
		int index = 0;
		Line l = firstLine;
		while (l.hasNext() && !l.equals(line)) {
			l = l.getNext();
			index++;
		}
		return index;
	}
}
