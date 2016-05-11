package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.utils.Colors;

public class CaretPainter {

	private SyntaxDocument document;
	BracketService bracketPositionService;

	public CaretPainter(SyntaxDocument document) {
		this.document = document;
		this.bracketPositionService = new BracketService(document);
	}

	public void paintCaret(Graphics g, boolean caretVisible) {
		if (!caretVisible) {
			return;
		}
		int caretRow = document.getCurLineIndexToPaint();
		g.setColor(Color.BLACK);
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int y = caretRow * height;
		int width = fontMetrics.stringWidth("a");
		int x = document.getXToPaint() * width;
		int caretWidth = document.getCaretType() == CaretType.INSERT ? width : SyntaxTextEditor.CARET_WIDTH;
		g.fillRect(x, y, caretWidth, height);
	}

	public void paintBackground(Graphics g) {
		int caretRow = document.getCurLineIndexToPaint();
		g.setColor(Colors.CURRENT_LINE_BACKGROUND);
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int y = caretRow * height;
		int width = fontMetrics.stringWidth("a") * (document.getCols() + 1);

		g.fillRect(0, y, width, height);
	}

	public void highlightBrackets(Graphics g) {
		if (document.getLanguage() == Language.PLAIN_TEXT) {
			return;
		}
		int position = bracketPositionService.getBracketAtCurrentPosition();
		if (position < 0) {
			return;
		}

		int[] coordinates = bracketPositionService.getRelativeBracket();
		int caretRow = document.getCurLineIndexToPaint();
		int offset = document.getCurrentLine().getOffsetToPaint() - document.getFirstVisibleCol();
		if (coordinates == null) {
			highlightBracket(g, caretRow, offset, position, Colors.NOT_FOUND_BRACKET_BACKGROUND);
		} else {
			highlightBracket(g, caretRow, offset, position, Colors.FOUND_BRACKET_BACKGROUND);
			highlightBracket(g, coordinates[0], coordinates[1], 0, Colors.FOUND_BRACKET_BACKGROUND);
		}
	}

	/**
	 * Highlight char in position with color
	 * 
	 * @param g
	 *            graphics object
	 * @param row
	 *            row number (0 is first visible row) of char to highlight
	 * @param col
	 *            column number (0 is first visible column) of char to highlight
	 * @param position
	 *            0 - highlight char after position, 1 - before position
	 * @param color
	 *            color to highlight with
	 */
	void highlightBracket(Graphics g, int row, int col, int position, Color color) {
		g.setColor(color);
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int y = row * height;
		int width = fontMetrics.stringWidth("a");
		int x = width * (col - position);
		g.fillRect(x, y, width, height);
	}

}
