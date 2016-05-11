package ru.tesei7.textEditor.editor.scroll;

import java.util.ArrayList;
import java.util.List;

/**
 * Broadcast document height or width changes 
 * @author Ilya
 *
 */
public class DimensionsObservable {
	List<DocumentDimensionsListener> listeners = new ArrayList<>();

	public void addListener(DocumentDimensionsListener listener) {
		listeners.add(listener);
	}

	public void removeListener(DocumentDimensionsListener listener) {
		listeners.remove(listener);
	}

	public void removeAllListeners() {
		listeners.clear();
	}

	public void notifyListeners(DimensionsEvent e) {
		listeners.stream().forEach(l -> l.onDimensionsChanged(e));
	}
}
