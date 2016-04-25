package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxMouseListener extends MouseAdapter {

	private SyntaxCaretObservable caretObservable;
	private SyntaxDocument document;

	public SyntaxMouseListener(SyntaxDocument document, SyntaxCaretObservable caretObservable) {
		this.document = document;
		this.caretObservable = caretObservable;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int value = e.getWheelRotation() * SyntaxTextEditor.MOUSE_WHEEL_SCROLL_LINES;
		document.setFirstVisibleRow(document.getFirstVisibleRow() + value);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		caretObservable
				.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.MOUSE, e.getX(), e.getY(), e.isShiftDown()));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		caretObservable.notifyListeners(
				new SyntaxCaretEvent(SyntaxCaretEventType.MOUSE_SELECTION, e.getX(), e.getY(), e.isShiftDown()));
	}

}
