package ru.tesei7.textEditor.editor.caret;

import java.util.List;

import javax.annotation.PostConstruct;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;
import ru.tesei7.textEditor.editor.painter.SyntaxDocumentPainter;

public class SyntaxCaret {

	private CaretType type;
	private SyntaxDocument document;
	private SyntaxDocumentPainter painter;

	@PostConstruct
	public void init() {
		type = CaretType.NORMAL;
	}

	public void setEditor(SyntaxTextEditor editor) {
		this.document = editor.getDocument();
		this.painter = editor.getPainter();
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
		Line targetLine = visibleLines.get(y);
		targetLine.setOffset(painter.getTargetLineOffset(targetLine));
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
		setX(document.getCurrentLine().getLenght());
	}

	public Line getCurrentLine() {
		return document.getCurrentLine();
	}

}
