package ru.tesei7.textEditor.editor.document.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TextSelectionTest {
	@InjectMocks
	@Spy
	private TextSelection textSelection;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Line l;

	@Test
	public final void testSetStartLine() throws Exception {
		when(document.getCorrectLineIndex(2)).thenReturn(2);
		textSelection.setStartLine(2);
		assertThat(textSelection._getStartLine(), is(2));
	}

	@Test
	public final void testSetEndLine() throws Exception {
		when(document.getCorrectLineIndex(2)).thenReturn(2);
		textSelection.setEndLine(2);
		assertThat(textSelection._getEndLine(), is(2));
	}

	@Test
	public final void testGetStartOffsetToPaint() throws Exception {
		textSelection.setStartOffset(1);
		when(document.getXToPaint(l, 1)).thenReturn(2);
		assertThat(textSelection.getStartOffsetToPaint(l), is(2));
	}

	@Test
	public final void testGetEndOffsetToPaint() throws Exception {
		textSelection.setEndOffset(1);
		when(document.getXToPaint(l, 1)).thenReturn(2);
		assertThat(textSelection.getEndOffsetToPaint(l), is(2));
	}

	@Test
	public final void testGetLineFrom() throws Exception {
		textSelection._setStartLine(1);
		textSelection._setEndLine(2);
		doReturn(true).when(textSelection).isReversedLines();
		assertThat(textSelection.getLineFrom(), is(2));
		doReturn(false).when(textSelection).isReversedLines();
		assertThat(textSelection.getLineFrom(), is(1));
	}

	@Test
	public final void testGetLineTo() throws Exception {
		textSelection._setStartLine(1);
		textSelection._setEndLine(2);
		doReturn(true).when(textSelection).isReversedLines();
		assertThat(textSelection.getLineTo(), is(1));
		doReturn(false).when(textSelection).isReversedLines();
		assertThat(textSelection.getLineTo(), is(2));
	}

	@Test
	public final void testGetOffsetFrom() throws Exception {
		textSelection.setStartOffset(1);
		textSelection.setEndOffset(2);
		doReturn(true).when(textSelection).isReversed();
		assertThat(textSelection.getOffsetFrom(), is(2));
		doReturn(false).when(textSelection).isReversed();
		assertThat(textSelection.getOffsetFrom(), is(1));
	}

	@Test
	public final void testGetOffsetTo() throws Exception {
		textSelection.setStartOffset(1);
		textSelection.setEndOffset(2);
		doReturn(true).when(textSelection).isReversed();
		assertThat(textSelection.getOffsetTo(), is(1));
		doReturn(false).when(textSelection).isReversed();
		assertThat(textSelection.getOffsetTo(), is(2));
	}

	@Test
	public final void testGetOffsetToPaintFrom() throws Exception {
		doReturn(1).when(textSelection).getStartOffsetToPaint(l);
		doReturn(2).when(textSelection).getEndOffsetToPaint(l);
		doReturn(true).when(textSelection).isReversedOffset(l);
		assertThat(textSelection.getOffsetToPaintFrom(l), is(2));
		doReturn(false).when(textSelection).isReversedOffset(l);
		assertThat(textSelection.getOffsetToPaintFrom(l), is(1));
	}

	@Test
	public final void testGetOffsetToPaintTo() throws Exception {
		doReturn(1).when(textSelection).getStartOffsetToPaint(l);
		doReturn(2).when(textSelection).getEndOffsetToPaint(l);
		doReturn(true).when(textSelection).isReversedOffset(l);
		assertThat(textSelection.getOffsetToPaintTo(l), is(1));
		doReturn(false).when(textSelection).isReversedOffset(l);
		assertThat(textSelection.getOffsetToPaintTo(l), is(2));
	}

	@Test
	public final void testIsReversedLines() throws Exception {
		textSelection._setStartLine(1);
		textSelection._setEndLine(2);
		assertThat(textSelection.isReversedLines(), is(false));
		textSelection._setStartLine(2);
		assertThat(textSelection.isReversedLines(), is(false));
		textSelection._setStartLine(3);
		assertThat(textSelection.isReversedLines(), is(false));
		
		doReturn(true).when(textSelection).notSelected();
		assertThat(textSelection.isReversedLines(), is(false));
	}

	@Test
	public final void testIsReversedOffset() throws Exception {
		doReturn(false).when(textSelection).notSelected();
		doReturn(true).when(textSelection).isReversedLines();
		assertThat(textSelection.isReversedOffset(l), is(true));
		
		textSelection._setStartLine(1);
		textSelection._setEndLine(2);
		doReturn(1).when(textSelection).getStartOffsetToPaint(l);
		doReturn(2).when(textSelection).getEndOffsetToPaint(l);
		doReturn(false).when(textSelection).isReversedLines();
		assertThat(textSelection.isReversedOffset(l), is(false));
		
		textSelection._setStartLine(2);
		assertThat(textSelection.isReversedOffset(l), is(false));
		
		doReturn(2).when(textSelection).getStartOffsetToPaint(l);
		assertThat(textSelection.isReversedOffset(l), is(false));
		
		doReturn(3).when(textSelection).getStartOffsetToPaint(l);
		assertThat(textSelection.isReversedOffset(l), is(true));
		
		doReturn(true).when(textSelection).notSelected();
		assertThat(textSelection.isReversedOffset(l), is(false));
	}

	@Test
	public final void testIsReversed() throws Exception {
		doReturn(true).when(textSelection).isReversedLines();
		assertThat(textSelection.isReversed(), is(true));
		
		doReturn(false).when(textSelection).isReversedLines();
		textSelection._setStartLine(1);
		textSelection._setEndLine(2);
		textSelection.setStartOffset(1);
		textSelection.setEndOffset(2);
		assertThat(textSelection.isReversed(), is(false));
		
		textSelection._setStartLine(2);
		assertThat(textSelection.isReversed(), is(false));
		
		textSelection.setStartOffset(3);
		assertThat(textSelection.isReversed(), is(true));
	}

	@Test
	public final void testNotSelected() throws Exception {
		textSelection._setStartLine(null);
		textSelection._setEndLine(null);
		textSelection.setStartOffset(null);
		textSelection.setEndOffset(null);
		assertThat(textSelection.notSelected(), is(true));
		
		textSelection._setStartLine(1);
		textSelection._setEndLine(2);
		textSelection.setStartOffset(1);
		textSelection.setEndOffset(2);
		assertThat(textSelection.notSelected(), is(false));
	}

}
