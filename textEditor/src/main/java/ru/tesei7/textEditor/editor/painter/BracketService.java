package ru.tesei7.textEditor.editor.painter;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;

public class BracketService {

	private SyntaxDocument document;

	public BracketService(SyntaxDocument document) {
		this.document = document;
	}

	/**
	 * 
	 * @return -1 - no bracket at current position, 0 - bracket at current
	 *         position, 1 - bracket before current position
	 */
	public int getBracketAtCurrentPosition() {
		Line l = document.getCurrentLine();
		Token cur = l.getCurrentToken();
		Token prev = l.getPreviousToken();
		if (isBracket(cur)) {
			return 0;
		} else if (isBracket(prev)) {
			return 1;
		} else
			return -1;
	}

	boolean isBracket(Token t) {
		if (t == null) {
			return false;
		}
		int type = t.getType();
		return type == TokenTypes.L_BRACKET || type == TokenTypes.R_BRACKET || type == TokenTypes.L_BRACE
				|| type == TokenTypes.R_BRACE || type == TokenTypes.L_PARENTHESIS || type == TokenTypes.R_PARENTHESIS;
	}

	public int[] getRelativeBracket() {
		return new BracketFinder(document, this).find();
	}

}
