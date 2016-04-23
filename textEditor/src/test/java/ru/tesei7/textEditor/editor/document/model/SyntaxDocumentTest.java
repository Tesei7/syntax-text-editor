package ru.tesei7.textEditor.editor.document.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.scroll.bar.FrameObserverable;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxDocumentTest {
	@InjectMocks
	private SyntaxDocument syntaxDocument;
	@Mock
	private Line currentLine;
	@Mock
	private Line firstLine;
	@Mock
	private Line firstVisibleLine;
	@Mock
	private FrameObserverable frameObserverable;

	@Before
	public void setUp() {
		syntaxDocument.firstLine = firstLine;
		syntaxDocument.currentLine = currentLine;
		syntaxDocument.firstVisibleLine = firstVisibleLine;
	}

	@Test
	public void testGetSize() throws Exception {
		assertThat(syntaxDocument.getSize(), is(1));

		when(firstLine.hasNext()).thenReturn(true, true, false);
		when(firstLine.getNext()).thenReturn(firstLine, firstLine, null);
		assertThat(syntaxDocument.getSize(), is(3));
	}

	@Test
	public void testGetLineIndex() throws Exception {
		assertThat(syntaxDocument.getLineIndex(firstLine), is(0));

		when(firstLine.hasNext()).thenReturn(true);
		when(firstLine.getNext()).thenReturn(firstLine, firstLine, currentLine);
		assertThat(syntaxDocument.getLineIndex(currentLine), is(3));
	}

	@Test
	public void testSetFirstVisibleLine() throws Exception {
		syntaxDocument.setFirstVisibleLine(firstLine);
		verify(frameObserverable).notifyListeners(argThat(hasProperty("firstVisibleLine", equalTo(firstLine))));
	}

	@Test
	public void testGetCurrentLineY() throws Exception {
		syntaxDocument.firstVisibleLine = currentLine;
		assertThat(syntaxDocument.getCurrentLineY(), is(0));

		syntaxDocument.firstVisibleLine = firstVisibleLine;
		when(firstVisibleLine.hasNext()).thenReturn(true, true);
		when(firstVisibleLine.getNext()).thenReturn(firstVisibleLine, currentLine);
		assertThat(syntaxDocument.getCurrentLineY(), is(2));

		when(firstVisibleLine.hasNext()).thenReturn(true, false);
		when(firstVisibleLine.getNext()).thenReturn(firstVisibleLine);
		assertThat(syntaxDocument.getCurrentLineY(), is(-1));
	}

	@Test
	public void testGetVisibleLines() throws Exception {
		assertThat(syntaxDocument.getVisibleLines(), contains(firstVisibleLine));

		when(firstVisibleLine.hasNext()).thenReturn(true, true, false);
		when(firstVisibleLine.getNext()).thenReturn(firstVisibleLine);
		assertThat(syntaxDocument.getVisibleLines(), contains(firstVisibleLine, firstVisibleLine, firstVisibleLine));

		syntaxDocument.setRows(2);
		when(firstVisibleLine.hasNext()).thenReturn(true);
		when(firstVisibleLine.getNext()).thenReturn(firstVisibleLine);
		assertThat(syntaxDocument.getVisibleLines(), contains(firstVisibleLine, firstVisibleLine));
	}

}
