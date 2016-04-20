package ru.tesei7.textEditor.editor.caret;

import javax.annotation.PostConstruct;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxCaret {

	private CaretType type;
	public SyntaxDocument document;

	@PostConstruct
	public void init() {
		type = CaretType.NORMAL;
	}

	public void setDocument(SyntaxDocument document) {
		this.document = document;
	}

	public CaretType getType() {
		return type;
	}

	public void setType(CaretType type) {
		this.type = type;
	}

	public int getX() {
		return document.getCurrentLine().getOffset();
	}

	public int getY() {
		return document.getCurrentLineRow();
	}

	public void left() {
		Line currentLine = document.getCurrentLine();
		int offset = currentLine.getOffset();
		if (offset > 0) {
			currentLine.setOffset(offset - 1);
		}
	}

	public void right() {
		Line currentLine = document.getCurrentLine();
		int offset = currentLine.getOffset();
		if (offset < currentLine.getLenght()) {
			currentLine.setOffset(offset + 1);
		}
	}

	public void up() {
		Line currentLine = document.getCurrentLine();
		if (!currentLine.hasPrevious()) {
			return;
		}
		Line previous = currentLine.getPrevious();
		previous.setOffset(currentLine.getOffset());
		document.setCurrentLine(previous);
	}

	public void down() {
		Line currentLine = document.getCurrentLine();
		if (!currentLine.hasNext()) {
			return;
		}
		Line next = currentLine.getNext();
		next.setOffset(currentLine.getOffset());
		document.setCurrentLine(next);
	}

	public void home() {
		Line currentLine = document.getCurrentLine();
		currentLine.setOffset(0);
	}

	public void end() {
		Line currentLine = document.getCurrentLine();
		currentLine.setOffset(currentLine.getLenght());
	}

}
