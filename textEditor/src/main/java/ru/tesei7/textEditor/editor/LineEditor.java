package ru.tesei7.textEditor.editor;

import ru.tesei7.textEditor.editor.document.Line;

public class LineEditor {

	public void printChar(char c, Line line) {
		line.getText().add(line.getOffset(), c);
		line.setOffset(line.getOffset() + 1);
	}

	public void delete(Line line) {
		if (line.getOffset() >= line.getLenght()) {
			return;
		}
		line.getText().remove(line.getOffset());
	}

	public void backspace(Line line) {
		if (line.getOffset()==0){
			return;
		}
		line.getText().remove(line.getOffset() - 1);
		line.setOffset(line.getOffset() - 1);
	}

}
