package ru.tesei7.textEditor;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class ResizeListener extends ComponentAdapter {

	private SyntaxTextEditor textArea;

	public ResizeListener(SyntaxTextEditor textArea) {
		this.textArea = textArea;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		textArea.resizeToFitParent();
	}
}
