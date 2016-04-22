package ru.tesei7.textEditor.editor.caret;

public class SyntaxCaretEvent {

	private SyntaxCaretEventType type;
	private int x;
	private int y;

	public SyntaxCaretEvent(SyntaxCaretEventType type) {
		this.type = type;
	}

	public SyntaxCaretEvent(SyntaxCaretEventType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public SyntaxCaretEventType getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
