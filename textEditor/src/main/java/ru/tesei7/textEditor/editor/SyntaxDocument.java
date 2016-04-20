package ru.tesei7.textEditor.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class SyntaxDocument {

	private LinkedList<Line> lines = new LinkedList<>();
	private Line currentLine;

	public SyntaxDocument() {
		currentLine = new Line();
		lines.add(currentLine);
	}

	public LinkedList<Line> getLines() {
		return lines;
	}

	public Line getCurrentLine() {
		return currentLine;
	}
	
	public void addChar(char c) {
		currentLine.addChar(c);
	}

	public void backspaceChar() {
		currentLine.backspaceChar();
	}

}
