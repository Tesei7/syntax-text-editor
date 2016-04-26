package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.document.model.TextSelection;

public class SyntaxDocumentPainter {

	private LinePainter linePainter;

	private SyntaxDocument document;

	private FontProperties fontProperties;

	public SyntaxDocumentPainter(SyntaxDocument document, FontProperties fontProperties) {
		this.document = document;
		this.fontProperties = fontProperties;
		this.linePainter = new LinePainter();
	}

	public void paint(Graphics g) {
		List<Line> lines = document.getVisibleLines();

		paintLines(g, lines);
		paintSelection(g, lines);
	}

	private void paintSelection(Graphics g, List<Line> lines) {
		TextSelection selection = document.getSelection();
		if (selection.notSelected()) {
			return;
		}

		int lineFrom = selection.getLineFrom() - document.getFirstVisibleRow();
		int lineTo = selection.getLineTo() - document.getFirstVisibleRow();

		for (int i = lineFrom; i <= lineTo; i++) {
			if (i < 0 || i > lines.size() - 1) {
				continue;
			}

			Line l = lines.get(i);
			char[] lineCharsToShow = document.getLineCharsToShow(l);

			int from = 0, to = document.getCols();
			if (i == lineFrom) {
				from = Math.max(0, Math.min(selection.getOffsetFrom(l), document.getCols()));
			}
			if (i == lineTo) {
				to = Math.max(0, Math.min(selection.getOffsetTo(l), document.getCols()));
			}
			int y = getHeightToPaint(i);
			int by = fontProperties.getLineHeight() * i;
			linePainter.paintSelection(g, lineCharsToShow, from, to, by, y, fontProperties);
		}
	}

	private void paintLines(Graphics g, List<Line> lines) {
		for (int i = 0; i < lines.size(); i++) {
			char[] chars = document.getLineCharsToShow(lines.get(i));
			int y = getHeightToPaint(i);
			g.setColor(Color.BLACK);
			g.drawChars(chars, 0, chars.length, 0, y);
		}
	}

	private int getHeightToPaint(int i) {
		int rowHeight = fontProperties.getLineHeight();
		int descent = fontProperties.getDescent();
		int height = rowHeight * (i + 1) - descent;
		return height;
	}

}
