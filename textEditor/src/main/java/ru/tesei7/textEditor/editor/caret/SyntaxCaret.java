package ru.tesei7.textEditor.editor.caret;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

/**
 * Listens all actions with caret and perform changes in {@link SyntaxDocument}
 * 
 * @author Ilya
 *
 */
public class SyntaxCaret implements SyntaxCaretListener {

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

	public void moveY(int y) {
		Line targetLine = document.getCurrentLine();
		if (y < 0) {
			while (targetLine.hasPrevious() && y < 0) {
				targetLine = targetLine.getPrevious();
				y++;
			}
		} else if (y > 0) {
			while (targetLine.hasNext() && y > 0) {
				targetLine = targetLine.getNext();
				y--;
			}
		}
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
		moveY(-1);
	}

	public void down() {
		moveY(1);
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

	public int getCols() {
		return editor.getCols();
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		switch (e.getType()) {
		case LEFT:
			left();
			break;
		case RIGHT:
			right();
			break;
		case UP:
			up();
			break;
		case DOWN:
			down();
			break;
		case END:
			end();
			break;
		case HOME:
			home();
			break;
		case INSERT:
			// TODO
			break;
		case PAGE_DOWN:
			// TODO
			break;
		case PAGE_UP:
			// TODO
			break;
		case MOUSE:
			// TODO
			break;
		}
	}

}
