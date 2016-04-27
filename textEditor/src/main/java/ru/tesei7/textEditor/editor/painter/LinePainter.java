package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fife.ui.rsyntaxtextarea.modes.Token;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.utils.Colors;
import ru.tesei7.textEditor.editor.utils.FontUtils;

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
		Token token = line.getToken();
		List<StyledText> styledText = getStyledText(token);
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

	private List<StyledText> getStyledText(Token token) {
		List<StyledText> list = new ArrayList<StyledText>();
		if (token == null) {
			return list;
		}
		int offset = 0;
		while (token != null && token.getType() != Token.NULL) {
			Color color = getColor(token.getType());
			Font font = getFont(token.getType());
			char[] text = getChars(token);
			list.add(new StyledText(text, offset, font, color));

			offset += text.length;
			token = token.getNextToken();
		}
		return list;
	}

	private char[] getChars(Token token) {
		char[] chars = new char[token.length()];
		System.arraycopy(token.getTextArray(), token.getOffset(), chars, 0, token.length());
		return new String(chars).replaceAll("\t", "    ").toCharArray();
	}

	private Color getColor(int type) {
		switch (type) {
		case Token.RESERVED_WORD:
		case Token.RESERVED_WORD_2:
			return Colors.KEY_WORD;
		case Token.COMMENT_EOL:
		case Token.COMMENT_MULTILINE:
		case Token.COMMENT_DOCUMENTATION:
		case Token.COMMENT_MARKUP:
		case Token.COMMENT_KEYWORD:
			return Colors.COMMENT;
		default:
			return Colors.DEFAULT_TEXT;
		}
	}

	private Font getFont(int type) {
		switch (type) {
		case Token.RESERVED_WORD:
		case Token.RESERVED_WORD_2:
			return FontUtils.BOLD;
		default:
			return FontUtils.DEFAULT;
		}
	}

}
