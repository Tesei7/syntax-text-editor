package ru.tesei7.textEditor.editor.painter;

import java.awt.FontMetrics;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;

public class CaretPainter {

	private SyntaxCaret caret;

	public CaretPainter(SyntaxCaret caret) {
		this.caret = caret;
	}

	public void paint(Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int width = fontMetrics.stringWidth("a");

		int x = caret.getXToPaint() * width;
		int y = caret.getY() * height;
		g.fillRect(x, y, 2, height);
	}

	
}
