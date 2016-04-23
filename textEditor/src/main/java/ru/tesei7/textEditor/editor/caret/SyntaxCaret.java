package ru.tesei7.textEditor.editor.caret;

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

	public SyntaxCaret(SyntaxDocument document) {
		type = CaretType.NORMAL;
		this.document = document;
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
		int xToPaint = document.getCurrentLine().getXToPaint();
		int firstVisibleCol = document.getFirstVisibleCol();
		return xToPaint - firstVisibleCol;
	}

	public int getY() {
		return document.getCurrentLineY();
	}

	public void setX(int x) {
		Line currentLine = document.getCurrentLine();
		if (x < 0) {
			x = 0;
		}
		if (x > currentLine.getLength()) {
			x = currentLine.getLength();
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
		targetLine.setOffset(document.getTargetLineOffset(targetLine));
		document.setCurrentLine(targetLine);
	}

	Line getCurrentLine() {
		return document.getCurrentLine();
	}

	public int getCols() {
		return document.getCols();
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		switch (e.getType()) {
		case LEFT:
			setX(getX() - 1);
			break;
		case RIGHT:
			setX(getX() + 1);
			break;
		case UP:
			moveY(-1);
			break;
		case DOWN:
			moveY(1);
			break;
		case PAGE_UP:
			moveY(-1*document.getRows());
			break;
		case PAGE_DOWN:
			moveY(document.getRows());
			break;
		case HOME:
			setX(0);
			break;
		case END:
			setX(getCurrentLine().getLength());
			break;
		case INSERT:
			// TODO
			break;
		case MOUSE:
			// TODO
			break;
		}
	}

}
