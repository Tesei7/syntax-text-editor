package ru.tesei7.textEditor.editor.document.model;

import java.io.CharArrayReader;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.syntax.JavaTokenizer;
import ru.tesei7.textEditor.editor.syntax.Tokenizer;

/**
 * Factory for reinitialize tokenizers and make them flyweight
 * 
 * @author Ilya
 *
 */
public class TokenizerFactory {

	private JavaTokenizer javaTokenizer;

	public TokenizerFactory() {
		javaTokenizer = new JavaTokenizer(new CharArrayReader(new char[0]));
	}

	Tokenizer createTokenizer(Language language, char[] text, int state) {
		switch (language) {
		case JAVA:
			javaTokenizer.yyreset(new CharArrayReader(text));
			javaTokenizer.yybegin(state);
			return javaTokenizer;
		case JAVA_SCRIPT:
			// TODO JS support
			return null;
		default:
			return null;
		}
	}
}
