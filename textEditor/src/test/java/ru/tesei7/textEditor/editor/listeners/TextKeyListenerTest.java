package ru.tesei7.textEditor.editor.listeners;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.DocumentEditEvent;
import ru.tesei7.textEditor.editor.document.DocumentEditEventType;
import ru.tesei7.textEditor.editor.document.DocumentEditObservable;

@RunWith(MockitoJUnitRunner.class)
public class TextKeyListenerTest {
	@InjectMocks
	@Spy
	private TextKeyListener textKeyListener;
	@Mock
	private DocumentEditObservable observable;
	@Mock
	private KeyEvent e;

	@Test
	public void testKeyTyped() throws Exception {
		doReturn(true).when(textKeyListener).isPrintableChar('a');
		when(e.isControlDown()).thenReturn(true);
		textKeyListener.keyTyped(e);
		verify(textKeyListener, never()).isPrintableChar('a');
		
		when(e.isControlDown()).thenReturn(false);
		when(e.getKeyChar()).thenReturn('a');
		textKeyListener.keyTyped(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.PRINT_CHAR)));
		
		when(e.getKeyChar()).thenReturn('\n');
		textKeyListener.keyTyped(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.NEW_LINE)));
	}

	@Test
	public void testIsPrintableChar() throws Exception {
		assertThat(textKeyListener.isPrintableChar('a'), is(true));
		assertThat(textKeyListener.isPrintableChar('\t'), is(false));
		assertThat(textKeyListener.isPrintableChar('\n'), is(false));
		assertThat(textKeyListener.isPrintableChar('\r'), is(false));
	}

	@Test
	public void testKeyPressed() throws Exception {
		when(e.getKeyCode()).thenReturn(KeyEvent.VK_BACK_SPACE);
		textKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.BACKSPACE)));

		when(e.getKeyCode()).thenReturn(KeyEvent.VK_DELETE);
		textKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.DELETE)));

		when(e.getKeyCode()).thenReturn(KeyEvent.VK_C);
		when(e.isControlDown()).thenReturn(true);
		textKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.COPY)));

		when(e.getKeyCode()).thenReturn(KeyEvent.VK_V);
		textKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.PASTE)));

		when(e.getKeyCode()).thenReturn(KeyEvent.VK_X);
		textKeyListener.keyPressed(e);
		verify(observable).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.COPY_REMOVE)));

		when(e.getKeyCode()).thenReturn(KeyEvent.VK_INSERT);
		when(e.isShiftDown()).thenReturn(true);
		textKeyListener.keyPressed(e);
		verify(observable, times(2)).notifyListeners(argThat(isDocumentEditEvent(DocumentEditEventType.PASTE)));
	}

	private Matcher<DocumentEditEvent> isDocumentEditEvent(DocumentEditEventType type) {
		return hasProperty("type", equalTo(type));
	}

}
