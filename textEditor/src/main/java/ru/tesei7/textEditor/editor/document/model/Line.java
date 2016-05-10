package ru.tesei7.textEditor.editor.document.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.syntax.Token;

/**
 * Line of text document. Contains characters and calculated list of tokens.
 * 
 * @author Ilya Bochkarev
 *
 */
public class Line {
	/**
	 * Chars line consists of
	 */
	private char[] text = new char[0];
	/**
	 * Current caret position in line
	 */
	private int offset;
	/**
	 * Columns need to show this line (considering tab indents).
	 * lengthToPaint cached for performance reasons.
	 */
	private int lengthToPaint = 0;
	/**
	 * State of lexical analyzer at the end of this line
	 */
	private int lastTokenState = 0;
	/**
	 * Tokens, this line include
	 */
	private List<Token> tokens;

	public Line() {
		offset = 0;
	}

	char[] getText() {
		return text;
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
		setTextInner(text);
		offset = text.length;
	}

	private void setTextInner(char[] text) {
		this.text = text;
		recalculateLengthToPaint();
	}

	public int getLength() {
		return text.length;
	}

	/**
	 * 
	 * @return line length considering tab indents
	 */
	public int getLengthToPaint() {
		return lengthToPaint;
	}

	/**
	 * Recalculates {@link #lengthToPaint}. Should be run after each change of filed {@link #text}
	 */
	private void recalculateLengthToPaint() {
		lengthToPaint = getLengthTabReplaced(text);
	}

	private int getLengthTabReplaced(char[] chars) {
		int l = 0;
		for (char aChar : chars) {
			if (aChar == '\t') {
				l += SyntaxTextEditor.TAB_INDENT;
			} else {
				l++;
			}
		}
		return l;
	}

	public char[] getTextToPaint() {
		return getCharsTabReplaced(text);
	}

	public char[] getCharsTabReplaced(char[] chars) {
		char[] charsReplaced = new char[getLengthTabReplaced(chars)];
		int x = 0;
		for (char aChar : chars) {
			if (aChar == '\t') {
				charsReplaced[x++] = ' ';
				charsReplaced[x++] = ' ';
				charsReplaced[x++] = ' ';
				charsReplaced[x++] = ' ';
			} else if (aChar == '\r' || aChar == '\f' || aChar == '\b' || aChar == '\n') {
				charsReplaced[x++] = ' ';
			} else {
				charsReplaced[x++] = aChar;
			}
		}
		return charsReplaced;
	}

	char[] toArray(List<Character> list) {
		Character[] array = list.toArray(new Character[0]);
		return ArrayUtils.toPrimitive(array);
	}

	private List<Character> toList(char[] chars) {
		List<Character> list = new LinkedList<>();
		for (char aChar : chars) {
			list.add(aChar);
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

	public int getOffsetByOffsetToPaint(int offsetToPaint) {
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
		setTextInner(toArray(list));
		offset++;
	}

	public void printChars(char[] chars) {
		List<Character> textList = toList(text);
		List<Character> list = toList(chars);
		textList.addAll(offset, list);
		setTextInner(toArray(textList));
		offset += chars.length;
	}
	
	public void printChars(List<Character> list) {
		List<Character> textList = toList(text);
		textList.addAll(offset, list);
		setTextInner(toArray(textList));
		offset += list.size();
	}

	public void insertChar(char c) {
		if (atEndOfLine()) {
			printChar(c);
		} else {
			text[offset] = c;
			setTextInner(text);
			offset++;
		}
	}

	public void delete() {
		if (offset >= getLength()) {
			return;
		}
		List<Character> list = toList(text);
		list.remove(offset);
		setTextInner(toArray(list));
	}

	public void backspace() {
		if (offset == 0) {
			return;
		}
		List<Character> list = toList(text);
		list.remove(offset - 1);
		setTextInner(toArray(list));
		offset--;
	}

	// Tokens

	public int getLastTokenState() {
		return lastTokenState;
	}

	public void setLastTokenState(int lastTokenState) {
		this.lastTokenState = lastTokenState;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public Token getCurrentToken() {
		for (Token t : tokens) {
			if (t.getOffset() == offset) {
				return t;
			}
		}
		return null;
	}

	public Token getPreviousToken() {
		for (Token t : tokens) {
			if (t.getOffset() == offset - 1) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Get chars in token
	 * 
	 * @param t
	 *            token
	 * @return chars in token
	 */
	public char[] getChars(Token t) {
		char[] chars = new char[t.getLength()];
		System.arraycopy(text, t.getOffset(), chars, 0, t.getLength());
		return getCharsTabReplaced(chars);
	}

	// Other

	@Override
	public String toString() {
		return String.valueOf(text);
	}

}
