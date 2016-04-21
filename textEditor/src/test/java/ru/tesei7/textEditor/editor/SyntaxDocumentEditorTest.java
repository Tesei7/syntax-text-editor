package ru.tesei7.textEditor.editor;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxDocumentEditorTest {
	@InjectMocks
	@Spy
	private SyntaxDocumentEditor syntaxDocumentEditor;
	@Mock
	private SyntaxDocument document;
	@Mock
	private LineEditor lineEditor;
	@Mock
	private Line cline;

	@Before
	public void setUp() {
		when(document.getCurrentLine()).thenReturn(cline);
	}

	@Test
	public final void testPrintChar() throws Exception {
		doNothing().when(syntaxDocumentEditor).addNewLine();
		syntaxDocumentEditor.printChar('\n');
		verify(syntaxDocumentEditor).addNewLine();

		syntaxDocumentEditor.printChar('a');
		verify(lineEditor).printChar('a', cline);
	}

	@Test
	public final void testAddNewLine() throws Exception {
		Line l = new Line();
		l.setText(Arrays.asList('1', '2', '3'));
		l.setOffset(2);
		when(document.getCurrentLine()).thenReturn(l);

		syntaxDocumentEditor.addNewLine();
		Assertions.assertThat(l.getText()).containsExactly('1', '2');
		verify(document)
				.setCurrentLine(argThat(allOf(hasProperty("offset", equalTo(0)), hasProperty("text", hasItem('3')))));
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
		verify(lineEditor).delete(cline);
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
		verify(lineEditor).backspace(cline);
	}

}