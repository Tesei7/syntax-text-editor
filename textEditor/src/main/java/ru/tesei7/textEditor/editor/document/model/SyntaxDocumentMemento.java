package ru.tesei7.textEditor.editor.document.model;

/**
 * Memento to save and restore SyntaxDocument
 * Created by Ilya on 10.05.2016.
 */
public class SyntaxDocumentMemento {
    private final SyntaxDocument state;

    public SyntaxDocumentMemento(SyntaxDocument state) {
        this.state = state;
    }

    public SyntaxDocument getState() {
        return state;
    }
}
