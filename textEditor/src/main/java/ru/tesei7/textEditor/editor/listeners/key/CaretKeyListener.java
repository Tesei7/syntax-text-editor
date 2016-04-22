package ru.tesei7.textEditor.editor.listeners.key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;

public class CaretKeyListener extends SyntaxCaretObservable implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.LEFT));
			break;
		case KeyEvent.VK_RIGHT:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.RIGHT));
			break;
		case KeyEvent.VK_UP:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.UP));
			break;
		case KeyEvent.VK_DOWN:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.DOWN));
			break;
		case KeyEvent.VK_PAGE_UP:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.PAGE_UP));
			break;
		case KeyEvent.VK_PAGE_DOWN:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.PAGE_DOWN));
			break;
		case KeyEvent.VK_HOME:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.HOME));
			break;
		case KeyEvent.VK_END:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.END));
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
