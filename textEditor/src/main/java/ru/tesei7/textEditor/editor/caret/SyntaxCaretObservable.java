package ru.tesei7.textEditor.editor.caret;

import java.util.ArrayList;
import java.util.List;

public class SyntaxCaretObservable {

	List<SyntaxCaretListener> listeners = new ArrayList<>();

	public void addListener(SyntaxCaretListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SyntaxCaretListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners(SyntaxCaretEvent e) {
		listeners.stream().forEach(l -> l.onCaretChanged(e));
	}
}
