package ru.tesei7.textEditor.editor.caret;

public enum SyntaxCaretEventType {
	UP, DOWN, LEFT, RIGHT, PAGE_UP, PAGE_DOWN, HOME, END, INSERT, MOUSE, MOUSE_SELECTION, 
	
	// Action has already performed, only for notification.
	MOVED_LEFT, MOVED_RIGHT, MOVED_UP, MOVED_DOWN
}
