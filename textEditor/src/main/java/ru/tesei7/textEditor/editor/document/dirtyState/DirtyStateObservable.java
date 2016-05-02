package ru.tesei7.textEditor.editor.document.dirtyState;

import java.util.ArrayList;
import java.util.List;

public class DirtyStateObservable {
	List<DirtyStateListener> listeners = new ArrayList<>();

	public void addListener(DirtyStateListener listener) {
		listeners.add(listener);
	}

	public void removeListener(DirtyStateListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners(DirtyStateEvent e) {
		if (e.getOldDirtyState() != e.getNewDirtyState()) {
			listeners.stream().forEach(l -> l.onDirtyStateChanged(e));
		}
	}
}
