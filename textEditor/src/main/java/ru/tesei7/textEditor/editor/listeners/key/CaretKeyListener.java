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
			// caret.left();
			// editor.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.RIGHT));
			// caret.right();
			// editor.repaint();
			break;
		case KeyEvent.VK_UP:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.UP));
			// caret.up();
			// editor.repaint();
			break;
		case KeyEvent.VK_DOWN:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.DOWN));
			// caret.down();
			// editor.repaint();
			break;
		case KeyEvent.VK_HOME:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.HOME));
			// caret.home();
			// editor.repaint();
			break;
		case KeyEvent.VK_END:
			notifyListeners(new SyntaxCaretEvent(SyntaxCaretEventType.END));
			// caret.end();
			// editor.repaint();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
