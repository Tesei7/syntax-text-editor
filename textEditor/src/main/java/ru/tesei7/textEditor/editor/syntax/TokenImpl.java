package ru.tesei7.textEditor.editor.syntax;

public class TokenImpl implements Token {
	private int type;
	private int offset;
	private int length;

	public TokenImpl(int type, int offset, int length) {
		this.type = type;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getLength() {
		return length;
	}
}
