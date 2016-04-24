package ru.tesei7.textEditor.editor;

public class FontProperties {
	private int charWidth;
	private int lineHeight;

	public FontProperties(int charWidth, int lineHeight) {
		this.charWidth = charWidth;
		this.lineHeight = lineHeight;
	}

	public int getCharWidth() {
		return charWidth;
	}

	public int getLineHeight() {
		return lineHeight;
	}
}
