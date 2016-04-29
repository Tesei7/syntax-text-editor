package ru.tesei7.textEditor.editor.syntax;

public class TokenImpl implements Token {
	private int type;
	private String text;
	private int offset;

	public TokenImpl(int type, String text, int offset) {
		this.type = type;
		this.text = text;
		this.offset = offset;
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

	@Override
	public int getOffset() {
		return offset;
	}
}
