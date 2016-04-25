package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;

import org.apache.commons.lang.ArrayUtils;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;

/**
 * Stores text data in list of {@link Line}s.
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
	int rows = DEFAULT_ROWS;
	/**
	 * Number of visible columns
	 */
	int cols = DEFAULT_COLS;
	/**
	 * Type of care
	 */
	CaretType caretType = CaretType.NORMAL;
	/**
	 * List of lines of the document
	 */
	List<Line> lines = new ArrayList<>(1000000);
	/**
	 * Current Line Index
	 */
	int curLineIndex = 0;
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
		selection = new TextSelection(this);
		lines.add(new Line());
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

	public int getCurLineIndex() {
		return curLineIndex;
	}

	public void setCurLineIndex(int curLineIndex) {
		this.curLineIndex = getCorrectIndex(curLineIndex);
	}

	public void moveCurLineIndex(int reletive) {
		setCurLineIndex(curLineIndex + reletive);
	}

	public TextSelection getSelection() {
		return selection;
	}

	public void addLineAfter(int index, Line l) {
		lines.add(index + 1, l);
	}

	public Line getCurrentLine() {
		return lines.get(curLineIndex);
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
		int endLine = Math.min(firstVisibleRow + rows, getSize());
		return new ArrayList<>(lines.subList(firstVisibleRow, endLine));
	}

	public int getCurLineIndexToPaint() {
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
			} else if (c == '\r') {
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

	/**
	 * Calculate total number of lines.
	 * 
	 * @return size of document
	 */
	public int getSize() {
		return lines.size();
	}

	/**
	 * Returns line by index.
	 * 
	 * @param index
	 *            number of line
	 * @return line
	 */
	public Line getLineByIndex(int index) {
		return lines.get(getCorrectIndex(index));
	}

	private int getCorrectIndex(int index) {
		if (index < 0) {
			index = 0;
		}
		if (index > lines.size() - 1) {
			index = lines.size() - 1;
		}
		return index;
	}

	public boolean isCorrectLineIndex(int index) {
		if (index < 0) {
			return false;
		}
		if (index > lines.size() - 1) {
			return false;
		}
		return true;
	}

	/**
	 * Calculate max width of document. O(n)
	 * 
	 * @return length of the longest line in document
	 */
	public int getMaxCols() {
		OptionalInt max = lines.parallelStream().mapToInt(l -> l.getLengthToPaint()).max();
		return max.isPresent() ? max.getAsInt() : cols;
	}

	// For cursor

	public int getTargetLineOffset(Line targetLine) {
		int xToPaint = getCurrentLine().getOffsetToPaint();

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
		int xToPaint = getCurrentLine().getOffsetToPaint();
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

		lines.clear();
		String[] split = text.split("\n");
		for (int i = 0; i < split.length; i++) {
			Line l = new Line();
			l.setText(split[i].toCharArray());
			l.setOffset(0);
			lines.add(l);
		}
		firstVisibleRow = 0;
		firstVisibleCol = 0;
		selection.clear();

		long t3 = System.currentTimeMillis();
		System.out.println("File loaded: " + (t3 - t1) + "ms");
	}

	public String getText() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.size(); i++) {
			Line l = lines.get(i);
			for (int j = 0; j < l.getText().length; j++) {
				sb.append(l.getText()[j]);
			}
			if (i != lines.size() - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
