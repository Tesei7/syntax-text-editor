package ru.tesei7.textEditor.editor;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Line {
	private Line previous;
	private Line next;
	private List<Character> text = new LinkedList<>();
	private int offset;
	private LinkedList<Token> tokens = new LinkedList<>();

	public Line() {
		offset = 0;
	}

	public List<Character> getText() {
		return text;
	}

	public Line getPrevious() {
		return previous;
	}

	public boolean hasPrevious() {
		return previous != null;
	}

	public void setPrevious(Line previous) {
		this.previous = previous;
	}

	public Line getNext() {
		return next;
	}

	public boolean hasNext() {
		return next != null;
	}

	public void setNext(Line next) {
		this.next = next;
	}

	public LinkedList<Token> getTokens() {
		return tokens;
	}

	public char[] getChars() {
		return toPrimitive(text);
	}

	private char[] toPrimitive(List<Character> list) {
		char[] out = new char[list.size()];
		for (int i = 0; i < list.size(); i++) {
			out[i] = list.get(i);
		}
		return out;
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
		if (offset < 0) {
			this.offset = 0;
		} else if (offset > text.size()) {
			this.offset = text.size();
		} else {
			this.offset = offset;
		}
	}

	public void addChar(char c) {
		text.add(offset, c);
		offset++;
	}

	public void deleteChar() {
		if (offset == text.size()) {
			// TODO concat lines
			return;
		}
		text.remove(offset - 1);
	}

	public void backspaceChar() {
		if (offset == 0) {
			// TODO concat lines
			return;
		}
		text.remove(offset - 1);
		left();
	}

	public void setText(List<Character> text) {
		this.text = new LinkedList<>(text);
		offset = text.size();
	}

	public void right() {
		if (offset < text.size()) {
			offset++;
		}
	}

	public void left() {
		if (offset > 0) {
			offset--;
		}
	}

	public int getLenght() {
		return text.size();
	}

}
