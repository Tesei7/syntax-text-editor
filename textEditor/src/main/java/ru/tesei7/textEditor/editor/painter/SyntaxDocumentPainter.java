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

		boolean isReversedLines = selection.getStartLine() > selection.getEndLine();
		int lineFrom = (isReversedLines ? selection.getEndLine() : selection.getStartLine())
				- document.getFirstVisibleRow();
		int lineTo = (isReversedLines ? selection.getStartLine() : selection.getEndLine())
				- document.getFirstVisibleRow();
		if (lineFrom < 0) {
			lineFrom = 0;
		}
		if (lineTo > lines.size() - 1) {
			lineTo = lines.size() - 1;
		}

		for (int i = lineFrom; i <= lineTo; i++) {
			Line l = lines.get(i);
			int startOffset = selection.getStartOffset(l);
			int endOffset = selection.getEndOffset(l);
			boolean isReversedOffset = isReversedLines || (lineFrom == lineTo && startOffset > endOffset);
			char[] lineCharsToShow = document.getLineCharsToShow(l);

			int from = 0, to = lineCharsToShow.length;
			if (i == lineFrom) {
				from = Math.min(isReversedOffset ? endOffset : startOffset, lineCharsToShow.length);
			}
			if (i == lineTo) {
				to = Math.min(isReversedOffset ? startOffset : endOffset, lineCharsToShow.length);
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
