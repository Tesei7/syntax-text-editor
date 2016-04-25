package ru.tesei7.textEditor.editor.document;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.scroll.DimensionsObservable;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SyntaxDocumentEditor.class)
public class SyntaxDocumentEditorTest {
	private SyntaxDocumentEditor syntaxDocumentEditor;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Line cline;
	@Mock
	private SyntaxCaretObservable syntaxCaretObservable;
	@Mock
	private DimensionsObservable dimentionsObservable;
	@Mock
	private FrameObserverable frameObserverable;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		syntaxDocumentEditor = spy(new SyntaxDocumentEditor(document, syntaxCaretObservable, dimentionsObservable));
		when(document.getCurrentLine()).thenReturn(cline);
	}

	@Test
	public final void testPrintChar() throws Exception {
		when(document.getCaretType()).thenReturn(CaretType.NORMAL);
		syntaxDocumentEditor.printChar('a');
		verify(cline).printChar('a');
		
		when(document.getCaretType()).thenReturn(CaretType.INSERT);
		syntaxDocumentEditor.printChar('a');
		verify(cline).insertChar('a');
	}

	@Test
	public final void testAddNewLine() throws Exception {
		// Line l = new Line();
		// l.setText(new char[] { '1', '2', '3' });
		// l.setOffset(2);
		// when(document.getCurrentLine()).thenReturn(l);
		//
		// syntaxDocumentEditor.addNewLine();
		// assertThat(l.getChars()).containsExactly('1', '2');
		// verify(document)
		// .setCurrentLine(argThat(allOf(hasProperty("offset", equalTo(0)),
		// hasProperty("text", hasItem('3')))));
	}

	@Test
	public final void testDelete() throws Exception {
		Line l2 = mock(Line.class);
		when(cline.getLength()).thenReturn(3);
		when(cline.getOffset()).thenReturn(3);
		when(cline.getNext()).thenReturn(l2);

		doNothing().when(syntaxDocumentEditor).concatLines(any(), any(), eq(false));
		syntaxDocumentEditor.delete();
		verify(syntaxDocumentEditor).concatLines(cline, l2, false);

		when(cline.getOffset()).thenReturn(2);
		syntaxDocumentEditor.delete();
		verify(cline).delete();
	}

	@Test
	public final void testBackspace() throws Exception {
		Line l2 = mock(Line.class);
		when(cline.getLength()).thenReturn(3);
		when(cline.getPrevious()).thenReturn(l2);

		doNothing().when(syntaxDocumentEditor).concatLines(any(), any(), eq(true));
		syntaxDocumentEditor.backspace();
		verify(syntaxDocumentEditor).concatLines(l2, cline, true);

		when(cline.getOffset()).thenReturn(2);
		syntaxDocumentEditor.backspace();
		verify(cline).backspace();
	}

}
