package ru.tesei7.textEditor.editor.document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class Line {
	private Line previous;
	private Line next;
	private List<Character> text = new LinkedList<>();
	private int offset;
	private LinkedList<Token> tokens = new LinkedList<>();

	public Line() {
		offset = 0;
	}

	// Prev/Next

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

	// Text

	public List<Character> getText() {
		return text;
	}

	public void setText(List<Character> text) {
		this.text = new LinkedList<>(text);
		offset = text.size();
	}

	public int getLenght() {
		return text.size();
	}
	
	public char[] getChars() {
		return toPrimitive(text);
	}

	public char[] getCharsToShow() {
		ArrayList<Character> out = new ArrayList<>();
		for (int i = 0; i < text.size(); i++) {
			Character character = text.get(i);
			if (character.equals('\t')) {
				out.add(' ');
				out.add(' ');
				out.add(' ');
				out.add(' ');
			} else {
				out.add(character);
			}
		}
		return toPrimitive(out);
	}

	private char[] toPrimitive(List<Character> list) {
		Character[] array = list.toArray(new Character[0]);
		return ArrayUtils.toPrimitive(array);
	}

	// public void paint(Graphics g, int row) {
	// int offset = 0;
	// for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext();) {
	// Token token = (Token) iterator.next();
	// char[] text = token.getText();
	// g.drawChars(text, offset, text.length, 0, 17 * (row + 1));
	// offset += text.length;
	// }
	// }

	// Offset

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

}
