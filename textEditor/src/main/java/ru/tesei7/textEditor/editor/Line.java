package ru.tesei7.textEditor.editor;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Line {
	private Line previous;
	private Line next;
	private LinkedList<Character> text = new LinkedList<>();
	private int offset = 0;
	private LinkedList<Token> tokens = new LinkedList<>();

	public Line getPrevious() {
		return previous;
	}

	public boolean hasPrevious() {
		return previous != null;
	}

	public Line getNext() {
		return next;
	}

	public boolean hasNext() {
		return next != null;
	}

	public LinkedList<Token> getTokens() {
		return tokens;
	}

	public char[] getText() {
		char[] out = new char[text.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = text.get(i);
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
		this.offset = offset;
	}

	public void addChar(char c) {
		if (c == '\n') {
			addNewLine();
		} else {
			text.add(offset, c);
			offset++;
		}
	}

	private void addNewLine() {
		// TODO Auto-generated method stub
		
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

}
