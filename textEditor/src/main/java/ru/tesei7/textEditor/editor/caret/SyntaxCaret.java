package ru.tesei7.textEditor.editor.caret;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

/**
 * Listens all actions with caret and perform changes in {@link SyntaxDocument}
 * 
 * @author Ilya
 *
 */
public class SyntaxCaret implements SyntaxCaretListener {

	private SyntaxDocument document;
	private FontProperties fontProperties;

	public SyntaxCaret(SyntaxDocument document, FontProperties fontProperties) {
		this.document = document;
		this.fontProperties = fontProperties;
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		if (e.getType() != SyntaxCaretEventType.MOUSE_SELECTION && e.getType() != SyntaxCaretEventType.INSERT) {
			if (e.isWithShift()) {
				if (document.getSelection().notSelected()) {
					startSelection();
				}
			} else {
				document.clearSelection();
			}
		}

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
			moveY(-1 * document.getRows());
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
			CaretType caretType = document.getCaretType();
			document.setCaretType(caretType != CaretType.INSERT ? CaretType.INSERT : CaretType.NORMAL);
			break;
		case MOUSE:
			setCaret(e.getX(), e.getY(), e.isWithShift());
			break;
		case MOUSE_SELECTION:
			setSelection(e.getX(), e.getY());
			break;
		default:
			break;
		}

		if (e.getType() != SyntaxCaretEventType.MOUSE_SELECTION && e.getType() != SyntaxCaretEventType.INSERT) {
			if (e.isWithShift()) {
				setSelection();
			}
		}
	}

	public int getX() {
		return getCurrentLine().getOffset();
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

	private void startSelection() {
		document.startSelection(document.getLineIndex(document.getCurrentLine()),
				document.getCurrentLine().getOffset());
	}

	private void setSelection() {
		document.selectTo(document.getLineIndex(document.getCurrentLine()), document.getCurrentLine().getOffset());
	}

	private void setCaret(int x, int y, boolean withShift) {
		int lineIndex = y / fontProperties.getLineHeight() + document.getFirstVisibleRow();
		document.setCurrentLine(lineIndex);
		int offsetToPaint = x / fontProperties.getCharWidth() + document.getFirstVisibleCol();
		int offset = document.getCurrentLine().getOffestByOffsetToPaint(offsetToPaint);
		document.getCurrentLine().setOffset(offset);

		if (!withShift) {
			document.clearSelection();
			document.startSelection(lineIndex, offset);
		} else {
			document.selectTo(lineIndex, offset);
		}
	}

	private void setSelection(int x, int y) {
		int lineIndex = y / fontProperties.getLineHeight() + document.getFirstVisibleRow();
		int offsetToPaint = x / fontProperties.getCharWidth() + document.getFirstVisibleCol();
		Line line = document.getLineByIndex(lineIndex);
		int offset = line.getOffestByOffsetToPaint(offsetToPaint);
		document.selectTo(lineIndex, offset);
		document.setCurrentLine(line);
		line.setOffset(offset);
	}

}
