package ru.tesei7.textEditor.editor.scroll;

import java.util.ArrayList;
import java.util.List;

public class FrameObserverable {
	List<FrameListener> listeners = new ArrayList<>();

	public void addListener(FrameListener listener) {
		listeners.add(listener);
	}

	public void removeListener(FrameListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners(FrameEvent e) {
		listeners.stream().forEach(l -> l.onFrameChanged(e));
	}
}
