package ru.tesei7.textEditor.editor.caret;

import java.util.List;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxCaret {

	private CaretType type;
	private SyntaxDocument document;
	private SyntaxTextEditor editor;
	private CaretService caretService;

	public SyntaxCaret(SyntaxTextEditor editor) {
		type = CaretType.NORMAL;
		this.editor = editor;
		this.document = editor.getDocument();
		this.caretService = new CaretService(document);
	}

	public CaretType getType() {
		return type;
	}

	public void setType(CaretType type) {
		this.type = type;
	}

	public int getX() {
		return getCurrentLine().getOffset();
	}
	
	public int getXToPaint() {
		return caretService.getXToPaint();
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
		if (y < 0 || y > editor.getRows()) {
			return;
		}
		List<Line> visibleLines = document.getVisibleLines();
		if (y > visibleLines.size() - 1) {
			return;
		}
		Line targetLine = visibleLines.get(y);
		targetLine.setOffset(caretService.getTargetLineOffset(targetLine));
		document.setCurrentLine(targetLine);
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
		setX(getCurrentLine().getLenght());
	}

	Line getCurrentLine() {
		return document.getCurrentLine();
	}

}
