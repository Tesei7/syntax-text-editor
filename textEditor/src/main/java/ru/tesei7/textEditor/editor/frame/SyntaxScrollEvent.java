package ru.tesei7.textEditor.editor.frame;

public class SyntaxScrollEvent {
	private Direction direction;
	private int value;

	public SyntaxScrollEvent(Direction direction, int value) {
		this.direction = direction;
		this.value = value;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getValue() {
		return value;
	}

}
