package ru.tesei7.textEditor.editor.painter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Graphics;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxDocumentPainterTest {
	@InjectMocks
	@Spy
	private SyntaxDocumentPainter syntaxDocumentPainter;
	@Mock
	private SyntaxDocument document;
	@Mock
	private FontProperties fontProperties;
	@Mock
	private LinePainter linePainter;
	@Mock
	private List<Line> lines;
	@Mock
	private Graphics g;

	@Test
	public void testGetHeightToPaint() throws Exception {
		when(fontProperties.getLineHeight()).thenReturn(10);
		when(fontProperties.getDescent()).thenReturn(3);
		
		assertThat(syntaxDocumentPainter.getHeightToPaint(10), is(107));
	}

	@Test
	public void testPaint() throws Exception {
		when(document.getVisibleLines()).thenReturn(lines);
		doNothing().when(syntaxDocumentPainter).paintLines(g, lines, Language.JAVA);
		doNothing().when(syntaxDocumentPainter).paintSelection(g, lines);
		syntaxDocumentPainter.paint(g, Language.JAVA);
		verify(syntaxDocumentPainter).paintLines(g, lines, Language.JAVA);
		verify(syntaxDocumentPainter).paintSelection(g, lines);
	}

}
