package ru.tesei7.textEditor.editor.listeners;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollObservable;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObservable;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxMouseListenerTest {
	@InjectMocks
	private SyntaxMouseListener syntaxMouseListener;
	@Mock
	private SyntaxCaretObservable caretObservable;
    @Mock
    private FrameObservable frameObservable;
	@Mock
	private SyntaxScrollObservable scrollObservable;
	@Mock
	private MouseWheelEvent e;
	@Mock
	private MouseEvent me;

	@Test
	public void testMouseWheelMoved() throws Exception {
		when(e.getWheelRotation()).thenReturn(2);
		syntaxMouseListener.mouseWheelMoved(e);
		verify(scrollObservable).notifyListeners(
                argThat(isScrollEvent(Direction.VERTICAL_ADD, SyntaxTextEditor.MOUSE_WHEEL_SCROLL_LINES * 2)));
        verify(frameObservable).notifyListeners(
                argThat(isFrameEvent(FrameEventType.VERTICAL_ADD, SyntaxTextEditor.MOUSE_WHEEL_SCROLL_LINES * 2)));
	}

    private Matcher<FrameEvent> isFrameEvent(FrameEventType type, int value) {
        return allOf(hasProperty("type", equalTo(type)), hasProperty("value", equalTo(value)));
    }

    private Matcher<SyntaxScrollEvent> isScrollEvent(Direction direction, int value) {
        return allOf(hasProperty("direction", equalTo(direction)), hasProperty("value", equalTo(value)));
    }

    @Test
	public void testMousePressed() throws Exception {
		when(me.isShiftDown()).thenReturn(true);
		when(me.getX()).thenReturn(1);
		when(me.getY()).thenReturn(2);
		syntaxMouseListener.mousePressed(me);
		verify(caretObservable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.MOUSE, 1, 2, true)));
	}

	@SuppressWarnings("unchecked")
	private Matcher<SyntaxCaretEvent> isSyntaxCaretEvent(SyntaxCaretEventType type, int x, int y, boolean shift) {
		return allOf(hasProperty("type", equalTo(type)), hasProperty("x", equalTo(x)),
				hasProperty("y", equalTo(y)), hasProperty("withShift", equalTo(shift)));
	}

	@Test
	public void testMouseDragged() throws Exception {
		when(me.isShiftDown()).thenReturn(true);
		when(me.getX()).thenReturn(1);
		when(me.getY()).thenReturn(2);
		syntaxMouseListener.mouseDragged(me);
		verify(caretObservable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.MOUSE_SELECTION, 1, 2, true)));
	}

}
