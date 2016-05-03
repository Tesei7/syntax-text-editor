package ru.tesei7.textEditor.editor.painter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.syntax.TokenTypes;
import ru.tesei7.textEditor.editor.utils.Colors;
import ru.tesei7.textEditor.editor.utils.Fonts;

@RunWith(MockitoJUnitRunner.class)
public class LinePainterTest {
	@InjectMocks
	@Spy
	private LinePainter linePainter;

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

}
