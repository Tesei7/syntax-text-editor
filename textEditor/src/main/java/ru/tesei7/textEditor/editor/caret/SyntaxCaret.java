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

	public void setDocument(SyntaxDocument document) {
		this.document = document;
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		if (e.getType() == null) {
			return;
		}
		doSelectBefore(e);

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
			if (!e.isWithShift()) {
				CaretType caretType = document.getCaretType();
				document.setCaretType(caretType != CaretType.INSERT ? CaretType.INSERT : CaretType.NORMAL);
			}
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

		doSelectAfter(e);
	}

	void doSelectAfter(SyntaxCaretEvent e) {
		if (e.getType() != SyntaxCaretEventType.MOUSE_SELECTION && e.getType() != SyntaxCaretEventType.INSERT) {
			if (e.isWithShift()) {
				setSelection();
			}
		}
	}

	void doSelectBefore(SyntaxCaretEvent e) {
		if (e.getType() != SyntaxCaretEventType.MOUSE_SELECTION && e.getType() != SyntaxCaretEventType.INSERT) {
			if (e.isWithShift()) {
				if (document.getSelection().notSelected()) {
					startSelection();
				}
			} else {
				document.clearSelection();
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
		Line targetLine = document.getLineByIndex(document.getCurLineIndex() + y);
		targetLine.setOffset(document.getTargetLineOffset(targetLine));
		document.moveCurLineIndex(y);
	}

	Line getCurrentLine() {
		return document.getCurrentLine();
	}

	private void startSelection() {
		document.startSelection(document.getCurLineIndex(), document.getCurrentLine().getOffset());
	}

	private void setSelection() {
		document.selectTo(document.getCurLineIndex(), document.getCurrentLine().getOffset());
	}

	void setCaret(int x, int y, boolean withShift) {
		int lineIndex = y / fontProperties.getLineHeight() + document.getFirstVisibleRow();
		document.setCurLineIndex(lineIndex);
		int offsetToPaint = x / fontProperties.getCharWidth() + document.getFirstVisibleCol();
		int offset = document.getCurrentLine().getOffsetByOffsetToPaint(offsetToPaint);
		document.getCurrentLine().setOffset(offset);

		if (!withShift) {
			document.clearSelection();
			document.startSelection(lineIndex, offset);
		} else {
			document.selectTo(lineIndex, offset);
		}
	}

	void setSelection(int x, int y) {
		int lineIndex = y / fontProperties.getLineHeight() + document.getFirstVisibleRow();
		int offsetToPaint = x / fontProperties.getCharWidth() + document.getFirstVisibleCol();
		Line line = document.getLineByIndex(lineIndex);
		int offset = line.getOffsetByOffsetToPaint(offsetToPaint);
		document.selectTo(lineIndex, offset);
		document.setCurLineIndex(lineIndex);
		line.setOffset(offset);
	}

}
