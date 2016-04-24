package ru.tesei7.textEditor.editor.frame;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxTextEditorFrame;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxTextEditorFrameTest {
	@InjectMocks
	@Spy
	private SyntaxTextEditorFrame syntaxTextEditorScroller;
	@Mock
	private SyntaxDocument document;
	@Mock
	private SyntaxScrollEvent scrollEvent;
	@Mock
	private Line fline;

	@Before
	public void setUp() {
		when(document.getFirstVisibleLine()).thenReturn(fline);
	}

	@Test
	@Ignore
	public void testScrollVerical() throws Exception {
	}

	@Test
	@Ignore
	public void testScrollHorizontal() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	@Ignore
	public void testOnCaretChanged() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testOnScrollChanged() throws Exception {
		when(scrollEvent.getDirection()).thenReturn(Direction.VERTICAL);
		when(scrollEvent.getValue()).thenReturn(12);
		syntaxTextEditorScroller.onScrollChanged(scrollEvent);
		verify(document).setFirstVisibleRow(12);

		when(scrollEvent.getDirection()).thenReturn(Direction.HORIZONTAL);
		syntaxTextEditorScroller.onScrollChanged(scrollEvent);
		verify(document).setFirstVisibleRow(12);
	}

}
