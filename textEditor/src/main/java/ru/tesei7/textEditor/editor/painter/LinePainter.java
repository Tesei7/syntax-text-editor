package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Graphics;

import ru.tesei7.textEditor.editor.document.model.Line;

public class LinePainter {

	public void paint(Graphics g, Line line, int height) {
		g.setColor(Color.BLACK);
		char[] chars = line.getCharsToShow();
		g.drawChars(chars, 0, chars.length, 0, height);
	}

}
