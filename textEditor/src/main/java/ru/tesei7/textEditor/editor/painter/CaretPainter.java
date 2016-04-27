package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.utils.Colors;

public class CaretPainter {

	private SyntaxDocument document;

	public CaretPainter(SyntaxDocument document) {
		this.document = document;
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

}
