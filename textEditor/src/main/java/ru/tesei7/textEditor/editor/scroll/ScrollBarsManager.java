package ru.tesei7.textEditor.editor.scroll;

import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class ScrollBarsManager implements DocumentDimensionsListener, FrameListener {

	SyntaxDocument document;
	JScrollBar hBar;
	JScrollBar vBar;

	public ScrollBarsManager(SyntaxDocument document, JScrollBar hBar, JScrollBar vBar) {
		this.document = document;
		this.hBar = hBar;
		this.vBar = vBar;
	}

	public void setDocument(SyntaxDocument document) {
		this.document = document;
	}

	@Override
	public void onDimensionsChanged(DimensionsEvent e) {
		recalculateMaxCols();
		if (e.getType() == DimensionType.X_AND_Y) {
			recalculateMaxRows();
		}
	}

	void recalculateMaxRows() {
		int height = document.getSize();
		int max = Math.max(height, document.getRows());
		setBarFields(vBar, max, document.getRows());
	}

	void recalculateMaxCols() {
		int cols = document.getMaxCols();
		int max = Math.max(cols, document.getCols());
		setBarFields(hBar, max, document.getCols());
	}

	@Override
	public void onFrameChanged(FrameEvent e) {
		Integer value = e.getValue();
		switch (e.getType()) {
		case HORIZONTAL:
			setBarValue(hBar, value);
			break;
		case VERTICAL:
			setBarValue(vBar, value);
			break;
		}
	}

	/**
	 * Set bar maximum and visible amount without notification
	 * 
	 * @param max bar maximum
	 * @param extent bar extent
	 */
	void setBarFields(JScrollBar bar, int max, int extent) {
		AdjustmentListener[] listeners = bar.getAdjustmentListeners();
        for (AdjustmentListener listener : listeners) {
            bar.removeAdjustmentListener(listener);
        }
		bar.setMaximum(max);
		bar.setVisibleAmount(extent);
		for (AdjustmentListener l : listeners) {
			bar.addAdjustmentListener(l);
		}
	}

	/**
	 * Set bar current value without listeners notification
	 * 
	 * @param bar scroll bar
	 * @param value bar value
	 */
	void setBarValue(JScrollBar bar, int value) {
		AdjustmentListener[] listeners = bar.getAdjustmentListeners();
        for (AdjustmentListener listener : listeners) {
            bar.removeAdjustmentListener(listener);
        }
		bar.setValue(value);
		for (AdjustmentListener l : listeners) {
			bar.addAdjustmentListener(l);
		}
	}

}
