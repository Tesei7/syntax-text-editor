package ru.tesei7.textEditor.editor;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxTextEditorScroller implements FrameMoveListener {

	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	public SyntaxTextEditorScroller(SyntaxTextEditor editor) {
		this.editor = editor;
		this.document = editor.getDocument();
	}

	public void moveVerical(int y) {
		Line line = document.getFirstLine();
		for (int i = 0; i < y; i++) {
			if (line.hasNext()) {
				line = line.getNext();
			} else {
				break;
			}
		}
		document.setFirstVisibleLine(line);
		editor.repaint();
	}

	public void moveHorizontal(int x) {

	}

	public void updateScrollBars() {

	}

}
