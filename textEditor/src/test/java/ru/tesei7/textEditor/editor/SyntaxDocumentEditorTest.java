package ru.tesei7.textEditor.editor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SyntaxDocumentEditor.class)
public class SyntaxDocumentEditorTest {
	private SyntaxDocumentEditor syntaxDocumentEditor;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Line cline;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		syntaxDocumentEditor = spy(new SyntaxDocumentEditor(document));
		when(document.getCurrentLine()).thenReturn(cline);
	}

	@Test
	public final void testPrintChar() throws Exception {
		doNothing().when(syntaxDocumentEditor).addNewLine();
		syntaxDocumentEditor.printChar('\n');
		verify(syntaxDocumentEditor).addNewLine();

		syntaxDocumentEditor.printChar('a');
		verify(cline).printChar('a');
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
		when(cline.getLenght()).thenReturn(3);
		when(cline.getOffset()).thenReturn(3);
		when(cline.getNext()).thenReturn(l2);

		doNothing().when(syntaxDocumentEditor).concatLines(any(), any());
		syntaxDocumentEditor.delete();
		verify(syntaxDocumentEditor).concatLines(cline, l2);

		when(cline.getOffset()).thenReturn(2);
		syntaxDocumentEditor.delete();
		verify(cline).delete();
	}

	@Test
	public final void testBackspace() throws Exception {
		Line l2 = mock(Line.class);
		when(cline.getLenght()).thenReturn(3);
		when(cline.getPrevious()).thenReturn(l2);

		doNothing().when(syntaxDocumentEditor).concatLines(any(), any());
		syntaxDocumentEditor.backspace();
		verify(syntaxDocumentEditor).concatLines(l2, cline);

		when(cline.getOffset()).thenReturn(2);
		syntaxDocumentEditor.backspace();
		verify(cline).backspace();
	}

}
