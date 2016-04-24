package ru.tesei7.textEditor.editor.listeners;

import static org.mockito.Mockito.when;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.scroll.bar.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.bar.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.bar.FrameObserverable;

public class SyntaxMouseListener extends MouseAdapter {

	private SyntaxCaretObservable caretObservable;
	private SyntaxDocument document;

	public SyntaxMouseListener(SyntaxDocument document, SyntaxCaretObservable caretObservable) {
		this.document = document;
		this.caretObservable = caretObservable;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int value = e.getWheelRotation() * SyntaxTextEditor.MOUSE_WHEEL_SCROLL_LINES;
		Line l = document.getFirstVisibleLine();
		if (value > 0) {
			while (l.hasNext() && value > 0) {
				l = l.getNext();
				value--;
			}
		} else if (value < 0) {
			while (l.hasPrevious() && value < 0) {
				l = l.getPrevious();
				value++;
			}
		}
		document.setFirstVisibleLine(l);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

}
