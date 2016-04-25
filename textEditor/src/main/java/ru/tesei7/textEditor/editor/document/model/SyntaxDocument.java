package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;

/**
 * Stores text data in linked list of {@link Line}s.
 * 
 * @author Ilya
 *
 */
public class SyntaxDocument {
	static final int DEFAULT_ROWS = 40;
	static final int DEFAULT_COLS = 80;

	/**
	 * Number of visible rows
	 */
	private int rows = DEFAULT_ROWS;
	/**
	 * Number of visible columns
	 */
	private int cols = DEFAULT_COLS;
	/**
	 * Type of care
	 */
	private CaretType caretType = CaretType.NORMAL;
	/**
	 * First line. Should never be replaced by another line.
	 */
	Line firstLine;
	/**
	 * Line, cursor stays on
	 */
	Line currentLine;
	/**
	 * Vertical offset of visible frame
	 */
	int firstVisibleRow = 0;
	/**
	 * Horizontal offset of visible frame
	 */
	int firstVisibleCol = 0;
	/**
	 * Stores coordinates of selected text
	 */
	TextSelection selection;
	/**
	 * Broadcaster for frame movements
	 */
	private FrameObserverable frameObserverable;

	public SyntaxDocument(FrameObserverable frameObserverable) {
		this.frameObserverable = frameObserverable;
		firstLine = new Line();
		selection = new TextSelection(this);
		currentLine = firstLine;
	}

	// GETTERS & SETTERS

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

	public CaretType getCaretType() {
		return caretType;
	}

	public void setCaretType(CaretType caretType) {
		this.caretType = caretType;
	}

	public TextSelection getSelection() {
		return selection;
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

	public void setCurrentLine(int index) {
		this.currentLine = getLineByIndex(index);
	}

	public Line getFirstVisibleLine() {
		return getLineByIndex(firstVisibleRow);
	}

	public int getFirstVisibleRow() {
		return firstVisibleRow;
	}

	public void setFirstVisibleRow(int firstVisibleRow) {
		if (firstVisibleRow < 0) {
			firstVisibleRow = 0;
		}
		this.firstVisibleRow = firstVisibleRow;
		checkLastLinesNotEmpty();
		frameObserverable.notifyListeners(new FrameEvent(FrameEventType.VERTICAL, firstVisibleRow));
	}

	public void checkLastLinesNotEmpty() {
		int size = getSize();
		if (size - firstVisibleRow < rows) {
			firstVisibleRow = Math.max(0, size - rows);
		}
	}

	public int getFirstVisibleCol() {
		return firstVisibleCol;
	}

	public void setFirstVisibleCol(int firstVisibleCol) {
		if (firstVisibleCol < 0) {
			firstVisibleCol = 0;
		}
		this.firstVisibleCol = firstVisibleCol;
		checkLastColNotEmpty();
		frameObserverable.notifyListeners(new FrameEvent(FrameEventType.HORIZONTAL, firstVisibleCol));
	}

	public void checkLastColNotEmpty() {
		int size = getMaxCols();
		if (size - firstVisibleCol < cols) {
			firstVisibleCol = Math.max(0, size - cols);
		}
	}

	// Lines

	public List<Line> getVisibleLines() {
		List<Line> lines = new ArrayList<>();
		Integer i = new Integer(rows);
		Line line = getFirstVisibleLine();
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

	public int getCurrentLineIndex() {
		int curLineIndex = getLineIndex(currentLine);
		if (curLineIndex < firstVisibleRow) {
			return -1;
		} else if (curLineIndex >= firstVisibleRow + rows) {
			return -2;
		} else {
			return curLineIndex - firstVisibleRow;
		}
	}
	
	public char[] getLineCharsToShow(Line line) {
		return getCharsToShow(line.getText());
	}

	char[] getCharsToShow(char[] chars) {
		ArrayList<Character> out = new ArrayList<>();
		for (int i = firstVisibleCol; i < chars.length; i++) {
			char c = chars[i];
			if (c == '\t') {
				out.add(' ');
				out.add(' ');
				out.add(' ');
				out.add(' ');
			} else if (c == '\r' ) {
				out.add(' ');
			} else {
				out.add(c);
			}
		}
		Character[] array = out.toArray(new Character[0]);
		return ArrayUtils.toPrimitive(array);
	}

	// Selection

	public void startSelection(int lineIndex, int offsetToPaint) {
		selection.startLine = lineIndex;
		selection.startOffset = offsetToPaint;
	}

	public void selectTo(int lineIndex, int offsetToPaint) {
		selection.endLine = lineIndex;
		selection.endOffset = offsetToPaint;
	}

	public void clearSelection() {
		selection.clear();
	}
	
	public void removeSelection() {
		Line lineFrom = getLineByIndex(selection.getLineFrom());
		// TODO Auto-generated method stub
		selection.clear();
	}

	// O(n)

	/**
	 * Calculate total number of lines. O(n) operation.
	 * 
	 * @return size of document
	 */
	public int getSize() {
		int size = 1;
		Line l = firstLine;
		while (l.hasNext()) {
			l = l.getNext();
			size++;
		}
		return size;
	}

	/**
	 * Returns line by index. O(n) operation.
	 * 
	 * @param index
	 *            number of line
	 * @return line
	 */
	public Line getLineByIndex(int index) {
		Line l = firstLine;
		while (l.hasNext() && index > 0) {
			l = l.getNext();
			index--;
		}
		return l;
	}

	/**
	 * Get number of line in document. O(n) operation.
	 * 
	 * @param line
	 *            line of document
	 * @return line number
	 */
	public int getLineIndex(Line line) {
		int index = 0;
		Line l = firstLine;
		while (l.hasNext() && !l.equals(line)) {
			l = l.getNext();
			index++;
		}
		return index;
	}

	/**
	 * Calculate max width of document. O(n) operation.
	 * 
	 * @return length of the longest line in document
	 */
	public int getMaxCols() {
		int max = cols;
		Line l = firstLine;
		do {
			max = Math.max(l.getLengthToPaint(), max);
			l = l.getNext();
		} while (l != null);
		return max;
	}

	// For cursor

	public int getTargetLineOffset(Line targetLine) {
		int xToPaint = currentLine.getOffsetToPaint();

		int i = 0;
		for (Iterator<Character> iterator = targetLine.getChars().iterator(); iterator.hasNext();) {
			Character c = iterator.next();
			xToPaint -= c.equals('\t') ? SyntaxTextEditor.TAB_INDENT : 1;
			if (xToPaint < 0) {
				break;
			}
			i++;
		}
		return i;
	}

	/**
	 * 
	 * @return caret position considering {@link firstVisibleCol}
	 */
	public int getXToPaint() {
		int xToPaint = currentLine.getOffsetToPaint();
		return xToPaint - firstVisibleCol;
	}

	public int getXToPaint(Line l, int offset) {
		int xToPaint = l.getOffsetToPaint(offset);
		return xToPaint - firstVisibleCol;
	}

	// Save / Load

	public void setText(String text) {
		long t1 = System.currentTimeMillis();
		System.out.println("Start loading file");

		String[] split = text.split("\n");
		Line prev = null;
		for (int i = 0; i < split.length; i++) {
			Line l = null;
			if (i == 0) {
				l = firstLine;
			} else {
				l = new Line();
				prev.linkWith(l);
			}
			l.setText(split[i].toCharArray());
			prev = l;
			l.setOffset(0);
		}
		currentLine = firstLine;
		firstVisibleRow = 0;
		firstVisibleCol = 0;
		selection.clear();

		long t3 = System.currentTimeMillis();
		System.out.println("File loaded: " + (t3 - t1) + "ms");
	}

	public String getText() {
		StringBuilder sb = new StringBuilder();
		Line line = firstLine;
		do {
			for (int i = 0; i < line.getText().length; i++) {
				sb.append(line.getText()[i]);
			}
			if (line.hasNext()) {
				sb.append("\n");
			}
			line = line.getNext();
		} while (line != null);
		return sb.toString();
	}

}
