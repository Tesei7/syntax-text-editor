package ru.tesei7.textEditor.editor.caret;

public class SyntaxCaretEvent {

	private SyntaxCaretEventType type;
	private int x;
	private int y;
	private boolean withShift = false;

	public SyntaxCaretEvent(SyntaxCaretEventType type, boolean withShift) {
		this.type = type;
		this.withShift = withShift;
	}

	public SyntaxCaretEvent(SyntaxCaretEventType type, int x, int y, boolean withShift) {
		this.type = type;
		this.withShift = withShift;
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
	
	public boolean isWithShift() {
		return withShift;
	}

}
