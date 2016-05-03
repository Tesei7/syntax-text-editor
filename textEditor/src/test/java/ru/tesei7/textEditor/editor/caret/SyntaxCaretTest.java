package ru.tesei7.textEditor.editor.caret;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxCaretTest {
	@InjectMocks
	@Spy
	private SyntaxCaret syntaxCaret;
	@Mock
	private SyntaxDocument document;
	@Mock
	private FontProperties fontProperties;
	@Mock
	private SyntaxCaretEvent e;
	@Mock
	private Line line;

	@Test
	public void testOnCaretChanged() throws Exception {
		doNothing().when(syntaxCaret).doSelectBefore(e);
		doNothing().when(syntaxCaret).doSelectAfter(e);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret, never()).doSelectBefore(e);

		when(e.getType()).thenReturn(SyntaxCaretEventType.LEFT);
		doReturn(5).when(syntaxCaret).getX();
		doNothing().when(syntaxCaret).setX(4);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).setX(4);

		when(e.getType()).thenReturn(SyntaxCaretEventType.RIGHT);
		doReturn(5).when(syntaxCaret).getX();
		doNothing().when(syntaxCaret).setX(6);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).setX(6);

		when(e.getType()).thenReturn(SyntaxCaretEventType.UP);
		doNothing().when(syntaxCaret).moveY(-1);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).moveY(-1);

		when(e.getType()).thenReturn(SyntaxCaretEventType.DOWN);
		doNothing().when(syntaxCaret).moveY(1);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).moveY(1);
		
		when(document.getRows()).thenReturn(80);
		when(e.getType()).thenReturn(SyntaxCaretEventType.PAGE_UP);
		doNothing().when(syntaxCaret).moveY(-80);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).moveY(-1);

		when(e.getType()).thenReturn(SyntaxCaretEventType.PAGE_DOWN);
		doNothing().when(syntaxCaret).moveY(80);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).moveY(1);
		
		when(e.getType()).thenReturn(SyntaxCaretEventType.HOME);
		doNothing().when(syntaxCaret).setX(0);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).setX(0);
		
		when(e.getType()).thenReturn(SyntaxCaretEventType.END);
		doReturn(line).when(syntaxCaret).getCurrentLine();
		when(line.getLength()).thenReturn(10);
		doNothing().when(syntaxCaret).setX(10);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).setX(10);
		
		when(e.getType()).thenReturn(SyntaxCaretEventType.INSERT);
		when(e.isWithShift()).thenReturn(false);
		when(document.getCaretType()).thenReturn(CaretType.NORMAL);
		syntaxCaret.onCaretChanged(e);
		verify(document).setCaretType(CaretType.INSERT);
		
		when(document.getCaretType()).thenReturn(CaretType.INSERT);
		syntaxCaret.onCaretChanged(e);
		verify(document).setCaretType(CaretType.NORMAL);
		
		when(e.getX()).thenReturn(1);
		when(e.getY()).thenReturn(2);
		when(e.getType()).thenReturn(SyntaxCaretEventType.MOUSE);
		doNothing().when(syntaxCaret).setCaret(1, 2, false);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).setCaret(1, 2, false);
		
		when(e.getType()).thenReturn(SyntaxCaretEventType.MOUSE_SELECTION);
		doNothing().when(syntaxCaret).setSelection(1, 2);
		syntaxCaret.onCaretChanged(e);
		verify(syntaxCaret).setSelection(1, 2);
	}

}
