package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * Line of text document. Contains characters and calculated list of tokens.
 * 
 * @author Ilya Bochkarev
 *
 */
public class Line {
	private Line previous;
	private Line next;
	private char[] text = new char[0];
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

	/**
	 * Atomic operation for line connection. Links {@code this} line with
	 * {@code l}, making {@code l} next for {@code this} and this previous for l
	 * 
	 * @param l
	 *            next line or null if {@code this} is last line
	 */
	public void linkWith(Line l) {
		next = l;
		if (l != null) {
			l.previous = this;
		}
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

	// Text

	public List<Character> getChars() {
		return toList();
	}

	public void setChars(List<Character> chars) {
		char[] array = toArray(chars);
		setText(array);
	}

	public void setText(char[] text) {
		this.text = text;
		offset = text.length;
	}

	public int getLenght() {
		return text.length;
	}

	public char[] getCharsToShow() {
		ArrayList<Character> out = new ArrayList<>();
		for (int i = 0; i < text.length; i++) {
			Character character = text[i];
			if (character.equals('\t')) {
				out.add(' ');
				out.add(' ');
				out.add(' ');
				out.add(' ');
			} else {
				out.add(character);
			}
		}
		return toArray(out);
	}

	private char[] toArray(List<Character> list) {
		Character[] array = list.toArray(new Character[0]);
		return ArrayUtils.toPrimitive(array);
	}

	private LinkedList<Character> toList() {
		LinkedList<Character> list = new LinkedList<>();
		for (int i = 0; i < text.length; i++) {
			list.add(text[i]);
		}
		return list;
	}

	// Offset

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		if (offset < 0) {
			this.offset = 0;
		} else if (offset > text.length) {
			this.offset = text.length;
		} else {
			this.offset = offset;
		}
	}

	// Edit

	public void printChar(char c) {
		LinkedList<Character> list = toList();
		list.add(offset, c);
		text = toArray(list);
		offset++;
	}

	public void delete() {
		if (offset >= getLenght()) {
			return;
		}
		LinkedList<Character> list = toList();
		list.remove(offset);
		text = toArray(list);
	}

	public void backspace() {
		if (offset == 0) {
			return;
		}
		LinkedList<Character> list = toList();
		list.remove(offset - 1);
		text = toArray(list);
		offset--;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length; i++) {
			sb.append(text[i]);
		}
		return sb.toString();
	}

}
