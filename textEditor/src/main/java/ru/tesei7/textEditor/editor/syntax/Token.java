package ru.tesei7.textEditor.editor.syntax;

public interface Token {
	int getType();

	int getOffset();
	
	int getLength();
}
