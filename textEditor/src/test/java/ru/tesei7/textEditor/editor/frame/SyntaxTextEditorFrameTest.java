package ru.tesei7.textEditor.editor.frame;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxTextEditorFrameTest {
	@InjectMocks
	@Spy
	private SyntaxTextEditorFrame syntaxTextEditorFrame;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Line l;
	@Mock
	private SyntaxScrollEvent scrollEvent;
	@Mock
	private Line line;

	@Before
	public void setUp() {
		when(document.getFirstVisibleLine()).thenReturn(line);
	}

	@Test
	public void testOnCaretChanged() throws Exception {
		doNothing().when(syntaxTextEditorFrame).makeCaretVisibleX();
		doNothing().when(syntaxTextEditorFrame).makeCaretVisibleY();
		syntaxTextEditorFrame.onCaretChanged(any());
		verify(syntaxTextEditorFrame).makeCaretVisibleX();
		verify(syntaxTextEditorFrame).makeCaretVisibleY();
	}

	@Test
	public void testOnScrollChanged() throws Exception {
		when(scrollEvent.getDirection()).thenReturn(Direction.VERTICAL);
		when(scrollEvent.getValue()).thenReturn(12);
		syntaxTextEditorFrame.onScrollChanged(scrollEvent);
		verify(document).setFirstVisibleRow(12);

		when(scrollEvent.getDirection()).thenReturn(Direction.VERTICAL_ADD);
		when(scrollEvent.getValue()).thenReturn(12);
		when(document.getFirstVisibleRow()).thenReturn(21);
		syntaxTextEditorFrame.onScrollChanged(scrollEvent);
		verify(document).setFirstVisibleRow(33);

		when(scrollEvent.getDirection()).thenReturn(Direction.HORIZONTAL);
		syntaxTextEditorFrame.onScrollChanged(scrollEvent);
		verify(document).setFirstVisibleRow(12);
	}

	@Test
	public void testMakeCaretVisibleX() throws Exception {
		when(document.getCurrentLine()).thenReturn(l);
		
		when(document.getCols()).thenReturn(80);
		when(document.getFirstVisibleCol()).thenReturn(1);
		when(l.getOffsetToPaint()).thenReturn(4);
		
		syntaxTextEditorFrame.makeCaretVisibleX();
		verify(document).checkLastColNotEmpty();
		
		when(document.getFirstVisibleCol()).thenReturn(100);
		syntaxTextEditorFrame.makeCaretVisibleX();
		verify(document).setFirstVisibleCol(4);
		
		when(document.getFirstVisibleCol()).thenReturn(120);
		when(l.getOffsetToPaint()).thenReturn(205);
		syntaxTextEditorFrame.makeCaretVisibleX();
		verify(document).setFirstVisibleCol(125);
	}

	@Test
	public void testMakeCaretVisibleY() throws Exception {
		when(document.getFirstVisibleRow()).thenReturn(1);
		when(document.getCurLineIndex()).thenReturn(5);
		when(document.getRows()).thenReturn(80);
		
		syntaxTextEditorFrame.makeCaretVisibleY();
		verify(document).checkLastLinesNotEmpty();
		
		when(document.getFirstVisibleRow()).thenReturn(6);
		syntaxTextEditorFrame.makeCaretVisibleY();
		verify(document).setFirstVisibleRow(5);
		
		when(document.getCurLineIndex()).thenReturn(86);
		syntaxTextEditorFrame.makeCaretVisibleY();
		verify(document).setFirstVisibleRow(7);
	}
	
}
