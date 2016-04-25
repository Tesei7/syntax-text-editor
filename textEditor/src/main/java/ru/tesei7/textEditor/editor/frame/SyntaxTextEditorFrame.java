package ru.tesei7.textEditor.editor.frame;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxTextEditorFrame implements SyntaxCaretListener, SyntaxScrollListener {

	private SyntaxDocument document;

	public SyntaxTextEditorFrame(SyntaxDocument document) {
		this.document = document;
	}

	@Override
	public void onScrollChanged(SyntaxScrollEvent e) {
		Integer value = e.getValue();
		if (e.getDirection() == Direction.VERTICAL) {
			document.setFirstVisibleRow(value);
		} else {
			document.setFirstVisibleCol(value);
		}
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

		if (beforeFrame) {
			document.setFirstVisibleCol(offsetToPaint);
		} else if (afterFrame) {
			int col = Math.max(0, offsetToPaint - document.getCols());
			document.setFirstVisibleCol(col);
		} else {
			document.checkLastColNotEmpty();
		}
	}

	private void makeCaretVisibleY(SyntaxCaretEventType type) {
		int firstVisibleRow = document.getFirstVisibleRow();
		int currentIndex = document.getLineIndex(document.getCurrentLine());
		boolean beforeFrame = currentIndex < firstVisibleRow;
		boolean afterFrame = currentIndex >= firstVisibleRow + document.getRows();

		if (beforeFrame) {
			document.setFirstVisibleRow(currentIndex);
		} else if (afterFrame) {
			document.setFirstVisibleRow(currentIndex - (document.getRows() - 1));
		} else {
			document.checkLastLinesNotEmpty();
		}
	}

}
