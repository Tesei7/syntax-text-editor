package ru.tesei7.textEditor.editor.scroll.bar;

import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class ScrollBarsManager implements DocumentDimensionsListener, FrameListener {

	private SyntaxDocument document;
	private JScrollBar hbar;
	private JScrollBar vbar;

	public ScrollBarsManager(SyntaxDocument document, JScrollBar hbar, JScrollBar vbar) {
		this.document = document;
		this.hbar = hbar;
		this.vbar = vbar;
	}

	@Override
	public void onDimensionsChanged(DimensionsEvent e) {
		int height = document.getSize();
		int max = Math.max(height, document.getRows());
		setBarMaximum(vbar, max);
	}

	@Override
	public void onFrameChanged(FrameEvent e) {
		Line firstVisibleLine = e.getFirstVisibleLine();
		if (firstVisibleLine != null) {
			int value = document.getLineIndex(firstVisibleLine);
			setBarValue(vbar, value);
		}
		// TODO horizontal scroll
	}

	/**
	 * Set bar maximum without notification
	 * 
	 * @param height
	 */
	private void setBarMaximum(JScrollBar bar, int max) {
		AdjustmentListener[] listeners = bar.getAdjustmentListeners();
		for (int i = 0; i < listeners.length; i++) {
			bar.removeAdjustmentListener(listeners[i]);
		}
		bar.setMaximum(max);
		for (AdjustmentListener l : listeners) {
			bar.addAdjustmentListener(l);
		}
	}

	/**
	 * Set bar current value without listeners notification
	 * 
	 * @param bar
	 * @param value
	 */
	private void setBarValue(JScrollBar bar, int value) {
		AdjustmentListener[] listeners = bar.getAdjustmentListeners();
		for (int i = 0; i < listeners.length; i++) {
			bar.removeAdjustmentListener(listeners[i]);
		}
		bar.setValue(value);
		for (AdjustmentListener l : listeners) {
			bar.addAdjustmentListener(l);
		}
	}

}
