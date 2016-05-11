package ru.tesei7.textEditor.editor;

import org.junit.Test;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocumentMemento;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SyntaxTextEditorCaretakerTest {
    private SyntaxTextEditorCaretaker caretaker = new SyntaxTextEditorCaretaker();

    @Test
    public void getMemento() throws Exception {
        assertNull(caretaker.getMemento());

        SyntaxDocumentMemento memento = mock(SyntaxDocumentMemento.class);
        caretaker.setMemento(memento);
        assertThat(caretaker.getMemento(), is(memento));
        assertNull(caretaker.getMemento());
    }

}