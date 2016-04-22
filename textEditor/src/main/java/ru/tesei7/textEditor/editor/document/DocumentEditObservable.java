package ru.tesei7.textEditor.editor.document;

import java.util.ArrayList;
import java.util.List;

public abstract class DocumentEditObservable {
	private List<DocumentEditListener> listeners = new ArrayList<>();

	public void addListener(DocumentEditListener listener) {
		listeners.add(listener);
	}

	public void removeListener(DocumentEditListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners(DocumentEditEvent e) {
		listeners.stream().forEach(l -> l.onDocumentEdited(e));
	}
}
