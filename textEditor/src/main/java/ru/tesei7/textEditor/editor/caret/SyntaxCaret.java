package ru.tesei7.textEditor.editor.caret;

import java.util.List;

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

	public int getXToPaint() {
		int x = 0;
		Line currentLine = document.getCurrentLine();
		for (int i = 0; i < currentLine.getOffset(); i++) {
			Character c = currentLine.getText().get(i);
			if (c.equals('\t')) {
				x += 4;
			} else {
				x += 1;
			}
		}
		return x;
	}

	public int getY() {
		return document.getCurrentLineY();
	}

	public void setX(int x) {
		Line currentLine = document.getCurrentLine();
		if (x < 0) {
			x = 0;
		}
		if (x > currentLine.getLenght()) {
			x = currentLine.getLenght();
		}
		currentLine.setOffset(x);
	}

	public void setY(int y) {
		if (y < 0 || y > 10) {
			return;
		}
		List<Line> visibleLines = document.getVisibleLines();
		if (y > visibleLines.size() - 1) {
			return;
		}
		int offset = document.getCurrentLine().getOffset();
		document.setCurrentLine(visibleLines.get(y));
		document.getCurrentLine().setOffset(offset);
	}

	public void left() {
		setX(getX() - 1);
	}

	public void right() {
		setX(getX() + 1);
	}

	public void up() {
		setY(getY() - 1);
	}

	public void down() {
		setY(getY() + 1);
	}

	public void home() {
		setX(0);
	}

	public void end() {
		setX(document.getCurrentLine().getLenght());
	}

}
