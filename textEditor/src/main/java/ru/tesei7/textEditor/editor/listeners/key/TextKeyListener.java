package ru.tesei7.textEditor.editor.listeners.key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.inject.Inject;

import ru.tesei7.textEditor.editor.SyntaxDocumentEditor;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class TextKeyListener implements KeyListener {
	@Inject
	private SyntaxDocumentEditor documentEditor;

	private SyntaxTextEditor editor;
	private SyntaxDocument document;

	public void setEditor(SyntaxTextEditor editor) {
		this.editor = editor;
		document = editor.getDocument();
		documentEditor.setDocument(document);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (isPrintableChar(c) || c == '\n' || c == '\t') {
			documentEditor.printChar(c);
			editor.repaint();
		}
	}

	public boolean isPrintableChar(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return (!Character.isISOControl(c)) && c != KeyEvent.CHAR_UNDEFINED && block != null
				&& block != Character.UnicodeBlock.SPECIALS;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			documentEditor.backspace();
			editor.repaint();
			break;
		case KeyEvent.VK_DELETE:
			documentEditor.delete();
			editor.repaint();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
