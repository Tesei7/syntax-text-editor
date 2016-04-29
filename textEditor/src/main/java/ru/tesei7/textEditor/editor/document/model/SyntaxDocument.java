package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

import org.apache.commons.lang.ArrayUtils;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;
import ru.tesei7.textEditor.editor.syntax.JavaTokenizer;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.Tokenizer;

/**
 * Stores text data in list of {@link Line}s.
 * 
 * @author Ilya
 *
 */
public class SyntaxDocument {
	/**
	 * Number of visible rows
	 */
	int rows = SyntaxTextEditor.DEFAULT_ROWS;
	/**
	 * Number of visible columns
	 */
	int cols = SyntaxTextEditor.DEFAULT_COLS;
	/**
	 * Number of columns in fixed width mode
	 */
	int maxCols = SyntaxTextEditor.DEFAULT_MAX_COLS;
	/**
	 * Language to highlight
	 */
	Language language;
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
	/**
	 * Read tokens from line
	 */
	private TokenCalculator tokenCalculator;
	/**
	 * Creates tokenizer
	 */
	private TokenizerFactory tokenizerFactory;

	public SyntaxDocument(FrameObserverable frameObserverable) {
		this.frameObserverable = frameObserverable;
		selection = new TextSelection(this);
		lines.add(new Line());
		tokenizerFactory = new TokenizerFactory();
		tokenCalculator = new TokenCalculator();
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

	// LANGUAGE

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
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
			if (lineFrom == lineTo) {
				chars = chars.subList(selection.getOffsetFrom(), selection.getOffsetTo());
			} else {
				if (i == lineFrom) {
					chars = chars.subList(selection.getOffsetFrom(), chars.size());
				}
				if (i == lineTo) {
					chars = chars.subList(0, selection.getOffsetTo());
				}
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
		curLineIndex = 0;
		selection.clear();

		if (language != Language.PLAIN_TEXT) {
			recalcTokens(0, split.length);
		}

		long t3 = System.currentTimeMillis();
		System.out.println("File loaded: " + (t3 - t1) + "ms");
	}

	public void recalcTokens(int firstLineIndex, int lines) {
		if (getLanguage() == Language.PLAIN_TEXT) {
			return;
		}
		for (int i = firstLineIndex; i < firstLineIndex + lines; i++) {
			// do not infinite loop
			if (i >= getSize()) {
				return;
			}

			int prevState = JavaTokenizer.YYINITIAL;
			if (i != 0) {
				prevState = getLineByIndex(i - 1).getLastTokenState();
			}
			Line l = getLineByIndex(i);
			Tokenizer tokenizer = tokenizerFactory.createTokenizer(getLanguage(), l.getText(), prevState);
			List<Token> tokens = new ArrayList<>();
			int newState = tokenCalculator.readTokens(tokens, tokenizer);
			if (newState == JavaTokenizer.STRING || newState == JavaTokenizer.CHARLITERAL) {
				newState = JavaTokenizer.YYINITIAL;
			}
			l.setTokens(tokens);
			int oldState = l.getLastTokenState();
			l.setLastTokenState(newState);

			// recalculate multiline tokens till the end
			boolean isLastRecalculatedLine = i == firstLineIndex + lines - 1;
			if (newState != oldState && isLastRecalculatedLine) {
				lines++;
			}
		}
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
