package ru.tesei7.textEditor.editor.syntax;

public class JavaToken {
	private int type;
	private String text;

	public JavaToken(int type, String text) {
		this.type = type;
		this.text = text;
	}

	public JavaToken(int type, char c) {
		this.type = type;
		this.text = new String(new char[] { c });
	}

	public int getType() {
		return type;
	}

	public String getText() {
		return text;
	}
}
