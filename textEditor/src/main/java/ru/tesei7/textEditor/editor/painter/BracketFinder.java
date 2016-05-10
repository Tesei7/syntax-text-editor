package ru.tesei7.textEditor.editor.painter;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;

public class BracketFinder {

	private SyntaxDocument document;
	private BracketService bracketService;
	Token currentBracketToken;
	int direction;
	int relativeBracketType;
	Token relativeBracketToken;
	int relativeBracketLine;

	public BracketFinder(SyntaxDocument document, BracketService bracketService) {
		this.document = document;
		this.bracketService = bracketService;
	}

	public int[] find() {
		try {
			return findCurrentBracket().setRelativeBracketType().setSearchDirection().findRelativeBracket()
					.getRelativeBracketCoordinates();
		} catch (Exception e) {
			return null;
		}
	}

	BracketFinder findCurrentBracket() throws Exception {
		Line l = document.getCurrentLine();
		currentBracketToken = l.getCurrentToken();
		if (!bracketService.isBracket(currentBracketToken)) {
			currentBracketToken = l.getPreviousToken();
			if (!bracketService.isBracket(currentBracketToken)) {
				throw new Exception();
			}
		}
		return this;
	}

	BracketFinder setRelativeBracketType() throws Exception {
		switch (currentBracketToken.getType()) {
		case TokenTypes.L_BRACKET:
			relativeBracketType = TokenTypes.R_BRACKET;
			return this;
		case TokenTypes.R_BRACKET:
			relativeBracketType = TokenTypes.L_BRACKET;
			return this;
		case TokenTypes.L_BRACE:
			relativeBracketType = TokenTypes.R_BRACE;
			return this;
		case TokenTypes.R_BRACE:
			relativeBracketType = TokenTypes.L_BRACE;
			return this;
		case TokenTypes.L_PARENTHESIS:
			relativeBracketType = TokenTypes.R_PARENTHESIS;
			return this;
		case TokenTypes.R_PARENTHESIS:
			relativeBracketType = TokenTypes.L_PARENTHESIS;
			return this;
		}
		throw new Exception();
	}

	BracketFinder setSearchDirection() throws Exception {
		switch (currentBracketToken.getType()) {
		case TokenTypes.L_BRACKET:
		case TokenTypes.L_BRACE:
		case TokenTypes.L_PARENTHESIS:
			direction = 1;
			return this;
		case TokenTypes.R_BRACKET:
		case TokenTypes.R_BRACE:
		case TokenTypes.R_PARENTHESIS:
			direction = -1;
			return this;
		}
		throw new Exception();
	}

	BracketFinder findRelativeBracket() throws Exception {
		int currTokenType = currentBracketToken.getType();
		int openBracketsCount = 1;
		for (int i = document.getCurLineIndex(); direction > 0 ? i < document.getSize() : i >= 0; i = i + direction) {
			Line l = document.getLineByIndex(i);
			int fromTokenIndex = i == document.getCurLineIndex()
					? l.getTokens().indexOf(currentBracketToken) + direction
					: direction > 0 ? 0 : l.getTokens().size() - 1;
			for (int j = fromTokenIndex; direction > 0 ? j < l.getTokens().size() : j >= 0; j = j + direction) {
				Token t = l.getTokens().get(j);
				if (t.getType() == currTokenType) {
					openBracketsCount++;
				} else if (t.getType() == relativeBracketType) {
					openBracketsCount--;
				}
				if (openBracketsCount == 0) {
					relativeBracketToken = t;
					relativeBracketLine = i;
					return this;
				}
			}
		}
		throw new Exception();
	}

	int[] getRelativeBracketCoordinates() {
		int offset = relativeBracketToken.getOffset();
		int offsetToPaint = document.getXToPaint(document.getLineByIndex(relativeBracketLine), offset);
		int rowToPaint = relativeBracketLine - document.getFirstVisibleRow();
		return new int[] { rowToPaint, offsetToPaint };
	}

}
