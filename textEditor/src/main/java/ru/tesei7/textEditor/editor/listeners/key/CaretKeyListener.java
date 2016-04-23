package ru.tesei7.textEditor.editor.listeners.key;

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
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.LEFT));
			break;
		case KeyEvent.VK_RIGHT:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.RIGHT));
			break;
		case KeyEvent.VK_UP:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.UP));
			break;
		case KeyEvent.VK_DOWN:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.DOWN));
			break;
		case KeyEvent.VK_PAGE_UP:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.PAGE_UP));
			break;
		case KeyEvent.VK_PAGE_DOWN:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.PAGE_DOWN));
			break;
		case KeyEvent.VK_HOME:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.HOME));
			break;
		case KeyEvent.VK_END:
			observable.notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.END));
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
