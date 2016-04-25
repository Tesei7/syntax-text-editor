package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.utils.Colors;

public class LinePainter {

	public void paintSelection(Graphics g, char[] chars, int from, int to, int backgroundY, int textY, FontProperties fp) {
		char[] toShow = Arrays.copyOfRange(chars, from, to);

		g.setColor(Colors.SELECTED_BACKGROUND);
		int x = fp.getCharWidth() * from;
		int width = fp.getCharWidth() * toShow.length;
		int height = fp.getLineHeight();
		g.fillRect(x, backgroundY, width, height);

		g.setColor(Color.WHITE);
		g.drawChars(toShow, 0, toShow.length, x, textY);
	}

}
