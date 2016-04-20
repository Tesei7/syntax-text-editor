package ru.tesei7.textEditor.editor.painter;

import java.awt.Graphics;

import ru.tesei7.textEditor.editor.Line;

public class LinePainter {

	public void paint(Graphics g, Line line, int height) {
		g.drawChars(line.getText(), 0, line.getText().length, 0, height);
		

	}

}
