package ru.tesei7.textEditor.editor.utils;

import java.awt.Font;

public class FontUtils {
	private Font font;
	
	public FontUtils() {
		font = new Font("Monospaced", Font.PLAIN, 12);
	}
	
	public Font getFont(){
		return font;
	}
}
