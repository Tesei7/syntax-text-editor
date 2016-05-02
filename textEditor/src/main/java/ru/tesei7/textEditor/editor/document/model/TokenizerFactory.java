package ru.tesei7.textEditor.editor.document.model;

import java.io.CharArrayReader;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.syntax.JavaScriptTokenizer;
import ru.tesei7.textEditor.editor.syntax.JavaTokenizer;
import ru.tesei7.textEditor.editor.syntax.Tokenizer;

/**
 * Factory for reinitialize tokenizers and make them flyweight
 * 
 * @author Ilya
 *
 */
public class TokenizerFactory {

	JavaTokenizer javaTokenizer;
	JavaScriptTokenizer javaScriptTokenizer;

	public TokenizerFactory() {
		javaTokenizer = new JavaTokenizer(new CharArrayReader(new char[0]));
		javaScriptTokenizer = new JavaScriptTokenizer(new CharArrayReader(new char[0]));
	}

	Tokenizer createTokenizer(Language language, char[] text, int state) {
		Tokenizer tokenizer = null;
		switch (language) {
		case JAVA:
			tokenizer = javaTokenizer;
			break;
		case JAVA_SCRIPT:
			tokenizer = javaScriptTokenizer;
			break;
		default:
			return null;

		}
		tokenizer.yyreset(new CharArrayReader(text));
		tokenizer.yybegin(state);
		return tokenizer;
	}
}
