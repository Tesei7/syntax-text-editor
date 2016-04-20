package ru.tesei7.textEditor.editor.painter;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.inject.Inject;

import ru.tesei7.textEditor.editor.Line;
import ru.tesei7.textEditor.editor.SyntaxDocument;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class SyntaxDocumentPainter {

	@Inject
	private LinePainter linePainter;

	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	public void setEditor(SyntaxTextEditor syntaxTextEditor) {
		this.editor = syntaxTextEditor;
		document = editor.getDocument();
	}

	public void paint(Graphics g) {
		paintCaret(g);
		paintLines(g);
	}

	private void paintLines(Graphics g) {
		int rowHeight = g.getFontMetrics().getHeight();

		List<Line> lines = document.getLines();
		for (int i = 0; i < lines.size(); i++) {
			linePainter.paint(g, lines.get(i), rowHeight * (i + 1));
		}
	}

	private void paintCaret(Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int ascent = fontMetrics.getAscent();
		int width = fontMetrics.stringWidth("a");

		Line currentLine = document.getCurrentLine();
		g.drawRect(currentLine.getOffset() * width, (height - ascent) + (document.getCurrentLineRow()) * height, 0,
				ascent);
	}

}
