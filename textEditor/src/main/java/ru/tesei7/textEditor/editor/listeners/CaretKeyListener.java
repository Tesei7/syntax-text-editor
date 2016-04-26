package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;

public class CaretKeyListener implements KeyListener {

	private SyntaxCaretObservable observable;

	public CaretKeyListener(SyntaxCaretObservable syntaxCaretObservable) {
		this.observable = syntaxCaretObservable;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.LEFT, e.isShiftDown()));
			break;
		case KeyEvent.VK_RIGHT:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.RIGHT, e.isShiftDown()));
			break;
		case KeyEvent.VK_UP:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.UP, e.isShiftDown()));
			break;
		case KeyEvent.VK_DOWN:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.DOWN, e.isShiftDown()));
			break;
		case KeyEvent.VK_PAGE_UP:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.PAGE_UP, e.isShiftDown()));
			break;
		case KeyEvent.VK_PAGE_DOWN:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.PAGE_DOWN, e.isShiftDown()));
			break;
		case KeyEvent.VK_HOME:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.HOME, e.isShiftDown()));
			break;
		case KeyEvent.VK_END:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.END, e.isShiftDown()));
			break;
		case KeyEvent.VK_INSERT:
			if (!e.isShiftDown()) {
				observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.INSERT, e.isShiftDown()));
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
