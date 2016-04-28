package ru.tesei7.textEditor.editor.syntax;

public class JavaToken implements Token {
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

	@Override
	public int getType() {
		return type;
	}

	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
