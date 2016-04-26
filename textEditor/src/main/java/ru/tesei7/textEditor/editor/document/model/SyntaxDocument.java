package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

import javax.swing.text.Segment;

import org.apache.commons.lang.ArrayUtils;
import org.fife.ui.rsyntaxtextarea.modes.JavaTokenMaker;
import org.fife.ui.rsyntaxtextarea.modes.Token;

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
	static final int DEFAULT_MAX_COLS = 256;

	/**
	 * Number of visible rows
	 */
	int rows = DEFAULT_ROWS;
	/**
	 * Number of visible columns
	 */
	int cols = DEFAULT_COLS;
	/**
	 * Number of columns in fixed width mode
	 */
	int maxCols = DEFAULT_MAX_COLS;
	/**
	 * Type of representation
	 */
	SyntaxTextEditorViewMode viewMode = SyntaxTextEditorViewMode.DEFAULT;
	/**
	 * Type of care
	 */
	CaretType caretType = CaretType.NORMAL;
	/**
	 * List of lines of the document
	 */
	List<Line> lines = new ArrayList<>(SyntaxTextEditor.DEFAULT_LINES_COUNT);
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
	
	private JavaTokenMaker javaTokenMaker;

	public SyntaxDocument(FrameObserverable frameObserverable) {
		this.frameObserverable = frameObserverable;
		selection = new TextSelection(this);
		lines.add(new Line());
		javaTokenMaker = new JavaTokenMaker();
	}

	// ROWS & COLS

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

	public void setMaxCols(int maxCols) {
		this.maxCols = maxCols;
	}

	// VIEW MODE

	public SyntaxTextEditorViewMode getViewMode() {
		return viewMode;
	}

	public void setViewMode(SyntaxTextEditorViewMode viewMode) {
		this.viewMode = viewMode;
	}

	// CARET

	public CaretType getCaretType() {
		return caretType;
	}

	public void setCaretType(CaretType caretType) {
		this.caretType = caretType;
	}

	// CUR LINE

	public int getCurLineIndex() {
		return curLineIndex;
	}

	public void setCurLineIndex(int curLineIndex) {
		this.curLineIndex = getCorrectLineIndex(curLineIndex);
	}

	public void moveCurLineIndex(int reletive) {
		setCurLineIndex(curLineIndex + reletive);
	}

	public Line getCurrentLine() {
		return lines.get(curLineIndex);
	}

	// VISIBLE

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

	public Line getFirstVisibleLine() {
		return getLineByIndex(firstVisibleRow);
	}

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

	// Lines

	public void addLineAfter(int index, Line l) {
		lines.add(index + 1, l);
	}

	public void removeLineAfter(int index) {
		lines.remove(index + 1);
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

	/**
	 * Returns line by index.
	 * 
	 * @param index
	 *            number of line
	 * @return line
	 */
	public Line getLineByIndex(int index) {
		return lines.get(getCorrectLineIndex(index));
	}

	int getCorrectLineIndex(int index) {
		if (lines.size() == 0) {
			return 0;
		}
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

	// Selection

	public TextSelection getSelection() {
		return selection;
	}

	public void startSelection(int lineIndex, int offsetToPaint) {
		selection.setStartLine(lineIndex);
		selection.setStartOffset(offsetToPaint);
	}

	public void selectTo(int lineIndex, int offsetToPaint) {
		selection.setEndLine(lineIndex);
		selection.setEndOffset(offsetToPaint);
	}

	public void clearSelection() {
		selection.clear();
	}

	public void removeSelection() {
		Integer lineFrom = selection.getLineFrom();
		Integer lineTo = selection.getLineTo();

		Line l1 = getLineByIndex(lineFrom);
		Line l2 = getLineByIndex(lineTo);
		LinkedList<Character> add = new LinkedList<>();
		List<Character> subList1 = l1.getChars().subList(0, selection.getOffsetFrom());
		List<Character> subList2 = l2.getChars().subList(selection.getOffsetTo(), l2.getLength());
		add.addAll(subList1);
		add.addAll(subList2);
		l1.setChars(add);
		l1.setOffset(subList1.size());

		if (lineFrom < lineTo) {
			lines.subList(lineFrom + 1, lineTo + 1).clear();
		}
		setCurLineIndex(lineFrom);
		selection.clear();
	}

	public String getSelectedString() {
		Integer lineFrom = selection.getLineFrom();
		Integer lineTo = selection.getLineTo();

		StringBuilder sb = new StringBuilder();
		for (int i = lineFrom; i <= lineTo; i++) {
			List<Character> chars = getLineByIndex(i).getChars();
			if (i == lineFrom) {
				chars = chars.subList(selection.getOffsetFrom(), chars.size());
			}
			if (i == lineTo) {
				chars = chars.subList(0, selection.getOffsetTo());
			}
			for (Character c : chars) {
				sb.append(c);
			}
			if (i != lineTo) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	// Dimensions

	/**
	 * Calculate total number of lines.
	 * 
	 * @return size of document
	 */
	public int getSize() {
		return lines.size();
	}

	/**
	 * Calculate max width of document. O(n)
	 * 
	 * @return length of the longest line in document
	 */
	public int getMaxCols() {
		if (viewMode == SyntaxTextEditorViewMode.FIXED_WIDTH) {
			return maxCols;
		} else {
			OptionalInt max = lines.parallelStream().mapToInt(l -> l.getLengthToPaint()).max();
			return max.isPresent() ? max.getAsInt() : cols;
		}
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

		int lastToken = Token.NULL;
		lines.clear();
		String[] split = text.split("\n");
		for (int i = 0; i < split.length; i++) {
			Line l = new Line();
			l.setText(split[i].toCharArray());
			l.setOffset(0);
			lines.add(l);
			lastToken = recalcTokens(l, lastToken);
		}
		firstVisibleRow = 0;
		firstVisibleCol = 0;
		selection.clear();

		long t3 = System.currentTimeMillis();
		System.out.println("File loaded: " + (t3 - t1) + "ms");
	}

	private int recalcTokens(Line l, int lastToken) {
		Token tokenList = javaTokenMaker.getTokenList(new Segment(l.getText(), 0, l.getText().length), lastToken, 0);
		l.getTokens().add(tokenList);
		while(tokenList.getNextToken()!=null){
			tokenList = tokenList.getNextToken();
			l.getTokens().add(tokenList);
		}
		return tokenList.getType();
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
