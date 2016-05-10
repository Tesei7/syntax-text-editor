package ru.tesei7.textEditor.editor.syntax;

import java.io.IOException;
import java.io.Reader;

public interface Tokenizer {
	void yyclose() throws IOException;

	void yyreset(Reader reader);

	int yystate();

	void yybegin(int newState);

	Token yylex() throws IOException;
}
