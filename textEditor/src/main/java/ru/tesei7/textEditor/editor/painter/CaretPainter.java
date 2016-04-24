package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.CaretType;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.utils.Colors;

public class CaretPainter {

	private SyntaxDocument document;

	public CaretPainter(SyntaxDocument document) {
		this.document = document;
	}

	public void paint(Graphics g, boolean caretVisible) {
		int caretRow = document.getCurrentLineY();
		if (caretRow < 0) {
			return;
		}

		paintBackground(g, caretRow);
		if (caretVisible) {
			paintCaret(g, caretRow);
		}
	}

	private void paintCaret(Graphics g, int caretRow) {
		g.setColor(Color.BLACK);
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int y = caretRow * height;
		int width = fontMetrics.stringWidth("a");
		int x = document.getXToPaint() * width;
		int caretWidth = document.getCaretType() == CaretType.INSERT ? width : SyntaxTextEditor.CARET_WIDTH;
		g.fillRect(x, y, caretWidth, height);
	}

	private void paintBackground(Graphics g, int caretRow) {
		g.setColor(Colors.CURRENT_LINE_BACKGROUND);
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int y = caretRow * height;
		int width = fontMetrics.stringWidth("a") * document.getCols();

		g.fillRect(0, y, width, height);
	}

}
