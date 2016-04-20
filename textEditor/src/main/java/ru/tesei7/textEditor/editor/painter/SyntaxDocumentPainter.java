package ru.tesei7.textEditor.editor.painter;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.inject.Inject;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentPainter {

	@Inject
	private LinePainter linePainter;

	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	private SyntaxCaret caret;

	public void setEditor(SyntaxTextEditor syntaxTextEditor) {
		this.editor = syntaxTextEditor;
		document = editor.getDocument();
		caret = editor.getCaret();
	}

	public void paint(Graphics g) {
		paintCaret(g);
		paintLines(g);
	}

	private void paintLines(Graphics g) {
		int rowHeight = g.getFontMetrics().getHeight();

		List<Line> lines = document.getVisibleLines();
		for (int i = 0; i < lines.size(); i++) {
			linePainter.paint(g, lines.get(i), rowHeight * (i + 1));
		}
	}

	private void paintCaret(Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int ascent = fontMetrics.getAscent();
		int width = fontMetrics.stringWidth("a");

		int x = caret.getX() * width;
		int y = (height - ascent) + caret.getY() * height;
		g.fillRect(x, y, 2, ascent);
	}

}
