package ru.tesei7.textEditor.editor.painter;

import java.awt.Color;
import java.awt.Font;

public class StyledText {
	char[] text;
	int offset;
	Color color;
	Font font;	
	
	public StyledText(char[] text, int offset, Font font, Color color) {
		this.text = text;
		this.offset = offset;
		this.font = font;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return new String(text);
	}
}
