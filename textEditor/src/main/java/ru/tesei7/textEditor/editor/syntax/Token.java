package ru.tesei7.textEditor.editor.syntax;

public interface Token {
	public int getType();

	public String getText();

	public int getOffset();
}
