package ru.tesei7.textEditor.editor.document.model;

import java.io.IOException;
import java.util.List;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.syntax.JavaTokenizer;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;
import ru.tesei7.textEditor.editor.syntax.Tokenizer;

public class LexicalAnalyzer {
	/**
	 * Creates tokenizer
	 */
	TokenizerFactory tokenizerFactory;

	public LexicalAnalyzer() {
		tokenizerFactory = new TokenizerFactory();
	}

	/**
	 * Read tokens from line and fill tokens list
	 * 
	 * @param tokens
	 *            list of tokens to fill
	 * @param l
	 *            line to read
	 * @param language
	 *            language of line
	 * @param state
	 *            initial state of line
	 * @return last state of line
	 */
	public int readTokensFromLine(final List<Token> tokens, Line l, Language language, int state) {
		Tokenizer tokenizer = tokenizerFactory.createTokenizer(language, l.getText(), state);
		return readTokens(tokens, tokenizer);
	}

	/**
	 * Read tokens with tokenizer and fill list of tokens
	 * 
	 * @param tokens
	 *            list of tokens to fill
	 * @param tokenizer
	 *            tokenizer to read tokens
	 * @param state
	 *            initial state of tokenizer
	 * @return last state of tokenizer
	 */
	int readTokens(final List<Token> tokens, Tokenizer tokenizer) {
		int state = 0;
		Token token = null;
		do {
			try {
				token = tokenizer.yylex();
				state = tokenizer.yystate();
				if (token != null && token.getType() != TokenTypes.EOF) {
					tokens.add(token);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return state;
			}
		} while (token != null && token.getType() != TokenTypes.EOF);

		// strings are not multiline tokens
		if (state == JavaTokenizer.STRING || state == JavaTokenizer.CHARLITERAL) {
			state = JavaTokenizer.YYINITIAL;
		}
		return state;
	}

}
