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
		Line line = document.getFirstLine();
		for (int i = 0; i < value; i++) {
			if (line.hasNext()) {
				line = line.getNext();
			} else {
				break;
			}
		}
		document.setFirstVisibleLine(line);
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
		int firstVisibleIndex = document.getLineIndex(document.getFirstVisibleLine());
		int currentIndex = document.getLineIndex(document.getCurrentLine());
		boolean beforeFrame = currentIndex < firstVisibleIndex;
		boolean afterFrame = currentIndex >= firstVisibleIndex + document.getRows();

		if (beforeFrame) {
			document.setFirstVisibleLine(document.getCurrentLine());
		} else if (afterFrame) {
			document.setFirstVisibleLine(getFvToShowCurrentAtBootom());
		} else if (!document.getCurrentLine().hasNext()) {
			// is last line
			document.setFirstVisibleLine(getFvToShowCurrentAtBootom());
		}
	}

	/**
	 * 
	 * @return first visible line to current line be last visible
	 */
	private Line getFvToShowCurrentAtBootom() {
		Line line = document.getCurrentLine();
		// rows steps up to show cur line at bottom
		int i = document.getRows() - 2;
		while (line.hasPrevious() && i >= 0) {
			line = line.getPrevious();
			i--;
		}
		return line;
	}

}
