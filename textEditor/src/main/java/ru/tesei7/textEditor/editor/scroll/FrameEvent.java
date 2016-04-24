package ru.tesei7.textEditor.editor.scroll;

public class FrameEvent {
	private FrameEventType type;
	private Integer value;

	public FrameEvent(FrameEventType type, Integer value) {
		this.type = type;
		this.value = value;
	}

	public FrameEventType getType() {
		return type;
	}

	public Integer getValue() {
		return value;
	}

}
