package ru.tesei7.textEditor.editor.document.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.fife.ui.rsyntaxtextarea.modes.Token;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;

/**
 * Line of text document. Contains characters and calculated list of tokens.
 * 
 * @author Ilya Bochkarev
 *
 */
public class Line {
	private char[] text = new char[0];
	private int offset;
	private LinkedList<Token> tokens = new LinkedList<>();

	public Line() {
		offset = 0;
	}

	char[] getText() {
		return text;
	}

	public LinkedList<Token> getTokens() {
		return tokens;
	}

	// Text

	public List<Character> getChars() {
		return toList(text);
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

	private List<Character> toList(char[] chars) {
		List<Character> list = new LinkedList<>();
		for (int i = 0; i < chars.length; i++) {
			list.add(chars[i]);
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
		if (offset > text.length) {
			offset = text.length;
		}
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
		List<Character> list = toList(text);
		list.add(offset, c);
		text = toArray(list);
		offset++;
	}

	public void printChars(char[] chars) {
		List<Character> textList = toList(text);
		List<Character> list = toList(chars);
		textList.addAll(offset, list);
		text = toArray(textList);
		offset += chars.length;
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
		List<Character> list = toList(text);
		list.remove(offset);
		text = toArray(list);
	}

	public void backspace() {
		if (offset == 0) {
			return;
		}
		List<Character> list = toList(text);
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
