package ru.tesei7.textEditor.editor;

public class FontProperties {
	private int charWidth;
	private int lineHeight;
	private int descent;

	public FontProperties(int charWidth, int lineHeight, int descent) {
		this.charWidth = charWidth;
		this.lineHeight = lineHeight;
		this.descent = descent;
	}

	public int getCharWidth() {
		return charWidth;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public int getDescent() {
		return descent;
	}
}
