package ru.tesei7.textEditor.editor;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Line {
	private LinkedList<Token> tokens = new LinkedList<>();
	private LinkedList<Character> text = new LinkedList<>();
	private int offset = 0;

	public LinkedList<Token> getTokens() {
		return tokens;
	}

	public void paint(Graphics g, int row) {
		int offset = 0;
		for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext();) {
			Token token = (Token) iterator.next();
			char[] text = token.getText();
			g.drawChars(text, offset, text.length, 0, 17 * (row + 1));
			offset += text.length;
		}
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void addChar(char c) {
		text.add(offset, c);
		offset++;
	}

	public void deleteChar() {
		if (offset < 0 || offset >= text.size()) {
			offset = text.size();
		}
		text.remove(offset);
	}

	public void backspaceChar() {
		text.remove(offset);
	}

	public void left() {
		if (offset < text.size()) {
			offset++;
		}
	}

	public void right() {
		if (offset > 0) {
			offset--;
		}
	}

}
