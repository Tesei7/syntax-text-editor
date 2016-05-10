package ru.tesei7.textEditor.editor.scroll;

/**
 * Listen document height or width changes
 * 
 * @author Ilya
 *
 */
public interface DocumentDimensionsListener {
	void onDimensionsChanged(DimensionsEvent e);
}
