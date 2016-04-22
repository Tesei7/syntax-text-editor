package ru.tesei7.textEditor.editor.document;

public class DocumentEditEvent {
	private DocumentEditEventType type;
	private char c;

	public DocumentEditEvent(DocumentEditEventType type) {
		this.type = type;
	}

	public DocumentEditEvent(DocumentEditEventType type, char c) {
		this.type = type;
		this.c = c;
	}

	public DocumentEditEventType getType() {
		return type;
	}

	public char getChar() {
		return c;
	}
}
