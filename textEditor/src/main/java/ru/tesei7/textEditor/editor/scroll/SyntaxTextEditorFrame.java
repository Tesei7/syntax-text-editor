package ru.tesei7.textEditor.editor.scroll;

import java.awt.event.AdjustmentEvent;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxTextEditorFrame implements SyntaxCaretListener, SyntaxScrollListener {

	private SyntaxTextEditor editor;
	SyntaxDocument document;

	public SyntaxTextEditorFrame(SyntaxTextEditor editor) {
		this.editor = editor;
		this.document = this.editor.getDocument();
	}

	@Override
	public void onScrollChanged(SyntaxScrollEvent e) {
		if (e.getDirection() == ScrollDirection.VERTICAL) {
			scrollVerical(e.getAdjustmentType(), e.getAbsolut());
		} else {
			scrollHorizontal(e.getAdjustmentType(), e.getAbsolut());
		}
	}

	void scrollVerical(int adjustmentType, int value) {
		int rows = editor.getRows();
		switch (adjustmentType) {
		case AdjustmentEvent.UNIT_INCREMENT:
			scrollVericalReletive(1);
			break;
		case AdjustmentEvent.UNIT_DECREMENT:
			scrollVericalReletive(-1);
			break;
		case AdjustmentEvent.BLOCK_INCREMENT:
			scrollVericalReletive(rows);
			break;
		case AdjustmentEvent.BLOCK_DECREMENT:
			scrollVericalReletive(-1 * rows);
			break;
		case AdjustmentEvent.TRACK:
			scrollVericalAbsolut(value);
			break;
		}
	}

	void scrollVericalReletive(int i) {
		Line line = document.getFirstVisibleLine();
		if (i > 0) {
			while (i > 0 && line.hasNext()) {
				line = line.getNext();
				i--;
			}
		} else {
			while (i < 0 && line.hasPrevious()) {
				line = line.getPrevious();
				i++;
			}
		}
		document.setFirstVisibleLine(line);
	}

	void scrollVericalAbsolut(int value) {
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

	void scrollHorizontal(int adjustmentType, int value) {
		// TODO
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		// if caret not visible now
		if (document.getCurrentLineY() < 0) {
			SyntaxCaretEventType type = e.getType();

			if (type == SyntaxCaretEventType.PAGE_DOWN || type == SyntaxCaretEventType.DOWN) {
				document.setFirstVisibleLine(getFvToShowCurrentAtBootom());
			} else {
				document.setFirstVisibleLine(document.getCurrentLine());
			}
		}
	}

	/**
	 * 
	 * @return first visible line to current line be last visible
	 */
	private Line getFvToShowCurrentAtBootom() {
		Line line = document.getCurrentLine();
		// rows steps up to show cur line at bottom
		int i = editor.getRows() - 2;
		while (line.hasPrevious() && i >= 0) {
			line = line.getPrevious();
			i--;
		}
		return line;
	}

}
