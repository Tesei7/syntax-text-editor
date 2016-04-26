/*
 * 02/21/2004
 *
 * Token.java - A token used in syntax highlighting.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.awt.Color;

import javax.swing.text.Segment;

/**
 * The default implementation of {@link Token}.
 * <p>
 *
 * <b>Note:</b> The instances of <code>Token</code> returned by
 * {@link RSyntaxDocument}s are pooled and should always be treated as
 * immutable. They should not be cast to <code>TokenImpl</code> and modified.
 * Modifying tokens you did not create yourself can and will result in rendering
 * issues and/or runtime exceptions. You have been warned!
 * 
 * @author Robert Futrell
 * @version 0.3
 */
public class TokenImpl implements Token {

	/**
	 * The text this token represents. This is implemented as a segment so we
	 * can point directly to the text in the document without having to make a
	 * copy of it.
	 */
	public char[] text;
	public int textOffset;
	public int textCount;

	/**
	 * The offset into the document at which this token resides.
	 */
	private int offset;

	/**
	 * The type of token this is; for example, {@link #FUNCTION}.
	 */
	private int type;

	/**
	 * Whether this token is a hyperlink.
	 */
	private boolean hyperlink;

	/**
	 * The next token in this linked list.
	 */
	private Token nextToken;

	/**
	 * The language this token is in, <code>&gt;= 0</code>.
	 */
	private int languageIndex;

	/**
	 * Creates a "null token." The token itself is not null; rather, it
	 * signifies that it is the last token in a linked list of tokens and that
	 * it is not part of a "multi-line token."
	 */
	public TokenImpl() {
		this.text = null;
		this.textOffset = -1;
		this.textCount = -1;
		this.setType(NULL);
		setOffset(-1);
		hyperlink = false;
		nextToken = null;
	}

	/**
	 * Constructor.
	 *
	 * @param line
	 *            The segment from which to get the token.
	 * @param beg
	 *            The first character's position in <code>line</code>.
	 * @param end
	 *            The last character's position in <code>line</code>.
	 * @param startOffset
	 *            The offset into the document at which this token begins.
	 * @param type
	 *            A token type listed as "generic" above.
	 * @param languageIndex
	 *            The language index for this token.
	 */
	public TokenImpl(Segment line, int beg, int end, int startOffset, int type, int languageIndex) {
		this(line.array, beg, end, startOffset, type, languageIndex);
	}

	/**
	 * Constructor.
	 *
	 * @param line
	 *            The segment from which to get the token.
	 * @param beg
	 *            The first character's position in <code>line</code>.
	 * @param end
	 *            The last character's position in <code>line</code>.
	 * @param startOffset
	 *            The offset into the document at which this token begins.
	 * @param type
	 *            A token type listed as "generic" above.
	 * @param languageIndex
	 *            The language index for this token.
	 */
	public TokenImpl(char[] line, int beg, int end, int startOffset, int type, int languageIndex) {
		this();
		set(line, beg, end, startOffset, type);
		setLanguageIndex(languageIndex);
	}

	/**
	 * Creates this token as a copy of the passed-in token.
	 *
	 * @param t2
	 *            The token from which to make a copy.
	 */
	public TokenImpl(Token t2) {
		this();
		copyFrom(t2);
	}

	public char charAt(int index) {
		return text[textOffset + index];
	}

	public boolean containsPosition(int pos) {
		return pos >= getOffset() && pos < getOffset() + textCount;
	}

	/**
	 * Makes one token point to the same text segment, and have the same value
	 * as another token.
	 *
	 * @param t2
	 *            The token from which to copy.
	 */
	public void copyFrom(Token t2) {
		text = t2.getTextArray();
		textOffset = t2.getTextOffset();
		textCount = t2.length();
		setOffset(t2.getOffset());
		setType(t2.getType());
		hyperlink = t2.isHyperlink();
		languageIndex = t2.getLanguageIndex();
		nextToken = t2.getNextToken();
	}

	public int documentToToken(int pos) {
		return pos + (textOffset - getOffset());
	}

	public boolean endsWith(char[] ch) {
		if (ch == null || ch.length > textCount) {
			return false;
		}
		final int start = textOffset + textCount - ch.length;
		for (int i = 0; i < ch.length; i++) {
			if (text[start + i] != ch[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Token)) {
			return false;
		}

		Token t2 = (Token) obj;
		return offset == t2.getOffset() && type == t2.getType() && languageIndex == t2.getLanguageIndex()
				&& hyperlink == t2.isHyperlink() && ((getLexeme() == null && t2.getLexeme() == null)
						|| (getLexeme() != null && getLexeme().equals(t2.getLexeme())));

	}

	public int getEndOffset() {
		return offset + textCount;
	}

	/**
	 * Returns a <code>String</code> of the form "#xxxxxx" good for use in HTML,
	 * representing the given color.
	 *
	 * @param color
	 *            The color to get a string for.
	 * @return The HTML form of the color. If <code>color</code> is
	 *         <code>null</code>, <code>#000000</code> is returned.
	 */
	private static final String getHTMLFormatForColor(Color color) {
		if (color == null) {
			return "black";
		}
		String hexRed = Integer.toHexString(color.getRed());
		if (hexRed.length() == 1)
			hexRed = "0" + hexRed;
		String hexGreen = Integer.toHexString(color.getGreen());
		if (hexGreen.length() == 1)
			hexGreen = "0" + hexGreen;
		String hexBlue = Integer.toHexString(color.getBlue());
		if (hexBlue.length() == 1)
			hexBlue = "0" + hexBlue;
		return "#" + hexRed + hexGreen + hexBlue;
	}

	public int getLanguageIndex() {
		return languageIndex;
	}

	public Token getLastNonCommentNonWhitespaceToken() {

		Token last = null;

		for (Token t = this; t != null && t.isPaintable(); t = t.getNextToken()) {
			switch (t.getType()) {
			case COMMENT_DOCUMENTATION:
			case COMMENT_EOL:
			case COMMENT_MULTILINE:
			case COMMENT_KEYWORD:
			case COMMENT_MARKUP:
			case WHITESPACE:
				break;
			default:
				last = t;
				break;
			}
		}

		return last;

	}

	public Token getLastPaintableToken() {
		Token t = this;
		while (t.isPaintable()) {
			Token next = t.getNextToken();
			if (next == null || !next.isPaintable()) {
				return t;
			}
			t = next;
		}
		return null;
	}

	public String getLexeme() {
		return text == null ? null : new String(text, textOffset, textCount);
	}

	public Token getNextToken() {
		return nextToken;
	}

	public int getOffset() {
		return offset;
	}

	public char[] getTextArray() {
		return text;
	}

	public int getTextOffset() {
		return textOffset;
	}

	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return offset + (getLexeme() == null ? 0 : getLexeme().hashCode());
	}

	public boolean is(char[] lexeme) {
		if (textCount == lexeme.length) {
			for (int i = 0; i < textCount; i++) {
				if (text[textOffset + i] != lexeme[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean is(int type, char[] lexeme) {
		if (this.getType() == type && textCount == lexeme.length) {
			for (int i = 0; i < textCount; i++) {
				if (text[textOffset + i] != lexeme[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean is(int type, String lexeme) {
		return this.getType() == type && textCount == lexeme.length() && lexeme.equals(getLexeme());
	}

	public boolean isComment() {
		return getType() >= Token.COMMENT_EOL && getType() <= Token.COMMENT_MARKUP;
	}

	public boolean isCommentOrWhitespace() {
		return isComment() || isWhitespace();
	}

	public boolean isHyperlink() {
		return hyperlink;
	}

	public boolean isIdentifier() {
		return getType() == IDENTIFIER;
	}

	public boolean isLeftCurly() {
		return getType() == SEPARATOR && isSingleChar('{');
	}

	public boolean isRightCurly() {
		return getType() == SEPARATOR && isSingleChar('}');
	}

	public boolean isPaintable() {
		return getType() > Token.NULL;
	}

	public boolean isSingleChar(char ch) {
		return textCount == 1 && text[textOffset] == ch;
	}

	public boolean isSingleChar(int type, char ch) {
		return this.getType() == type && isSingleChar(ch);
	}

	public boolean isWhitespace() {
		return getType() == WHITESPACE;
	}

	public int length() {
		return textCount;
	}

	/**
	 * Makes this token start at the specified offset into the document.
	 * <p>
	 * 
	 * <b>Note:</b> You should not modify <code>Token</code> instances you did
	 * not create yourself (e.g., came from an <code>RSyntaxDocument</code>). If
	 * you do, rendering issues and/or runtime exceptions will likely occur. You
	 * have been warned!
	 *
	 * @param pos
	 *            The offset into the document this token should start at. Note
	 *            that this token must already contain this position; if it
	 *            doesn't, an exception is thrown.
	 * @throws IllegalArgumentException
	 *             If pos is not already contained by this token.
	 * @see #moveOffset(int)
	 */
	public void makeStartAt(int pos) {
		if (pos < getOffset() || pos >= (getOffset() + textCount)) {
			throw new IllegalArgumentException(
					"pos " + pos + " is not in range " + getOffset() + "-" + (getOffset() + textCount - 1));
		}
		int shift = pos - getOffset();
		setOffset(pos);
		textOffset += shift;
		textCount -= shift;
	}

	/**
	 * Moves the starting offset of this token.
	 * <p>
	 * 
	 * <b>Note:</b> You should not modify <code>Token</code> instances you did
	 * not create yourself (e.g., came from an <code>RSyntaxDocument</code>). If
	 * you do, rendering issues and/or runtime exceptions will likely occur. You
	 * have been warned!
	 *
	 * @param amt
	 *            The amount to move the starting offset. This should be between
	 *            <code>0</code> and <code>textCount</code>, inclusive.
	 * @throws IllegalArgumentException
	 *             If <code>amt</code> is an invalid value.
	 * @see #makeStartAt(int)
	 */
	public void moveOffset(int amt) {
		if (amt < 0 || amt > textCount) {
			throw new IllegalArgumentException("amt " + amt + " is not in range 0-" + textCount);
		}
		setOffset(getOffset() + amt);
		textOffset += amt;
		textCount -= amt;
	}

	/**
	 * Sets the value of this token to a particular segment of a document. The
	 * "next token" value is cleared.
	 *
	 * @param line
	 *            The segment from which to get the token.
	 * @param beg
	 *            The first character's position in <code>line</code>.
	 * @param end
	 *            The last character's position in <code>line</code>.
	 * @param offset
	 *            The offset into the document at which this token begins.
	 * @param type
	 *            A token type listed as "generic" above.
	 */
	public void set(final char[] line, final int beg, final int end, final int offset, final int type) {
		this.text = line;
		this.textOffset = beg;
		this.textCount = end - beg + 1;
		this.setType(type);
		this.setOffset(offset);
		nextToken = null;
	}

	/**
	 * Sets whether this token is a hyperlink.
	 *
	 * @param hyperlink
	 *            Whether this token is a hyperlink.
	 * @see #isHyperlink()
	 */
	public void setHyperlink(boolean hyperlink) {
		this.hyperlink = hyperlink;
	}

	/**
	 * Sets the language index for this token. If this value is positive, it
	 * denotes a specific "secondary" language this token represents (such as
	 * JavaScript code or CSS embedded in an HTML file). If this value is
	 * <code>0</code>, this token is in the "main" language being edited.
	 * Negative values are invalid and treated as <code>0</code>.
	 *
	 * @param languageIndex
	 *            The new language index. A value of <code>0</code> denotes the
	 *            "main" language, any positive value denotes a specific
	 *            secondary language. Negative values will be treated as
	 *            <code>0</code>.
	 * @see #getLanguageIndex()
	 */
	public void setLanguageIndex(int languageIndex) {
		this.languageIndex = languageIndex;
	}

	/**
	 * Sets the "next token" pointer of this token to point to the specified
	 * token.
	 *
	 * @param nextToken
	 *            The new next token.
	 * @see #getNextToken()
	 */
	public void setNextToken(Token nextToken) {
		this.nextToken = nextToken;
	}

	/**
	 * Sets the offset into the document at which this token resides.
	 *
	 * @param offset
	 *            The new offset into the document.
	 * @see #getOffset()
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean startsWith(char[] chars) {
		if (chars.length <= textCount) {
			for (int i = 0; i < chars.length; i++) {
				if (text[textOffset + i] != chars[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int tokenToDocument(int pos) {
		return pos + (getOffset() - textOffset);
	}

	/**
	 * Returns this token as a <code>String</code>, which is useful for
	 * debugging.
	 *
	 * @return A string describing this token.
	 */
	@Override
	public String toString() {
		return "[Token: "
				+ (getType() == Token.NULL ? "<null token>"
						: "text: '" + (text == null ? "<null>"
								: getLexeme() + "'; " + "offset: " + getOffset() + "; type: " + getType() + "; "
										+ "isPaintable: " + isPaintable() + "; nextToken==null: "
										+ (nextToken == null)))
				+ "]";
	}

}