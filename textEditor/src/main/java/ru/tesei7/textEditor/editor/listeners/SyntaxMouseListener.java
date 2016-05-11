package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollObservable;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObservable;

public class SyntaxMouseListener extends MouseAdapter {

    private FrameObservable frameObservable;
    private SyntaxCaretObservable caretObservable;
	private SyntaxScrollObservable scrollObservable;

	public SyntaxMouseListener(SyntaxScrollObservable scrollObservable, FrameObservable frameObservable, SyntaxCaretObservable caretObservable) {
		this.scrollObservable = scrollObservable;
        this.frameObservable = frameObservable;
        this.caretObservable = caretObservable;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int value = e.getWheelRotation() * SyntaxTextEditor.MOUSE_WHEEL_SCROLL_LINES;
		scrollObservable.notifyListeners(new SyntaxScrollEvent(Direction.VERTICAL_ADD, value));
        frameObservable.notifyListeners(new FrameEvent(FrameEventType.VERTICAL_ADD, value));
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
