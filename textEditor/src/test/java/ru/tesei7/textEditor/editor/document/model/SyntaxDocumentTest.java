package ru.tesei7.textEditor.editor.document.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxDocumentTest {
	@InjectMocks
	@Spy
	private SyntaxDocument syntaxDocument;
	@Mock
	private Line currentLine;
	@Mock
	private Line firstLine;
	@Mock
	private FrameObserverable frameObserverable;

	@Before
	public void setUp() {
		syntaxDocument.firstLine = firstLine;
		syntaxDocument.currentLine = currentLine;
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
	public void testSetFirstVisibleCol() throws Exception {
		doNothing().when(syntaxDocument).checkLastColNotEmpty();
		syntaxDocument.setFirstVisibleCol(-1);
		assertThat(syntaxDocument.firstVisibleCol, is(0));
		verify(frameObserverable).notifyListeners(argThat(isFrameEvent(FrameEventType.HORIZONTAL, 0)));
		verify(syntaxDocument).checkLastColNotEmpty();

		syntaxDocument.setFirstVisibleCol(12);
		assertThat(syntaxDocument.firstVisibleCol, is(12));
	}

	private Matcher<FrameEvent> isFrameEvent(FrameEventType type, int i) {
		return allOf(hasProperty("type", is(type)), hasProperty("value", is(i)));
	}

	@Test
	public void testGetMaxCols() throws Exception {
		syntaxDocument.setCols(80);
		when(firstLine.getNext()).thenReturn(firstLine, firstLine, null);

		when(firstLine.getLengthToPaint()).thenReturn(42, 44, 43);
		assertThat(syntaxDocument.getMaxCols(), is(80));

		syntaxDocument.setCols(40);
		when(firstLine.getLengthToPaint()).thenReturn(42, 44, 43);
		when(firstLine.getNext()).thenReturn(firstLine, firstLine, null);
		assertThat(syntaxDocument.getMaxCols(), is(44));
	}

	@Test
	public void testCheckLastColNotEmpty() throws Exception {
		syntaxDocument.setCols(80);
		doReturn(100).when(syntaxDocument).getMaxCols();
		syntaxDocument.firstVisibleCol = 0;

		syntaxDocument.checkLastColNotEmpty();
		assertThat(syntaxDocument.firstVisibleCol, is(0));

		syntaxDocument.firstVisibleCol = 21;
		syntaxDocument.checkLastColNotEmpty();
		assertThat(syntaxDocument.firstVisibleCol, is(20));

		syntaxDocument.firstVisibleCol = 20;
		syntaxDocument.checkLastColNotEmpty();
		assertThat(syntaxDocument.firstVisibleCol, is(20));

		syntaxDocument.firstVisibleCol = 2000;
		syntaxDocument.checkLastColNotEmpty();
		assertThat(syntaxDocument.firstVisibleCol, is(20));
	}

	@Test
	public void testSetFirstVisibleRow() throws Exception {
		doNothing().when(syntaxDocument).checkLastLinesNotEmpty();
		syntaxDocument.setFirstVisibleRow(-1);
		assertThat(syntaxDocument.firstVisibleRow, is(0));
		verify(frameObserverable).notifyListeners(argThat(isFrameEvent(FrameEventType.VERTICAL, 0)));
		verify(syntaxDocument).checkLastLinesNotEmpty();

		syntaxDocument.setFirstVisibleRow(12);
		assertThat(syntaxDocument.firstVisibleRow, is(12));
	}

	@Test
	public void testCheckLastLinesNotEmpty() throws Exception {
		syntaxDocument.setRows(40);
		doReturn(100).when(syntaxDocument).getSize();
		syntaxDocument.firstVisibleRow = 0;

		syntaxDocument.checkLastLinesNotEmpty();
		assertThat(syntaxDocument.firstVisibleRow, is(0));

		syntaxDocument.firstVisibleRow = 61;
		syntaxDocument.checkLastLinesNotEmpty();
		assertThat(syntaxDocument.firstVisibleRow, is(60));

		syntaxDocument.firstVisibleRow = 60;
		syntaxDocument.checkLastLinesNotEmpty();
		assertThat(syntaxDocument.firstVisibleRow, is(60));

		syntaxDocument.firstVisibleRow = 2000;
		syntaxDocument.checkLastLinesNotEmpty();
		assertThat(syntaxDocument.firstVisibleRow, is(60));
	}

	@Test
	@Ignore
	public void testGetCharsToShow() throws Exception {
		when(currentLine.getText()).thenReturn(new char[] {});
		syntaxDocument.firstVisibleCol = 0;
		assertTrue(Arrays.equals(new char[0], syntaxDocument.getLineCharsToShow(currentLine)));

		when(currentLine.getText()).thenReturn(new char[] { 'a' });
		assertTrue(Arrays.equals(new char[] { 'a' }, syntaxDocument.getLineCharsToShow(currentLine)));

		when(currentLine.getText()).thenReturn(new char[] { 'a', '\t' });
		assertTrue(
				Arrays.equals(new char[] { 'a', ' ', ' ', ' ', ' ' }, syntaxDocument.getLineCharsToShow(currentLine)));

		when(currentLine.getText()).thenReturn(new char[] { 'a', '\t' });
		syntaxDocument.firstVisibleCol = 1;
		assertTrue(Arrays.equals(new char[] { ' ', ' ', ' ', ' ' }, syntaxDocument.getLineCharsToShow(currentLine)));
	}
}
