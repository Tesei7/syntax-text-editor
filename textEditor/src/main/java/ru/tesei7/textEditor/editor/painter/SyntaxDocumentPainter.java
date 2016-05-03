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

	void paintSelection(Graphics g, List<Line> lines) {
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

			int textFrom = 0;
			int textTo = l.getLength();
			int from = 0, to = document.getCols() + 1;
			if (i == lineFrom) {
				Integer offsetToPaintFrom = selection.getOffsetToPaintFrom(l);
				from = Math.max(0, Math.min(offsetToPaintFrom, document.getCols()));
				textFrom = l.getOffsetToPaint(selection.getOffsetFrom());
			}
			if (i == lineTo) {
				Integer offsetToPaintTo = selection.getOffsetToPaintTo(l);
				to = Math.max(0, Math.min(offsetToPaintTo, document.getCols()));
				textTo = l.getOffsetToPaint(selection.getOffsetTo());
			}
			int y = getHeightToPaint(i);
			int by = fontProperties.getLineHeight() * i;
			linePainter.paintSelection(g, from, to, by, fontProperties);
			int x = fontProperties.getCharWidth() * from;
			linePainter.paintSelectionText(g, lineCharsToShow, textFrom, textTo, x, y, document);
		}
	}

	void paintLines(Graphics g, List<Line> lines, Language language) {
		for (int i = 0; i < lines.size(); i++) {
			int y = getHeightToPaint(i);
			Line line = lines.get(i);
			switch (language) {
			case PLAIN_TEXT:
				char[] textToPaint = line.getTextToPaint();
				int length = Math.max(0, textToPaint.length - document.getFirstVisibleCol());
				int offset = Math.min(textToPaint.length, document.getFirstVisibleCol());
				g.setFont(Fonts.DEFAULT);
				g.setColor(Colors.DEFAULT_TEXT);

				g.drawChars(textToPaint, offset, length, 0, y);
				break;
			default:
				linePainter.paintLine(g, line, y, fontProperties, document);
				break;
			}
		}
	}

	int getHeightToPaint(int i) {
		int rowHeight = fontProperties.getLineHeight();
		int descent = fontProperties.getDescent();
		int height = rowHeight * (i + 1) - descent;
		return height;
	}

}
