package ru.tesei7.textEditor.editor.painter;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxDocumentPainter {

	private LinePainter linePainter;

	private SyntaxDocument document;

	public SyntaxDocumentPainter(SyntaxDocument document) {
		this.document = document;
		this.linePainter = new LinePainter();
	}

	public void paint(Graphics g) {
		paintLines(g);
	}

	private void paintLines(Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int rowHeight = fontMetrics.getHeight();
		int descent = fontMetrics.getDescent();

		List<Line> lines = document.getVisibleLines();
		for (int i = 0; i < lines.size(); i++) {
			int height = rowHeight * (i + 1) - descent;
			int firstVisibleCol = document.getFirstVisibleCol();
			linePainter.paint(g, lines.get(i), height, firstVisibleCol);
		}
	}

}
