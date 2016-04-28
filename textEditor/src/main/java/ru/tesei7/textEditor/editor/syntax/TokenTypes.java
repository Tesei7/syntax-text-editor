package ru.tesei7.textEditor.editor.syntax;

public interface TokenTypes {
	int INITIAL = 0;
	
	int KEYWORD = 1;
	int BOOLEAN_LITERAL = 2;
	int LPAREN = 4;
	int RPAREN = 5;
	int LBRACE = 6;
	int RBRACE = 7;
	int LBRACK = 8;
	int RBRACK = 9;
	int SEMICOLON = 10;
	int COMMA = 11;
	int DOT = 12;
	int OPERATOR = 13;
	int STRING_LITERAL = 14;
	int CHARACTER_LITERAL = 15;
	int INTEGER_LITERAL = 16;
	int FLOATING_POINT_LITERAL = 17;
	int COMMENT_EOL = 18;
	int COMMENT_MULTI = 23;
	int IDENTIFIER = 19;
	int EOF = 20;
	int WHITESPACE = 22;
}
