package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.utils.Colors;

public class CaretPainter {

	private SyntaxCaret caret;

	public CaretPainter(SyntaxCaret caret) {
		this.caret = caret;
	}

	public void paint(Graphics g, boolean caretVisible) {
		int caretRow = caret.getY();
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
		int x = caret.getXToPaint() * width;

		g.fillRect(x, y, 2, height);
	}

	private void paintBackground(Graphics g, int caretRow) {
		g.setColor(Colors.CURRENT_LINE_BACKGROUND);
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int y = caretRow * height;
		int width = fontMetrics.stringWidth("a") * caret.getCols();

		g.fillRect(0, y, width, height);
	}

}
