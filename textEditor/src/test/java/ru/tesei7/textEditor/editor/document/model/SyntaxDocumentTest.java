package ru.tesei7.textEditor.editor.document.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
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
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.scroll.bar.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.bar.FrameObserverable;

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
		syntaxDocument.setFirstVisibleCol(12);
		verify(frameObserverable).notifyListeners(argThat(allOf(
//				hasProperty("type", equalTo(FrameEventType.VERTICAL)), 
				hasProperty("value", is(12)))
				));
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

}
