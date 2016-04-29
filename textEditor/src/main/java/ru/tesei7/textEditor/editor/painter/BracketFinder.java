package ru.tesei7.textEditor.editor.painter;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;

public class BracketFinder {

	private SyntaxDocument document;
	private BracketService bracketService;
	private Token currentBracketToken;
	private int direction;
	private int reletiveBracketType;
	private Token reletiveBracketToken;
	private int reletiveBracketLine;

	public BracketFinder(SyntaxDocument document, BracketService bracketService) {
		this.document = document;
		this.bracketService = bracketService;
	}

	public int[] find() {
		try {
			return findCurrentBracket().setReletiveBracketType().setSearchDirection().findReletiveBracket()
					.getReletiveBracketCoordinates();
		} catch (Exception e) {
			return null;
		}
	}

	private BracketFinder findCurrentBracket() throws Exception {
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

	private BracketFinder setReletiveBracketType() throws Exception {
		switch (currentBracketToken.getType()) {
		case TokenTypes.LBRACK:
			reletiveBracketType = TokenTypes.RBRACK;
			return this;
		case TokenTypes.RBRACK:
			reletiveBracketType = TokenTypes.LBRACK;
			return this;
		case TokenTypes.LBRACE:
			reletiveBracketType = TokenTypes.RBRACE;
			return this;
		case TokenTypes.RBRACE:
			reletiveBracketType = TokenTypes.LBRACE;
			return this;
		case TokenTypes.LPAREN:
			reletiveBracketType = TokenTypes.RPAREN;
			return this;
		case TokenTypes.RPAREN:
			reletiveBracketType = TokenTypes.LPAREN;
			return this;
		}
		throw new Exception();
	}

	private BracketFinder setSearchDirection() throws Exception {
		switch (currentBracketToken.getType()) {
		case TokenTypes.LBRACK:
		case TokenTypes.LBRACE:
		case TokenTypes.LPAREN:
			direction = 1;
			return this;
		case TokenTypes.RBRACK:
		case TokenTypes.RBRACE:
		case TokenTypes.RPAREN:
			direction = -1;
			return this;
		}
		throw new Exception();
	}

	private BracketFinder findReletiveBracket() throws Exception {
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
				} else if (t.getType() == reletiveBracketType) {
					openBracketsCount--;
				}
				if (openBracketsCount == 0) {
					reletiveBracketToken = t;
					reletiveBracketLine = i;
					return this;
				}
			}
		}
		throw new Exception();
	}

	private int[] getReletiveBracketCoordinates() {
		int offset = reletiveBracketToken.getOffset();
		int offsetToPaint = document.getXToPaint(document.getLineByIndex(reletiveBracketLine), offset);
		int rowToPaint = reletiveBracketLine - document.getFirstVisibleRow();
		return new int[] { rowToPaint, offsetToPaint };
	}

}
