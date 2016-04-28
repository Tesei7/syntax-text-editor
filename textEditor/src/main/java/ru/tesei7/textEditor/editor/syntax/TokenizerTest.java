package ru.tesei7.textEditor.editor.syntax;

import java.io.FileReader;
import java.io.IOException;

public class TokenizerTest {

	public static void main(String[] args) throws IOException {
		JavaTokenizer javaTokenizer = new JavaTokenizer(new FileReader(args[0]));
		JavaToken token = null;
		do {
			token = javaTokenizer.yylex();
			System.out.println(token.getType() + " - " + token.getText());
		} while (token != null && token.getType() != TokenTypes.EOF);
	}
}
