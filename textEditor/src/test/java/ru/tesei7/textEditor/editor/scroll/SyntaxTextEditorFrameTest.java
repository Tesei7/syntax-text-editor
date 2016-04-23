package ru.tesei7.textEditor.editor.scroll;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.AdjustmentEvent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxTextEditorFrameTest {
	@InjectMocks
	@Spy
	private SyntaxTextEditorFrame syntaxTextEditorScroller;
	@Mock
	private SyntaxDocument document;
	@Mock
	private SyntaxTextEditor editor;
	@Mock
	private SyntaxScrollEvent scrollEvent;
	@Mock
	private Line fline;

	@Before
	public void setUp() {
		syntaxTextEditorScroller.document = document;
		when(document.getFirstVisibleLine()).thenReturn(fline);
	}

	@Test
	public void testScrollVerical() throws Exception {
		doNothing().when(syntaxTextEditorScroller).scrollVericalAbsolut(12);
		doNothing().when(syntaxTextEditorScroller).scrollVericalReletive(1);
		doNothing().when(syntaxTextEditorScroller).scrollVericalReletive(-40);

		syntaxTextEditorScroller.scrollVerical(AdjustmentEvent.TRACK, 12);
		verify(syntaxTextEditorScroller).scrollVericalAbsolut(12);

		syntaxTextEditorScroller.scrollVerical(AdjustmentEvent.UNIT_INCREMENT, 122);
		verify(syntaxTextEditorScroller).scrollVericalReletive(1);

		when(editor.getRows()).thenReturn(40);
		syntaxTextEditorScroller.scrollVerical(AdjustmentEvent.BLOCK_DECREMENT, 122);
		verify(syntaxTextEditorScroller).scrollVericalReletive(-40);
	}

	@Test
	public void scrollVericalReletiveTest() throws Exception {
		Line l1 = mock(Line.class);
		Line l2 = mock(Line.class);

		syntaxTextEditorScroller.scrollVericalReletive(-1);
		verify(document, never()).setFirstVisibleLine(l1);

		syntaxTextEditorScroller.scrollVericalReletive(1);
		verify(document, never()).setFirstVisibleLine(l2);

		when(fline.getNext()).thenReturn(l1);
		when(fline.hasNext()).thenReturn(true);
		when(fline.getPrevious()).thenReturn(l2);
		when(fline.hasPrevious()).thenReturn(true);

		syntaxTextEditorScroller.scrollVericalReletive(1);
		verify(document).setFirstVisibleLine(l1);

		syntaxTextEditorScroller.scrollVericalReletive(-1);
		verify(document).setFirstVisibleLine(l2);
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
		doNothing().when(syntaxTextEditorScroller).scrollVerical(AdjustmentEvent.UNIT_INCREMENT, 12);
		doNothing().when(syntaxTextEditorScroller).scrollHorizontal(AdjustmentEvent.BLOCK_DECREMENT, 12);

		when(scrollEvent.getDirection()).thenReturn(Direction.VERTICAL);
		when(scrollEvent.getAbsolut()).thenReturn(12);
		when(scrollEvent.getAdjustmentType()).thenReturn(AdjustmentEvent.UNIT_INCREMENT);
		syntaxTextEditorScroller.onScrollChanged(scrollEvent);
		verify(syntaxTextEditorScroller).scrollVerical(AdjustmentEvent.UNIT_INCREMENT, 12);

		when(scrollEvent.getDirection()).thenReturn(Direction.HORIZONTAL);
		when(scrollEvent.getAdjustmentType()).thenReturn(AdjustmentEvent.BLOCK_DECREMENT);
		syntaxTextEditorScroller.onScrollChanged(scrollEvent);
		verify(syntaxTextEditorScroller).scrollHorizontal(AdjustmentEvent.BLOCK_DECREMENT, 12);
	}

}
