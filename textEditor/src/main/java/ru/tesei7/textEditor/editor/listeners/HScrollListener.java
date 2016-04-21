package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class HScrollListener implements AdjustmentListener{

	private SyntaxTextEditor editor;

	public HScrollListener(SyntaxTextEditor editor) {
		this.editor = editor;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
