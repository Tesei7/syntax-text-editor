package ru.tesei7.textEditor.editor.painter;

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
		int rowHeight = g.getFontMetrics().getHeight();

		List<Line> lines = document.getVisibleLines();
		for (int i = 0; i < lines.size(); i++) {
			linePainter.paint(g, lines.get(i), rowHeight * (i + 1));
		}
	}

	

}
