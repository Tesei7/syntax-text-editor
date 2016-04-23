package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

import ru.tesei7.textEditor.editor.document.model.Line;

public class LinePainter {

	public void paint(Graphics g, Line line, int height, int offset) {
		g.setColor(Color.BLACK);
		char[] chars = line.getCharsToShow();
		if (chars.length > offset) {
			char[] toShow = Arrays.copyOfRange(chars, offset, chars.length);
			g.drawChars(toShow, 0, toShow.length, 0, height);
		}
	}

}
