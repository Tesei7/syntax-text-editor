package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.tesei7.textEditor.editor.document.DocumentEditEvent;
import ru.tesei7.textEditor.editor.document.DocumentEditEventType;
import ru.tesei7.textEditor.editor.document.DocumentEditObservable;

public class TextKeyListener implements KeyListener {

	private DocumentEditObservable observable;

	public TextKeyListener(DocumentEditObservable documentEditObservable) {
		this.observable = documentEditObservable;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (isPrintableChar(c) || c == '\t') {
			observable.notifyListeners(new DocumentEditEvent(DocumentEditEventType.PRINT_CHAR, c));
		} else if (c == '\n') {
			observable.notifyListeners(new DocumentEditEvent(DocumentEditEventType.NEW_LINE));
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
			observable.notifyListeners(new DocumentEditEvent(DocumentEditEventType.BACKSPACE));
			break;
		case KeyEvent.VK_DELETE:
			observable.notifyListeners(new DocumentEditEvent(DocumentEditEventType.DELETE));
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
