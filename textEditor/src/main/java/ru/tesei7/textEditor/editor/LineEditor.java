package ru.tesei7.textEditor.editor;

import ru.tesei7.textEditor.editor.document.Line;

public class LineEditor {

	private Line line;

	public void setLine(Line line) {
		this.line = line;
	}

	public void printChar(char c) {
		line.getText().add(line.getOffset(), c);
		line.setOffset(line.getOffset() + 1);
	}

	public void delete() {
		line.getText().remove(line.getOffset());
	}

	public void backspace() {
		line.getText().remove(line.getOffset() - 1);
		line.setOffset(line.getOffset() - 1);
	}

}
