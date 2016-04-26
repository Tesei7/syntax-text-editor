/*
 * 02/21/2004
 *
 * Token.java - A token used in syntax highlighting.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

/**
 * A generic token that functions as a node in a linked list of syntax
 * highlighted tokens for some language.<p>
 *
 * A <code>Token</code> is a piece of text representing some logical token in
 * source code for a programming language.  For example, the line of C code:<p>
 * <pre>
 * int i = 0;
 * </pre>
 * would be broken into 8 <code>Token</code>s: the first representing
 * <code>int</code>, the second whitespace, the third <code>i</code>, the fourth
 * whitespace, the fifth <code>=</code>, etc.<p>
 *
 * <b>Note:</b> The tokens returned by {@link RSyntaxDocument}s are pooled and
 * should always be treated as immutable.  Modifying tokens you did not create
 * yourself can and will result in rendering issues and/or runtime exceptions.
 * You have been warned!
 * 
 * @author Robert Futrell
 * @version 0.3
 */
public interface Token extends TokenTypes {

	/**
	 * Returns the character at the specified offset in the token.
	 *
	 * @param index The index.  This should be in the range
	 *        <code>0-({@link #length()}-1)</code>.
	 * @return The character.
	 * @see #length()
	 */
	public char charAt(int index);


	/**
	 * Returns whether the token straddles the specified position in the
	 * document.
	 *
	 * @param pos The position in the document to check.
	 * @return Whether the specified position is straddled by this token.
	 */
	public boolean containsPosition(int pos);


	/**
	 * Returns the position in the token's internal char array corresponding
	 * to the specified document position.<p>
	 * Note that this method does NOT do any bounds checking; you can pass in
	 * a document position that does not correspond to a position in this
	 * token, and you will not receive an Exception or any other notification;
	 * it is up to the caller to ensure valid input.
	 *
	 * @param pos A position in the document that is represented by this token.
	 * @return The corresponding token position &gt;= <code>textOffset</code>
	 *         and &lt; <code>textOffset+textCount</code>.
	 * @see #tokenToDocument
	 */
	public int documentToToken(int pos);


	/**
	 * Returns whether this token's lexeme ends with the specified characters.
	 *
	 * @param ch The characters.
	 * @return Whether this token's lexeme ends with the specified characters.
	 * @see #startsWith(char[])
	 */
	public boolean endsWith(char[] ch);


	/**
	 * Returns the end offset of this token in the document (exclusive).  In
	 * other words, the token ranges from
	 * <code>[getOffset(), getEndOffset())</code>.
	 * 
	 * @return The end offset of this token.
	 * @see #getOffset()
	 */
	public int getEndOffset();


	/**
	 * Returns the language index of this token.
	 *
	 * @return The language index.  A value of <code>0</code> denotes the
	 *        "main" language, any positive value denotes a specific secondary
	 *        language.
	 * @see #setLanguageIndex(int)
	 */
	public int getLanguageIndex();


	/**
	 * Returns the last token in this list that is not whitespace or a
	 * comment.
	 *
	 * @return The last non-comment, non-whitespace token, or <code>null</code>
	 *         if there isn't one.
	 */
	public Token getLastNonCommentNonWhitespaceToken();


	/**
	 * Returns the last paintable token in this token list, or <code>null</code>
	 * if there is no paintable token.
	 *
	 * @return The last paintable token in this token list.
	 */
	public Token getLastPaintableToken();


	/**
	 * Returns the text of this token, as a string.<p>
	 *
	 * Note that this method isn't used much by the
	 * <code>ryntaxtextarea</code> package internally, as it tries to limit
	 * memory allocation.
	 *
	 * @return The text of this token.
	 */
	public String getLexeme();

	/**
	 * Returns the token after this one in the linked list.
	 *
	 * @return The next token.
	 */
	public Token getNextToken();


	/**
	 * Returns the offset into the document at which this token resides.
	 *
	 * @return The offset into the document.
	 * @see #getEndOffset()
	 */
	public int getOffset();

	/**
	 * Returns the character array backing the lexeme of this token.  This
	 * value should be treated as read-only.
	 *
	 * @return A character array containing the lexeme of this token.
	 * @see #getTextOffset()
	 * @see #length()
	 */
	public char[] getTextArray();


	/**
	 * Returns the offset into the character array of the lexeme in
	 * {@link #getTextArray()}.
	 *
	 * @return The offset of the lexeme in the character array.
	 * @see #getTextArray()
	 */
	public int getTextOffset();


	/**
	 * Returns the type of this token.
	 *
	 * @return The type of this token.
	 * @see TokenTypes
	 * @see #setType(int)
	 */
	public int getType();


	/**
	 * Returns whether this token's lexeme matches a specific character array.
	 *
	 * @param lexeme The lexeme to check for.
	 * @return Whether this token has that lexeme.
	 * @see #is(int, char[])
	 * @see #is(int, String)
	 * @see #isSingleChar(int, char)
	 * @see #startsWith(char[])
	 */
	public boolean is(char[] lexeme);


	/**
	 * Returns whether this token is of the specified type, with the specified
	 * lexeme.<p>
	 * This method is preferred over the other overload in performance-critical
	 * code where this operation may be called frequently, since it does not
	 * involve any String allocations.
	 *
	 * @param type The type to check for.
	 * @param lexeme The lexeme to check for.
	 * @return Whether this token has that type and lexeme.
	 * @see #is(int, String)
	 * @see #is(char[])
	 * @see #isSingleChar(int, char)
	 * @see #startsWith(char[])
	 */
	public boolean is(int type, char[] lexeme);


	/**
	 * Returns whether this token is of the specified type, with the specified
	 * lexeme.<p>
	 * The other overload of this method is preferred over this one in
	 * performance-critical code, as this one involves a String allocation
	 * while the other does not.
	 *
	 * @param type The type to check for.
	 * @param lexeme The lexeme to check for.
	 * @return Whether this token has that type and lexeme.
	 * @see #is(int, char[])
	 * @see #isSingleChar(int, char)
	 * @see #startsWith(char[])
	 */
	public boolean is(int type, String lexeme);


	/**
	 * Returns whether this token is a comment.
	 *
	 * @return Whether this token is a comment.
	 * @see #isWhitespace()
	 * @see #isCommentOrWhitespace()
	 */
	public boolean isComment();


	/**
	 * Returns whether this token is a comment or whitespace.
	 *
	 * @return Whether this token is a comment or whitespace.
	 * @see #isComment()
	 * @see #isWhitespace()
	 */
	public boolean isCommentOrWhitespace();


	/**
	 * Returns whether this token is a hyperlink.
	 *
	 * @return Whether this token is a hyperlink.
	 * @see #setHyperlink(boolean)
	 */
	public boolean isHyperlink();


	/**
	 * Returns whether this token is an identifier.
	 *
	 * @return Whether this token is an identifier.
	 */
	public boolean isIdentifier();


	/**
	 * Returns whether this token is a {@link #SEPARATOR} representing a single
	 * left curly brace.
	 *
	 * @return Whether this token is a left curly brace.
	 * @see #isRightCurly()
	 */
	public boolean isLeftCurly();


	/**
	 * Returns whether this token is a {@link #SEPARATOR} representing a single
	 * right curly brace.
	 *
	 * @return Whether this token is a right curly brace.
	 * @see #isLeftCurly()
	 */
	public boolean isRightCurly();


	/**
	 * Returns whether or not this token is "paintable;" i.e., whether or not
	 * the type of this token is one such that it has an associated syntax
	 * style.  What this boils down to is whether the token type is greater
	 * than <code>Token.NULL</code>.
	 *
	 * @return Whether or not this token is paintable.
	 */
	public boolean isPaintable();


	/**
	 * Returns whether this token is the specified single character.
	 *
	 * @param ch The character to check for.
	 * @return Whether this token's lexeme is the single character specified.
	 * @see #isSingleChar(int, char)
	 */
	public boolean isSingleChar(char ch);


	/**
	 * Returns whether this token is the specified single character, and of a
	 * specific type.
	 *
	 * @param type The token type.
	 * @param ch The character to check for.
	 * @return Whether this token is of the specified type, and with a lexeme
	 *         Equaling the single character specified.
	 * @see #isSingleChar(char)
	 */
	public boolean isSingleChar(int type, char ch);


	/**
	 * Returns whether or not this token is whitespace.
	 *
	 * @return <code>true</code> iff this token is whitespace.
	 * @see #isComment()
	 * @see #isCommentOrWhitespace()
	 */
	public boolean isWhitespace();


	/**
	 * Returns the length of this token.
	 *
	 * @return The length of this token.
	 * @see #getOffset()
	 */
	public int length();

	/**
	 * Sets whether this token is a hyperlink.
	 *
	 * @param hyperlink Whether this token is a hyperlink.
	 * @see #isHyperlink()
	 */
	public void setHyperlink(boolean hyperlink);


	/**
	 * Sets the language index for this token.  If this value is positive, it
	 * denotes a specific "secondary" language this token represents (such as
	 * JavaScript code or CSS embedded in an HTML file).  If this value is
	 * <code>0</code>, this token is in the "main" language being edited.
	 * Negative values are invalid and treated as <code>0</code>.
	 *
	 * @param languageIndex The new language index.  A value of
	 *        <code>0</code> denotes the "main" language, any positive value
	 *        denotes a specific secondary language.  Negative values will
	 *        be treated as <code>0</code>.
	 * @see #getLanguageIndex()
	 */
	public void setLanguageIndex(int languageIndex);


	/**
	 * Sets the type of this token.
	 *
	 * @param type The new token type.
	 * @see TokenTypes
	 * @see #getType()
	 */
	public void setType(int type);


	/**
	 * Returns whether this token starts with the specified characters.
	 *
	 * @param chars The characters.
	 * @return Whether this token starts with those characters.
	 * @see #endsWith(char[])
	 * @see #is(int, char[])
	 */
	public boolean startsWith(char[] chars);


	/**
	 * Returns the position in the document corresponding to the specified
	 * position in this token's internal char array (<code>textOffset</code> -
	 * <code>textOffset+textCount-1</code>).<p>
	 * Note that this method does NOT do any bounds checking; you can pass in
	 * an invalid token position, and you will not receive an Exception or any
	 * other indication that the returned document position is invalid.  It is
	 * up to the user to ensure valid input.
	 *
	 * @param pos A position in the token's internal char array
	 *        (<code>textOffset</code> - <code>textOffset+textCount</code>).
	 * @return The corresponding position in the document.
	 * @see #documentToToken(int)
	 */
	public int tokenToDocument(int pos);


}