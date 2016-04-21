package ru.tesei7.textEditor.editor.painter;

import java.awt.FontMetrics;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.document.Line;

public class CaretPainter {
	
	private SyntaxCaret caret;

	public void setCaret(SyntaxCaret caret){
		this.caret = caret;
	}

	public void paintCaret(Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int ascent = fontMetrics.getAscent();
		int width = fontMetrics.stringWidth("a");

		int x = getXToPaint() * width;
		int y = (height - ascent) + caret.getY() * height;
		g.fillRect(x, y, 2, height);
	}
	
	public int getXToPaint() {
		int x = 0;
		Line currentLine = caret.getCurrentLine();
		for (int i = 0; i < currentLine.getOffset(); i++) {
			Character c = currentLine.getText().get(i);
			if (c.equals('\t')) {
				x += 4;
			} else {
				x += 1;
			}
		}
		return x;
	}
}
