package ru.tesei7.textEditor.editor.scroll;

import java.util.ArrayList;
import java.util.List;

/**
 * Frame event observable
 * Created by Ilya on 10.05.2016.
 */
public class FrameObservable {
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
