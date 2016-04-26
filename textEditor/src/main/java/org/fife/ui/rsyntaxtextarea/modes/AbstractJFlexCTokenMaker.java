/*
 * 01/25/2009
 *
 * AbstractJFlexCTokenMaker.java - Base class for token makers that use curly
 * braces to denote code blocks, such as C, C++, Java, Perl, etc.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.util.regex.Pattern;


/**
 * Base class for JFlex-based token makers using C-style syntax.  This class
 * knows how to:
 * 
 * <ul>
 *    <li>Auto-indent after opening braces and parens
 *    <li>Automatically close multi-line and documentation comments
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractJFlexCTokenMaker extends AbstractJFlexTokenMaker {

	private static final Pattern MLC_PATTERN =
			Pattern.compile("([ \\t]*)(/?[\\*]+)([ \\t]*)");


	/**
	 * Returns <code>true</code> always as C-style languages use curly braces
	 * to denote code blocks.
	 *
	 * @return <code>true</code> always.
	 */
	@Override
	public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return type==Token.IDENTIFIER || type==Token.FUNCTION;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getShouldIndentNextLineAfter(Token t) {
		if (t!=null && t.length()==1) {
			char ch = t.charAt(0);
			return ch=='{' || ch=='(';
		}
		return false;
	}


	/**
	 * Returns whether a given token is an internal token type that represents
	 * an MLC or documentation comment continuing on to the next line.  This is
	 * done by languages such as JavaScript that are a little more verbose
	 * than necessary so that their code can be copy-and-pasted into other
	 * <code>TokenMaker</code>s that use them as nested languages (such as
	 * HTML, JSP, etc.).
	 *
	 * @param t The token to check.  This cannot be <code>null</code>.
	 * @return Whether the token is an internal token representing the end of
	 *         a line for an MLC/doc comment continuing on to the next line.
	 */
	private boolean isInternalEolTokenForMLCs(Token t) {
		int type = t.getType();
		if (type<0) {
			type = getClosestStandardTokenTypeForInternalType(type);
			return type==TokenTypes.COMMENT_MULTILINE ||
					type==TokenTypes.COMMENT_DOCUMENTATION;
		}
		return false;	
	}

}