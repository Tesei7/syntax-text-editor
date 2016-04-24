package ru.tesei7.textEditor.editor.scroll;

public class SyntaxScrollEvent {
	private Direction direction;
	private Integer value;

	public SyntaxScrollEvent(Direction direction, Integer value) {
		this.direction = direction;
		this.value = value;
	}

	public Direction getDirection() {
		return direction;
	}

	public Integer getValue() {
		return value;
	}

}
