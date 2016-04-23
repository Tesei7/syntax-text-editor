package ru.tesei7.textEditor.editor.scroll.bar;

import ru.tesei7.textEditor.editor.document.model.Line;

public class FrameEvent {
	private Line firstVisibleLine;
	private int xOffset;

	public FrameEvent(int xOffset) {
		this.xOffset = xOffset;
	}

	public FrameEvent(Line firstVisibleLine) {
		this.firstVisibleLine = firstVisibleLine;
	}

	public Line getFirstVisibleLine() {
		return firstVisibleLine;
	}

	public int getxOffset() {
		return xOffset;
	}

}
