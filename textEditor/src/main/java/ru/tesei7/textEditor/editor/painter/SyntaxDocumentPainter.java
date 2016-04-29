package ru.tesei7.textEditor.editor.painter;

import java.awt.Graphics;
import java.util.List;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.document.model.TextSelection;
import ru.tesei7.textEditor.editor.utils.Colors;
import ru.tesei7.textEditor.editor.utils.Fonts;

public class SyntaxDocumentPainter {

	private LinePainter linePainter;

	private SyntaxDocument document;

	private FontProperties fontProperties;

	public SyntaxDocumentPainter(SyntaxDocument document, FontProperties fontProperties) {
		this.document = document;
		this.fontProperties = fontProperties;
		this.linePainter = new LinePainter();
	}

	public void paint(Graphics g, Language language) {
		List<Line> lines = document.getVisibleLines();

		paintLines(g, lines, language);
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
				from = Math.max(0, Math.min(selection.getOffsetToPaintFrom(l), document.getCols()));
			}
			if (i == lineTo) {
				to = Math.max(0, Math.min(selection.getOffsetToPaintTo(l), document.getCols()));
			}
			int y = getHeightToPaint(i);
			int by = fontProperties.getLineHeight() * i;
			linePainter.paintSelection(g, lineCharsToShow, from, to, by, y, fontProperties);
		}
	}

	private void paintLines(Graphics g, List<Line> lines, Language language) {
		for (int i = 0; i < lines.size(); i++) {
			int y = getHeightToPaint(i);
			Line line = lines.get(i);
			switch (language) {
			case PLAIN_TEXT:
				int length = Math.max(0, line.getLengthToPaint() - document.getFirstVisibleCol());
				int offset = Math.min(length, document.getFirstVisibleCol());
				g.setFont(Fonts.DEFAULT);
				g.setColor(Colors.DEFAULT_TEXT);				
				g.drawChars(line.getTextToPaint(), offset, length, 0, y);
				break;
			default:
				linePainter.paintLine(g, line, y, fontProperties, document);
				break;
			}
		}
	}

	private int getHeightToPaint(int i) {
		int rowHeight = fontProperties.getLineHeight();
		int descent = fontProperties.getDescent();
		int height = rowHeight * (i + 1) - descent;
		return height;
	}

}
