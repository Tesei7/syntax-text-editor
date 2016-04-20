package ru.tesei7.textEditor.editor.listeners.key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.tesei7.textEditor.editor.Line;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class BaseKeyListener implements KeyListener {
	private SyntaxTextEditor editor;

	public void setEditor(SyntaxTextEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Line currentLine = editor.getDocument().getCurrentLine();
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			currentLine.left();
			editor.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			currentLine.right();
			editor.repaint();
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
