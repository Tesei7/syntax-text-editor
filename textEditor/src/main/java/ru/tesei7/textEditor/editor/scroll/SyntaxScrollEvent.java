package ru.tesei7.textEditor.editor.scroll;

public class SyntaxScrollEvent {
	private ScrollDirection direction;
	private int adjustmentType;
	private Integer absolut;

	public SyntaxScrollEvent(ScrollDirection direction, int adjasmentType, Integer absolute) {
		this.direction = direction;
		this.adjustmentType = adjasmentType;
		this.absolut = absolute;
	}

	public ScrollDirection getDirection() {
		return direction;
	}

	public int getAdjustmentType() {
		return adjustmentType;
	}

	public Integer getAbsolut() {
		return absolut;
	}

}
