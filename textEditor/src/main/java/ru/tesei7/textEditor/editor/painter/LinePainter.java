package ru.tesei7.textEditor.editor.painter;

import java.awt.Graphics;

import ru.tesei7.textEditor.editor.document.Line;

public class LinePainter {

	public void paint(Graphics g, Line line, int height) {
		char[] chars = line.getCharsToShow();
		g.drawChars(chars, 0, chars.length, 0, height);
	}

}
