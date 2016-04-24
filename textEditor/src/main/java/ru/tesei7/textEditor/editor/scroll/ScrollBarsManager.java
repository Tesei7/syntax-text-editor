package ru.tesei7.textEditor.editor.scroll;

import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

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
		recalcMaxCols();
		if (e.getType() == DimensionType.X_AND_Y) {
			recalcMaxRows();
		}
	}

	void recalcMaxRows() {
		int height = document.getSize();
		int max = Math.max(height, document.getRows());
		setBarMaximum(vbar, max);
	}

	void recalcMaxCols() {
		int cols = document.getMaxCols();
		int max = Math.max(cols, document.getCols());
		setBarMaximum(hbar, max);
	}

	@Override
	public void onFrameChanged(FrameEvent e) {
		Integer value = e.getValue();
		switch (e.getType()) {
		case HORIZONTAL:
			setBarValue(hbar, value);
			break;
		case VERTICAL:
			setBarValue(vbar, value);
			break;
		}
	}

	/**
	 * Set bar maximum without notification
	 * 
	 * @param height
	 */
	void setBarMaximum(JScrollBar bar, int max) {
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
	void setBarValue(JScrollBar bar, int value) {
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