package ru.tesei7.textEditor.editor.frame;

import java.util.ArrayList;
import java.util.List;

public class SyntaxScrollObserverable {
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
