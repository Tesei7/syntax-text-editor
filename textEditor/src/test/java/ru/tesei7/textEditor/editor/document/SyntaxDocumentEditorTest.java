package ru.tesei7.textEditor.editor.document;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.document.model.TextSelection;
import ru.tesei7.textEditor.editor.scroll.DimensionsObservable;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxDocumentEditorTest {
	@InjectMocks
	@Spy
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
	@Mock
	private TextSelection selection;
	@Mock
	private DocumentEditEvent e;

	@Before
	public void setUp() throws Exception {
		when(document.getCurrentLine()).thenReturn(cline);
		when(document.getSelection()).thenReturn(selection);
	}

	@Test
	public final void testPrintCharCaret() throws Exception {
		when(document.getCaretType()).thenReturn(CaretType.NORMAL);
		syntaxDocumentEditor.printCharCaret('a');
		verify(cline).printChar('a');

		when(document.getCaretType()).thenReturn(CaretType.INSERT);
		syntaxDocumentEditor.printCharCaret('a');
		verify(cline).insertChar('a');
	}

	@Test
	public final void testAddNewLine() throws Exception {
		doNothing().when(syntaxDocumentEditor).addNewLineCaret();
		doNothing().when(syntaxDocumentEditor).addNewLineSelection();
		when(selection.notSelected()).thenReturn(false);
		syntaxDocumentEditor.addNewLine();
		verify(syntaxDocumentEditor).addNewLineSelection();
		when(selection.notSelected()).thenReturn(true);
		syntaxDocumentEditor.addNewLine();
		verify(syntaxDocumentEditor).addNewLineCaret();
	}

	@Test
	public final void testDelete() throws Exception {
		doNothing().when(syntaxDocumentEditor).deleteChar();
		doNothing().when(syntaxDocumentEditor).removeSelection();
		when(selection.notSelected()).thenReturn(false);
		syntaxDocumentEditor.delete();
		verify(syntaxDocumentEditor).removeSelection();
		when(selection.notSelected()).thenReturn(true);
		syntaxDocumentEditor.delete();
		verify(syntaxDocumentEditor).deleteChar();
	}

	@Test
	public final void testBackspace() throws Exception {
		doNothing().when(syntaxDocumentEditor).backspaceChar();
		doNothing().when(syntaxDocumentEditor).removeSelection();
		when(selection.notSelected()).thenReturn(false);
		syntaxDocumentEditor.backspace();
		verify(syntaxDocumentEditor).removeSelection();
		when(selection.notSelected()).thenReturn(true);
		syntaxDocumentEditor.backspace();
		verify(syntaxDocumentEditor).backspaceChar();
	}

	@Test
	public void testPrintChar() throws Exception {
		doNothing().when(syntaxDocumentEditor).printCharCaret('a');
		doNothing().when(syntaxDocumentEditor).printCharSelection('a');
		when(selection.notSelected()).thenReturn(false);
		syntaxDocumentEditor.printChar('a');
		verify(syntaxDocumentEditor).printCharSelection('a');
		when(selection.notSelected()).thenReturn(true);
		syntaxDocumentEditor.printChar('a');
		verify(syntaxDocumentEditor).printCharCaret('a');
	}

	@Test
	public void testPaste() throws Exception {
		doNothing().when(syntaxDocumentEditor).pasteCaret();
		doNothing().when(syntaxDocumentEditor).removeSelection();
		when(selection.notSelected()).thenReturn(false);
		syntaxDocumentEditor.paste();
		verify(syntaxDocumentEditor).removeSelection();
		verify(syntaxDocumentEditor).pasteCaret();
		when(selection.notSelected()).thenReturn(true);
		syntaxDocumentEditor.paste();
		verify(syntaxDocumentEditor).removeSelection();
		verify(syntaxDocumentEditor, times(2)).pasteCaret();
	}

	@Test
	public void testOnDocumentEdited() throws Exception {
		doNothing().when(syntaxDocumentEditor).backspace();
		when(e.getType()).thenReturn(DocumentEditEventType.BACKSPACE);
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor).backspace();
		verify(document).setDirty(true);

		doNothing().when(syntaxDocumentEditor).delete();
		when(e.getType()).thenReturn(DocumentEditEventType.DELETE);
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor).delete();

		doNothing().when(syntaxDocumentEditor).printChar('a');
		when(e.getType()).thenReturn(DocumentEditEventType.PRINT_CHAR);
		when(e.getChar()).thenReturn('a');
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor).printChar('a');

		doNothing().when(syntaxDocumentEditor).addNewLine();
		when(e.getType()).thenReturn(DocumentEditEventType.NEW_LINE);
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor).addNewLine();

		doNothing().when(syntaxDocumentEditor).copy();
		when(e.getType()).thenReturn(DocumentEditEventType.COPY);
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor).copy();

		doNothing().when(syntaxDocumentEditor).paste();
		when(e.getType()).thenReturn(DocumentEditEventType.PASTE);
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor).paste();

		doNothing().when(syntaxDocumentEditor).removeSelection();
		when(e.getType()).thenReturn(DocumentEditEventType.COPY_REMOVE);
		syntaxDocumentEditor.onDocumentEdited(e);
		verify(syntaxDocumentEditor, times(2)).copy();
		verify(syntaxDocumentEditor).removeSelection();
	}

	@Test
	public void testPrintCharSelection() throws Exception {
		syntaxDocumentEditor.printCharSelection('a');
		verify(document).removeSelection();
		verify(cline).printChar('a');
	}

	@Test
	public void testAddNewLineSelection() throws Exception {
		doNothing().when(syntaxDocumentEditor).addNewLineCaret();
		syntaxDocumentEditor.addNewLineSelection();
		verify(document).removeSelection();
		verify(syntaxDocumentEditor).addNewLineCaret();
	}

	@Test
	public void testDeleteChar() throws Exception {
		when(cline.getOffset()).thenReturn(3);
		when(cline.getLength()).thenReturn(33);
		syntaxDocumentEditor.deleteChar();
		verify(cline).delete();
		
		when(document.getCurLineIndex()).thenReturn(42);
		doNothing().when(syntaxDocumentEditor).concatLines(42, 43, false);
		when(cline.getOffset()).thenReturn(33);
		syntaxDocumentEditor.deleteChar();
		verify(syntaxDocumentEditor).concatLines(42, 43, false);
	}

	@Test
	public void testBackspaceChar() throws Exception {
		when(cline.getOffset()).thenReturn(42);
		syntaxDocumentEditor.backspaceChar();
		verify(cline).backspace();
		
		when(cline.getOffset()).thenReturn(0);
		when(document.getCurLineIndex()).thenReturn(42);
		doNothing().when(syntaxDocumentEditor).concatLines(41, 42, true);
		syntaxDocumentEditor.backspaceChar();
		verify(syntaxDocumentEditor).concatLines(41, 42, true);
	}

	@Test
	public void testRemoveSelection() throws Exception {
		syntaxDocumentEditor.removeSelection();
		verify(document).removeSelection();
	}

}
