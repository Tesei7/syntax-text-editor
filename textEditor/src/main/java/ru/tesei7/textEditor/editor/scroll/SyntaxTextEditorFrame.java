package ru.tesei7.textEditor.editor.scroll;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxTextEditorFrame implements SyntaxCaretListener, SyntaxScrollListener {

	private SyntaxDocument document;

	public SyntaxTextEditorFrame(SyntaxDocument document) {
		this.document = document;
	}

	@Override
	public void onScrollChanged(SyntaxScrollEvent e) {
		if (e.getDirection() == Direction.VERTICAL) {
			scrollVerical(e.getValue());
		} else {
			scrollHorizontal(e.getValue());
		}
	}

	void scrollVerical(int value) {
		document.setFirstVisibleRow(value);
	}

	void scrollHorizontal(int value) {
		document.setFirstVisibleCol(value);
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		// if caret not visible now, make it visible
		SyntaxCaretEventType type = e.getType();
		makeCaretVisibleY(type);
		makeCaretVisibleX(type);
	}

	private void makeCaretVisibleX(SyntaxCaretEventType type) {
		int offsetToPaint = document.getCurrentLine().getOffsetToPaint();
		boolean beforeFrame = offsetToPaint < document.getFirstVisibleCol();
		boolean afterFrame = offsetToPaint > document.getFirstVisibleCol() + document.getCols();
		boolean atEndOfLine = document.getCurrentLine().atEndOfLine();

		if (beforeFrame) {
			document.setFirstVisibleCol(offsetToPaint);
		} else if (afterFrame || atEndOfLine) {
			int col = Math.max(0, offsetToPaint - document.getCols());
			document.setFirstVisibleCol(col);
		}
	}

	private void makeCaretVisibleY(SyntaxCaretEventType type) {
		int curLineY = document.getCurrentLineY();
		int currentIndex = document.getLineIndex(document.getCurrentLine());
		boolean beforeFrame = curLineY == -1;
		boolean afterFrame = curLineY == -2;

		if (beforeFrame) {
			document.setFirstVisibleRow(currentIndex);
		} else if (afterFrame) {
			document.setFirstVisibleRow(currentIndex - (document.getRows() - 1));
		}
	}

}
