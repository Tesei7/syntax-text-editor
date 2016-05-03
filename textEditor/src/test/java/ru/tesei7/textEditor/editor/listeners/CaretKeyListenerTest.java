package ru.tesei7.textEditor.editor.listeners;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEventType;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;

@RunWith(MockitoJUnitRunner.class)
public class CaretKeyListenerTest {
	@InjectMocks
	private CaretKeyListener caretKeyListener;
	@Mock
	private SyntaxCaretObservable observable;
	@Mock
	private KeyEvent e;

	@Test
	public void testKeyPressed() throws Exception {
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.LEFT, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.RIGHT, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_UP);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.UP, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.DOWN, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_HOME);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.HOME, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_END);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.END, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_PAGE_UP);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.PAGE_UP, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_PAGE_DOWN);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.PAGE_DOWN, false)));
		
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);
		caretKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.INSERT, false)));
		
		when(e.isShiftDown()).thenReturn(true);
		caretKeyListener.keyPressed(e);
		verify(observable, never()).notifyListeners(argThat(isSyntaxCaretEvent(SyntaxCaretEventType.INSERT, true)));
	}

	@SuppressWarnings("unchecked")
	private Matcher<SyntaxCaretEvent> isSyntaxCaretEvent(SyntaxCaretEventType type, boolean shift) {
		return allOf(hasProperty("type", equalTo(type)), hasProperty("withShift", equalTo(shift)));
	}

}
