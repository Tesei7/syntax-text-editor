package ru.tesei7.textEditor.editor.document.model;

import java.io.IOException;
import java.util.List;

import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;
import ru.tesei7.textEditor.editor.syntax.Tokenizer;

public class TokenCalculator {
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
		return state;
	}
}
