package ru.tesei7.textEditor.editor.painter;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentPainter {

	private LinePainter linePainter;

	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	public SyntaxDocumentPainter(SyntaxTextEditor syntaxTextEditor) {
		this.editor = syntaxTextEditor;
		this.document = editor.getDocument();
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
			linePainter.paint(g, lines.get(i), 
//					0
					rowHeight * (i + 1) - descent
					);
		}
	}

	

}
