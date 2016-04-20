package ru.tesei7.textEditor.editor.listeners.key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.tesei7.textEditor.editor.SyntaxDocument;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class TextKeyListener implements KeyListener {
	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	public void setEditor(SyntaxTextEditor editor) {
		this.editor = editor;
		document = editor.getDocument();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.VK_BACK_SPACE && keyChar != KeyEvent.VK_DELETE && keyChar != KeyEvent.CHAR_UNDEFINED) {
			document.addChar(keyChar);
			editor.repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			document.backspaceChar();
			editor.repaint();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
