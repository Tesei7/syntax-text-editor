package ru.tesei7.textEditor.editor.document.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.sun.xml.internal.txw2.Document;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;

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

	char[] getText() {
		return text;
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

	public int getLength() {
		return text.length;
	}

	/**
	 * 
	 * @return line length considering tab indents
	 */
	public int getLengthToPaint() {
		int l = 0;
		for (int i = 0; i < text.length; i++) {
			if (text[i] == '\t') {
				l += SyntaxTextEditor.TAB_INDENT;
			} else {
				l++;
			}
		}
		return l;
	}

	char[] toArray(List<Character> list) {
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

	/**
	 * 
	 * @return offset considering tab indents
	 */
	public int getOffsetToPaint() {
		return getOffsetToPaint(offset);
	}
	
	public int getOffsetToPaint(int offset) {
		int x = 0;
		for (int i = 0; i < offset; i++) {
			if (text[i] == '\t') {
				x += SyntaxTextEditor.TAB_INDENT;
			} else {
				x++;
			}
		}
		return x;
	}

	public int getOffestByOffsetToPaint(int offsetToPaint) {
		int i = 0;
		while (offsetToPaint > 0 && i < text.length) {
			offsetToPaint -= text[i] == '\t' ? SyntaxTextEditor.TAB_INDENT : 1;
			if (offsetToPaint >= 0) {
				i++;
			}
		}
		return i;
	}

	public boolean atEndOfLine() {
		return offset == getLength();
	}

	// Edit

	public void printChar(char c) {
		LinkedList<Character> list = toList();
		list.add(offset, c);
		text = toArray(list);
		offset++;
	}

	public void insertChar(char c) {
		if (atEndOfLine()) {
			printChar(c);
		} else {
			text[offset] = c;
			offset++;
		}
	}

	public void delete() {
		if (offset >= getLength()) {
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

	// Other

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length; i++) {
			sb.append(text[i]);
		}
		return sb.toString();
	}

}
