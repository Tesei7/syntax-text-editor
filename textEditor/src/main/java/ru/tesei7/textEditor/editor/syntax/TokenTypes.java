package ru.tesei7.textEditor.editor.syntax;

public interface TokenTypes {
	int INITIAL = 0;
	
	int KEYWORD = 1;
	int BOOLEAN_LITERAL = 2;
	int L_PARENTHESIS = 3;
	int R_PARENTHESIS = 4;
	int L_BRACE = 5;
	int R_BRACE = 6;
	int L_BRACKET = 7;
	int R_BRACKET = 8;
	int SEMICOLON = 9;
	int COMMA = 10;
	int DOT = 11;
	int OPERATOR = 12;
	int STRING_LITERAL = 13;
	int CHARACTER_LITERAL = 14;
	int INTEGER_LITERAL = 15;
	int FLOATING_POINT_LITERAL = 16;
	int COMMENT_EOL = 17;
	int COMMENT_MULTI = 18;
	int IDENTIFIER = 19;
	int EOF = 20;
	int WHITESPACE = 21;
	int OTHER = 22;
	int ANNOTATION = 23;
}
