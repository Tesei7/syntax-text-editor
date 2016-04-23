package ru.tesei7.textEditor.editor.scroll.bar;

import ru.tesei7.textEditor.editor.document.model.Line;

public class FrameEvent {
	private Line firstVisibleLine;
	private Integer firstVisibleCol;

	public FrameEvent(Integer firstVisibleCol) {
		this.firstVisibleCol = firstVisibleCol;
	}

	public FrameEvent(Line firstVisibleLine) {
		this.firstVisibleLine = firstVisibleLine;
	}

	public Line getFirstVisibleLine() {
		return firstVisibleLine;
	}

	public Integer getFirstVisibleCol() {
		return firstVisibleCol;
	}

}
