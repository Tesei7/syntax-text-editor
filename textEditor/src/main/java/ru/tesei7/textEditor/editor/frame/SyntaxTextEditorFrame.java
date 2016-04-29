package ru.tesei7.textEditor.editor.frame;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

/**
 * This listener moves visible part of document (frame) when caret moves and
 * scroll events occured
 * 
 * @author Ilya Bochkarev
 *
 */
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
		makeCaretVisibleY();
		makeCaretVisibleX();
	}

	void makeCaretVisibleX() {
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

	void makeCaretVisibleY() {
		int firstVisibleRow = document.getFirstVisibleRow();
		int currentIndex = document.getCurLineIndex();
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
