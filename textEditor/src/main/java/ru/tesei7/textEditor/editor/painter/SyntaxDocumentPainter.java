package ru.tesei7.textEditor.editor.painter;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentPainter {

	@Inject
	private LinePainter linePainter;
	@Inject
	private CaretPainter caretPainter;

	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	public void setEditor(SyntaxTextEditor syntaxTextEditor) {
		this.editor = syntaxTextEditor;
		document = editor.getDocument();
		caretPainter.setCaret(editor.getCaret());
	}

	public void paint(Graphics g) {
		caretPainter.paintCaret(g);
		paintLines(g);
	}

	private void paintLines(Graphics g) {
		int rowHeight = g.getFontMetrics().getHeight();

		List<Line> lines = document.getVisibleLines();
		for (int i = 0; i < lines.size(); i++) {
			linePainter.paint(g, lines.get(i), rowHeight * (i + 1));
		}
	}

	public int getTargetLineOffset(Line targetLine) {
		int xToPaint = caretPainter.getXToPaint();

		int i = 0;
		for (Iterator<Character> iterator = targetLine.getText().iterator(); iterator.hasNext();) {
			Character c = iterator.next();
			xToPaint -= c.equals('\t') ? 4 : 1;
			if (xToPaint < 0) {
				break;
			}
			i++;
		}
		return i;
	}

}
