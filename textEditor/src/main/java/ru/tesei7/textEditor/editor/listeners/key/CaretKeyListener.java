package ru.tesei7.textEditor.editor.listeners.key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaret;

public class CaretKeyListener implements KeyListener {
	private SyntaxTextEditor editor;
	private SyntaxCaret caret;

	public void setEditor(SyntaxTextEditor editor) {
		this.editor = editor;
		caret = editor.getCaret();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			caret.left();
			editor.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			caret.right();
			editor.repaint();
			break;
		case KeyEvent.VK_UP:
			caret.up();
			editor.repaint();
			break;
		case KeyEvent.VK_DOWN:
			caret.down();
			editor.repaint();
			break;
		case KeyEvent.VK_HOME:
			caret.home();
			editor.repaint();
			break;
		case KeyEvent.VK_END:
			caret.end();
			editor.repaint();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
