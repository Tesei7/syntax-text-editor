package ru.tesei7.textEditor.editor.painter;

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

		int lineFrom = (selection.isReversed() ? selection.getEndLine() : selection.getStartLine())
				- document.getFirstVisibleRow();
		int lineTo = (selection.isReversed() ? selection.getStartLine() : selection.getEndLine())
				- document.getFirstVisibleRow();
		if (lineFrom < 0) {
			lineFrom = 0;
		}
		if (lineTo > lines.size() - 1) {
			lineTo = lines.size() - 1;
		}

		for (int i = lineFrom; i <= lineTo; i++) {
			char[] lineCharsToShow = document.getLineCharsToShow(lines.get(i));
			int from = 0, to = lineCharsToShow.length;
			if (i == lineFrom) {
				from = selection.isReversed() ? selection.getEndOffset() : selection.getStartOffset();
			}
			if (i == lineTo) {
				to = Math.min(selection.isReversed() ? selection.getStartOffset() : selection.getEndOffset(),
						lineCharsToShow.length);
			}
			int y = getHeightToPaint(i);
			int by = fontProperties.getLineHeight() * i;
			linePainter.paintSelection(g, lineCharsToShow, from, to, by, y, fontProperties);
		}
	}

	private void paintLines(Graphics g, List<Line> lines) {
		for (int i = 0; i < lines.size(); i++) {
			linePainter.paint(g, document.getLineCharsToShow(lines.get(i)), getHeightToPaint(i));
		}
	}

	private int getHeightToPaint(int i) {
		int rowHeight = fontProperties.getLineHeight();
		int descent = fontProperties.getDescent();
		int height = rowHeight * (i + 1) - descent;
		return height;
	}

}
