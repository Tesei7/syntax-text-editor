package ru.tesei7.textEditor.editor.scroll;

import java.util.ArrayList;
import java.util.List;

public abstract class SyntaxScrollObserverable {
	private List<SyntaxScrollListener> listeners = new ArrayList<>();

	public void addListener(SyntaxScrollListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SyntaxScrollListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners(SyntaxScrollEvent e) {
		listeners.stream().forEach(l -> l.onScrollChanged(e));
	}
}
