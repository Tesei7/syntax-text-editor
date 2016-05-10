package ru.tesei7.textEditor.editor.painter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.FontMetrics;
import java.awt.Graphics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.CaretType;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.utils.Colors;

@RunWith(MockitoJUnitRunner.class)
public class CaretPainterTest {
	@InjectMocks
	@Spy
	private CaretPainter caretPainter;
	@Mock
	private BracketService bracketPositionService;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Graphics g;
	@Mock
	private FontMetrics fm;
	@Mock
	private Line line;

	@Before
	public void setUp() {
		when(g.getFontMetrics()).thenReturn(fm);
		when(fm.stringWidth("a")).thenReturn(5);
		when(fm.getHeight()).thenReturn(10);
		when(document.getXToPaint()).thenReturn(1);
	}

	@Test
	public void testPaintCaret() throws Exception {
		caretPainter.paintCaret(g, false);
		verify(g, never()).fillRect(0, 1, 2, 3);

		when(document.getCurLineIndexToPaint()).thenReturn(2);
		when(document.getCaretType()).thenReturn(CaretType.NORMAL);
		caretPainter.paintCaret(g, true);
		verify(g).fillRect(5, 20, SyntaxTextEditor.CARET_WIDTH, 10);

		when(document.getCaretType()).thenReturn(CaretType.INSERT);
		caretPainter.paintCaret(g, true);
		verify(g).fillRect(5, 20, 5, 10);
	}

	@Test
	public void testPaintBackground() throws Exception {
		when(document.getCurLineIndexToPaint()).thenReturn(2);
		when(document.getCols()).thenReturn(80);
		caretPainter.paintBackground(g);
		verify(g).fillRect(0, 20, 81 * 5, 10);
	}

	@Test
	public void testHighlightBrackets() throws Exception {
		doNothing().when(caretPainter).highlightBracket(g, 0, 0, 0, Colors.FOUND_BRACKET_BACKGROUND);
		doNothing().when(caretPainter).highlightBracket(g, 0, 0, 0, Colors.NOT_FOUND_BRACKET_BACKGROUND);

		when(document.getLanguage()).thenReturn(Language.PLAIN_TEXT);
		caretPainter.highlightBrackets(g);
		verify(caretPainter, never()).highlightBracket(g, 0, 0, 0, Colors.FOUND_BRACKET_BACKGROUND);

		when(document.getLanguage()).thenReturn(Language.JAVA);
		caretPainter.bracketPositionService = bracketPositionService;
		when(bracketPositionService.getBracketAtCurrentPosition()).thenReturn(-1);
		caretPainter.highlightBrackets(g);
		verify(caretPainter, never()).highlightBracket(g, 0, 0, 0, Colors.FOUND_BRACKET_BACKGROUND);

		when(bracketPositionService.getBracketAtCurrentPosition()).thenReturn(0);
		when(bracketPositionService.getRelativeBracket()).thenReturn(null);
		when(document.getCurLineIndexToPaint()).thenReturn(2);
		when(document.getCurrentLine()).thenReturn(line);
		when(line.getOffsetToPaint()).thenReturn(3);
		when(document.getFirstVisibleCol()).thenReturn(1);
		caretPainter.highlightBrackets(g);
		verify(caretPainter).highlightBracket(g, 2, 2, 0, Colors.NOT_FOUND_BRACKET_BACKGROUND);

		when(bracketPositionService.getRelativeBracket()).thenReturn(new int[] { 4, 5 });
		caretPainter.highlightBrackets(g);
		verify(caretPainter).highlightBracket(g, 2, 2, 0, Colors.FOUND_BRACKET_BACKGROUND);
		verify(caretPainter).highlightBracket(g, 4, 5, 0, Colors.FOUND_BRACKET_BACKGROUND);
	}

	@Test
	public void testHighlightBracket() throws Exception {
		caretPainter.highlightBracket(g, 1, 2, 0, Colors.FOUND_BRACKET_BACKGROUND);
		verify(g).setColor(Colors.FOUND_BRACKET_BACKGROUND);
		verify(g).fillRect(10, 10, 5, 10);
	}

}
