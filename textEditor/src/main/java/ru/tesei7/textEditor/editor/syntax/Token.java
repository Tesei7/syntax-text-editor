package ru.tesei7.textEditor.editor.syntax;

public interface Token {
	public int getType();

	public int getOffset();
	
	public int getLength();
}
