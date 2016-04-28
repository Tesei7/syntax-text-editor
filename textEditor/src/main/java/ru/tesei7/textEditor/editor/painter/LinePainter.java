package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;
import ru.tesei7.textEditor.editor.utils.Colors;
import ru.tesei7.textEditor.editor.utils.Fonts;

public class LinePainter {

	public void paintSelection(Graphics g, char[] chars, int from, int to, int backgroundY, int textY,
			FontProperties fp) {
		char[] toShow = Arrays.copyOfRange(chars, from, to);

		g.setColor(Colors.SELECTED_BACKGROUND);
		int x = fp.getCharWidth() * from;
		int width = fp.getCharWidth() * toShow.length;
		int height = fp.getLineHeight();
		g.fillRect(x, backgroundY, width, height);

		g.setColor(Color.WHITE);
		g.drawChars(toShow, 0, toShow.length, x, textY);
	}

	public void paintLine(Graphics g, Line line, int y, FontProperties fp, SyntaxDocument document) {
		int firstVisibleCol = document.getFirstVisibleCol();
		List<StyledText> styledText = getStyledText(line.getTokens());
		int x = 0;
		for (int i = 0; i < styledText.size(); i++) {
			StyledText st = styledText.get(i);
			g.setFont(st.font);
			g.setColor(st.color);

			int offset = 0;
			int length = 0;
			if (st.offset >= firstVisibleCol) {
				offset = 0;
				length = st.text.length;
			} else {
				offset = Math.min(st.text.length, firstVisibleCol - st.offset);
				length = Math.max(0, st.text.length - (firstVisibleCol - st.offset));
			}

			g.drawChars(st.text, offset, length, fp.getCharWidth() * x, y);
			x += length;
		}
	}

	private List<StyledText> getStyledText(List<ru.tesei7.textEditor.editor.syntax.Token> tokens) {
		List<StyledText> list = new ArrayList<StyledText>();
		if (tokens == null) {
			return list;
		}
		int offset = 0;
		
		for (ru.tesei7.textEditor.editor.syntax.Token t : tokens) {
			Color color = getColor(t.getType());
			Font font = getFont(t.getType());
			char[] text = getChars(t);
			list.add(new StyledText(text, offset, font, color));
			offset += text.length;
		}
		return list;
	}

	private char[] getChars(ru.tesei7.textEditor.editor.syntax.Token token) {
		return token.getText().replaceAll("\t", "    ").toCharArray();
	}

	private Color getColor(int type) {
		switch (type) {
		case TokenTypes.KEYWORD:
			return Colors.KEY_WORD;
		case TokenTypes.COMMENT_EOL:
		case TokenTypes.COMMENT_MULTI:
			return Colors.COMMENT;
		case TokenTypes.IDENTIFIER:
			return Colors.IDENTIFIER;
		default:
			return Colors.DEFAULT_TEXT;
		}
	}

	private Font getFont(int type) {
		switch (type) {
		case TokenTypes.KEYWORD:
			return Fonts.BOLD;
		default:
			return Fonts.DEFAULT;
		}
	}

}
