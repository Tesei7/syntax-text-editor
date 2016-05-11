package ru.tesei7.textEditor.editor;

import ru.tesei7.textEditor.editor.document.model.SyntaxDocumentMemento;

/**
 * Caretaker for {@link SyntaxTextEditor} state
 * Created by Ilya on 11.05.2016.
 */
public class SyntaxTextEditorCaretaker {
    private SyntaxDocumentMemento memento;

    /**
     * Get memento and forget it
     * @return memento
     */
    public SyntaxDocumentMemento getMemento() {
        SyntaxDocumentMemento memento = this.memento;
        this.memento = null;
        return memento;
    }

    /**
     * Ste memento
     * @param memento memento
     */
    public void setMemento(SyntaxDocumentMemento memento) {
        this.memento = memento;
    }
}
