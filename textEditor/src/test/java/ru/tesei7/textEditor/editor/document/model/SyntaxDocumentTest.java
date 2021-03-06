package ru.tesei7.textEditor.editor.document.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.dirtyState.DirtyStateObservable;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObservable;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxDocumentTest {
	@InjectMocks
	@Spy
	private SyntaxDocument syntaxDocument;
	@Mock
	private FrameObservable frameObservable;
	@Mock
	private List<Line> lines;
	@Mock
	private Line line;
	@Mock
	private DirtyStateObservable dirtyObservable;

	@Before
	public void setUp() {
		syntaxDocument.lines = lines;
	}

	@Test
	public void testGetSize() throws Exception {
		when(lines.size()).thenReturn(1);
		assertThat(syntaxDocument.getSize(), is(1));
	}

	@Test
	public void testSetFirstVisibleCol() throws Exception {
		doNothing().when(syntaxDocument).checkLastColNotEmpty();
		syntaxDocument.setFirstVisibleCol(-1);
		assertThat(syntaxDocument.firstVisibleCol, is(0));
		verify(frameObservable).notifyListeners(argThat(isFrameEvent(FrameEventType.HORIZONTAL, 0)));
		verify(syntaxDocument).checkLastColNotEmpty();

		syntaxDocument.setFirstVisibleCol(12);
		assertThat(syntaxDocument.firstVisibleCol, is(12));
	}

	private Matcher<FrameEvent> isFrameEvent(FrameEventType type, int i) {
		return allOf(hasProperty("type", is(type)), hasProperty("value", is(i)));
	}

	@Test
	@Ignore
	public void testGetMaxCols() throws Exception {
		syntaxDocument.lines = new ArrayList<>();
		Line l1 = mock(Line.class), l2 = mock(Line.class);
		lines.add(l1);
		lines.add(l2);
		syntaxDocument.cols = 100;
		when(l1.getLengthToPaint()).thenReturn(20);
		when(l2.getLengthToPaint()).thenReturn(120);
		assertThat(syntaxDocument.getMaxCols(), is(120));

		syntaxDocument.cols = 121;
		assertThat(syntaxDocument.getMaxCols(), is(121));
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
		verify(frameObservable).notifyListeners(argThat(isFrameEvent(FrameEventType.VERTICAL, 0)));
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
		Line currentLine = mock(Line.class);
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

	@Test
	public void testGetCorrectLineIndex() throws Exception {
		when(lines.size()).thenReturn(12);
		assertThat(syntaxDocument.getCorrectLineIndex(-1), is(0));
		assertThat(syntaxDocument.getCorrectLineIndex(0), is(0));
		assertThat(syntaxDocument.getCorrectLineIndex(11), is(11));
		assertThat(syntaxDocument.getCorrectLineIndex(12), is(11));
		when(lines.size()).thenReturn(0);
		assertThat(syntaxDocument.getCorrectLineIndex(0), is(0));
		assertThat(syntaxDocument.getCorrectLineIndex(-1), is(0));
		assertThat(syntaxDocument.getCorrectLineIndex(12), is(0));
	}

	@Test
	public void testIsCorrectLineIndex() throws Exception {
		when(lines.size()).thenReturn(12);
		assertThat(syntaxDocument.isCorrectLineIndex(-1), is(false));
		assertThat(syntaxDocument.isCorrectLineIndex(0), is(true));
		assertThat(syntaxDocument.isCorrectLineIndex(11), is(true));
		assertThat(syntaxDocument.isCorrectLineIndex(12), is(false));
		when(lines.size()).thenReturn(0);
		assertThat(syntaxDocument.isCorrectLineIndex(0), is(false));
		assertThat(syntaxDocument.isCorrectLineIndex(-1), is(false));
		assertThat(syntaxDocument.isCorrectLineIndex(12), is(false));
	}

	@Test
	public void testSetCurLineIndex() throws Exception {
		doReturn(1).when(syntaxDocument).getCorrectLineIndex(1);
		syntaxDocument.setCurLineIndex(1);
		assertThat(syntaxDocument.getCurLineIndex(), is(1));
	}

	@Test
	public void testMoveCurLineIndex() throws Exception {
		syntaxDocument.curLineIndex = 2;
		syntaxDocument.moveCurLineIndex(1);
		verify(syntaxDocument).setCurLineIndex(3);
	}

	@Test
	public void testGetCurLineIndexToPaint() throws Exception {
		syntaxDocument.curLineIndex = 2;
		syntaxDocument.firstVisibleRow = 3;
		syntaxDocument.rows = 40;
		assertThat(syntaxDocument.getCurLineIndexToPaint(), is(-1));
		syntaxDocument.curLineIndex = 5;
		assertThat(syntaxDocument.getCurLineIndexToPaint(), is(2));
		syntaxDocument.curLineIndex = 44;
		assertThat(syntaxDocument.getCurLineIndexToPaint(), is(-2));
	}

	@Test
	public void testGetLineByIndex() throws Exception {
		doReturn(3).when(syntaxDocument).getCorrectLineIndex(1);
		when(lines.get(3)).thenReturn(line);
		assertEquals(line, syntaxDocument.getLineByIndex(1));
	}

	@Test
	public void testAddLineAfter() throws Exception {
		syntaxDocument.addLineAfter(1, line);
		verify(lines).add(2, line);
	}

	@Test
	public void testRemoveLineAfter() throws Exception {
		syntaxDocument.removeLineAfter(1);
		verify(lines).remove(2);
	}

	@Test
	public void testGetText() throws Exception {
		ArrayList<Line> list = new ArrayList<>();
		Line l1 = mock(Line.class);
		Line l2 = mock(Line.class);
		list.add(l1);
		list.add(l2);
		syntaxDocument.lines = list;

		when(l1.getText()).thenReturn(new char[] { 'a', 'b', '\t' });
		when(l2.getText()).thenReturn(new char[] { '\r', 'c', 'x' });
		assertThat(syntaxDocument.getText(), is("ab\t\n\rcx"));
	}

	@Test
	public void testSetDirty() throws Exception {
		syntaxDocument.setDirty(true);
		assertThat(syntaxDocument.isDirty(), is(true));
		verify(dirtyObservable).notifyListeners(any());
	}

	@Test
	public void testGetXToPaint() throws Exception {
		doReturn(line).when(syntaxDocument).getCurrentLine();
		when(line.getOffsetToPaint()).thenReturn(12);
		syntaxDocument.firstVisibleCol = 10;
		assertThat(syntaxDocument.getXToPaint(), is(2));
	}

	@Test
	public void testGetXToPaint2() throws Exception {
		doReturn(line).when(syntaxDocument).getCurrentLine();
		when(line.getOffsetToPaint(1)).thenReturn(12);
		syntaxDocument.firstVisibleCol = 10;
		assertThat(syntaxDocument.getXToPaint(line, 1), is(2));
	}
}
