package ru.tesei7.textEditor.editor.syntax;

import java.io.IOException;
import java.io.Reader;

public interface Tokenizer {
	public void yyclose() throws IOException;

	public void yyreset(Reader reader);

	public int yystate();

	public void yybegin(int newState);

	public Token yylex() throws IOException;
}
