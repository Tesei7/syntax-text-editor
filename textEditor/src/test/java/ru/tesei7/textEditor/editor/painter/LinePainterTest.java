package ru.tesei7.textEditor.editor.painter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.FontProperties;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;
import ru.tesei7.textEditor.editor.utils.Colors;
import ru.tesei7.textEditor.editor.utils.Fonts;

@RunWith(MockitoJUnitRunner.class)
public class LinePainterTest {
	@InjectMocks
	@Spy
	private LinePainter linePainter;
	@Mock
	private Line line;
	@Mock
	private FontProperties fp;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Graphics g;
	@Mock
	private List<Token> tokens;

	@Test
	public void testGetColor() throws Exception {
		assertThat(linePainter.getColor(TokenTypes.KEYWORD), is(Colors.KEY_WORD));
		assertThat(linePainter.getColor(TokenTypes.COMMENT_EOL), is(Colors.COMMENT));
		assertThat(linePainter.getColor(TokenTypes.COMMENT_MULTI), is(Colors.COMMENT));
		assertThat(linePainter.getColor(TokenTypes.IDENTIFIER), is(Colors.IDENTIFIER));
		assertThat(linePainter.getColor(TokenTypes.STRING_LITERAL), is(Colors.STRING_LITERAL));
		assertThat(linePainter.getColor(TokenTypes.CHARACTER_LITERAL), is(Colors.STRING_LITERAL));
		assertThat(linePainter.getColor(TokenTypes.INTEGER_LITERAL), is(Colors.NUMBER_LITERAL));
		assertThat(linePainter.getColor(TokenTypes.FLOATING_POINT_LITERAL), is(Colors.NUMBER_LITERAL));
		assertThat(linePainter.getColor(TokenTypes.ANNOTATION), is(Colors.ANNOTATION));
		assertThat(linePainter.getColor(TokenTypes.OTHER), is(Colors.DEFAULT_TEXT));
		assertThat(linePainter.getColor(TokenTypes.OPERATOR), is(Colors.DEFAULT_TEXT));
	}

	@Test
	public void testGetFont() throws Exception {
		assertThat(linePainter.getFont(TokenTypes.KEYWORD), is(Fonts.BOLD));
		assertThat(linePainter.getFont(TokenTypes.OTHER), is(Fonts.DEFAULT));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetStyledText() throws Exception {
		assertThat(linePainter.getStyledText(line, null), hasSize(0));

		doReturn(Colors.IDENTIFIER).when(linePainter).getColor(TokenTypes.IDENTIFIER);
		doReturn(Fonts.DEFAULT).when(linePainter).getFont(TokenTypes.IDENTIFIER);
		Token t1 = mock(Token.class);
		Token t2 = mock(Token.class);
		when(line.getChars(t1)).thenReturn(new char[] { 'a', 'b' });
		when(line.getChars(t2)).thenReturn(new char[] { 'c', 'd', 'e' });
		when(t1.getOffset()).thenReturn(0);
		when(t2.getOffset()).thenReturn(2);
		when(t1.getType()).thenReturn(TokenTypes.IDENTIFIER);
		when(t2.getType()).thenReturn(TokenTypes.IDENTIFIER);

		Matcher<StyledText> t1Matcher = isStyledText(new char[] { 'a', 'b' }, 0, Fonts.DEFAULT, Colors.IDENTIFIER);
		Matcher<StyledText> t2Matcher = isStyledText(new char[] { 'c', 'd', 'e' }, 2, Fonts.DEFAULT, Colors.IDENTIFIER);
		assertThat(linePainter.getStyledText(line, Arrays.asList(t1, t2)), contains(t1Matcher, t2Matcher));
	}

	private Matcher<StyledText> isStyledText(char[] text, int offset, Font font, Color color) {
		return allOf(hasProperty("text", equalTo(text)), hasProperty("offset", equalTo(offset)),
				hasProperty("color", equalTo(color)), hasProperty("font", equalTo(font)));
	}

	@Test
	public void testPaintSelection() throws Exception {
		when(fp.getLineHeight()).thenReturn(10);
		when(fp.getCharWidth()).thenReturn(5);
		linePainter.paintSelection(g, new char[] { 'a', 'b', 'c', 'd' }, 1, 3, 5, 10, fp);
		verify(g).fillRect(5, 5, 10, 10);
		verify(g).drawChars(new char[] { 'b', 'c' }, 0, 2, 5, 10);
	}

	@Test
	public void testPaintLine() throws Exception {
		when(line.getTokens()).thenReturn(tokens);
		List<StyledText> list = new ArrayList<>();
		doReturn(list).when(linePainter).getStyledText(line, tokens);
		when(document.getFirstVisibleCol()).thenReturn(2);
		when(fp.getCharWidth()).thenReturn(5);

		list.add(new StyledText(new char[] { 'a', 'b', 'c' }, 0, Fonts.DEFAULT, Colors.IDENTIFIER));
		list.add(new StyledText(new char[] { ' ' }, 3, Fonts.DEFAULT, Colors.DEFAULT_TEXT));
		list.add(new StyledText(new char[] { 'i', 'f' }, 4, Fonts.BOLD, Colors.KEY_WORD));

		linePainter.paintLine(g, line, 42, fp, document);
		verify(g).drawChars(new char[] { 'a', 'b', 'c' }, 2, 1, 0, 42);
		verify(g).drawChars(new char[] { ' ', }, 0, 1, 5, 42);
		verify(g).drawChars(new char[] { 'i', 'f' }, 0, 2, 10, 42);
	}

}
