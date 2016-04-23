package ru.tesei7.textEditor.editor.scroll.bar;

public class DimensionsEvent {
	private DimensionType type;

	public DimensionsEvent(DimensionType type) {
		this.type = type;
	}

	public DimensionType getType() {
		return type;
	}

}
