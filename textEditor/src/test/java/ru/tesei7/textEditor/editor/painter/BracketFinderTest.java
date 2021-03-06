package ru.tesei7.textEditor.editor.painter;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;

@RunWith(MockitoJUnitRunner.class)
public class BracketFinderTest {
	@InjectMocks
	@Spy
	private BracketFinder bracketFinder;
	@Mock
	private BracketService bracketService;
	@Mock
	private Token currentBracketToken;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Token relativeBracketToken;

	@Test
	public void testSetRelativeBracketType() throws Exception {
		bracketFinder.currentBracketToken = currentBracketToken;

		when(currentBracketToken.getType()).thenReturn(TokenTypes.L_BRACE);
		bracketFinder.setRelativeBracketType();
		assertThat(bracketFinder.relativeBracketType, is(TokenTypes.R_BRACE));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.L_BRACKET);
		bracketFinder.setRelativeBracketType();
		assertThat(bracketFinder.relativeBracketType, is(TokenTypes.R_BRACKET));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.L_PARENTHESIS);
		bracketFinder.setRelativeBracketType();
		assertThat(bracketFinder.relativeBracketType, is(TokenTypes.R_PARENTHESIS));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.R_BRACE);
		bracketFinder.setRelativeBracketType();
		assertThat(bracketFinder.relativeBracketType, is(TokenTypes.L_BRACE));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.R_BRACKET);
		bracketFinder.setRelativeBracketType();
		assertThat(bracketFinder.relativeBracketType, is(TokenTypes.L_BRACKET));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.R_PARENTHESIS);
		bracketFinder.setRelativeBracketType();
		assertThat(bracketFinder.relativeBracketType, is(TokenTypes.L_PARENTHESIS));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.IDENTIFIER);
		assertThatThrownBy(() -> bracketFinder.setRelativeBracketType()).isInstanceOf(Exception.class);
	}

	@Test
	public void testSetSearchDirection() throws Exception {
		bracketFinder.currentBracketToken = currentBracketToken;

		when(currentBracketToken.getType()).thenReturn(TokenTypes.L_BRACE);
		bracketFinder.setSearchDirection();
		assertThat(bracketFinder.direction, is(1));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.R_BRACE);
		bracketFinder.setSearchDirection();
		assertThat(bracketFinder.direction, is(-1));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.IDENTIFIER);
		assertThatThrownBy(() -> bracketFinder.setSearchDirection()).isInstanceOf(Exception.class);
	}

	@Test
	public void testFind() throws Exception {
		doReturn(bracketFinder).when(bracketFinder).findCurrentBracket();
		doReturn(bracketFinder).when(bracketFinder).setRelativeBracketType();
		doReturn(bracketFinder).when(bracketFinder).setSearchDirection();
		doReturn(bracketFinder).when(bracketFinder).findRelativeBracket();
		int[] toBeReturned = new int[] { 1, 2 };
		doReturn(toBeReturned).when(bracketFinder).getRelativeBracketCoordinates();
		assertThat(bracketFinder.find(), is(toBeReturned));
		verify(bracketFinder).findCurrentBracket();
		verify(bracketFinder).setRelativeBracketType();
		verify(bracketFinder).setSearchDirection();
		verify(bracketFinder).findRelativeBracket();
		verify(bracketFinder).getRelativeBracketCoordinates();

		Exception toBeThrown = new Exception();
		doThrow(toBeThrown).when(bracketFinder).setSearchDirection();
		assertNull(bracketFinder.find());
	}

	@Test
	public void testGetRelativeBracketCoordinates() throws Exception {
		bracketFinder.relativeBracketToken = mock(Token.class);
		bracketFinder.relativeBracketLine = 2;
		when(bracketFinder.relativeBracketToken.getOffset()).thenReturn(1);
		Line line = mock(Line.class);
		when(document.getLineByIndex(2)).thenReturn(line);
		when(document.getXToPaint(line, 1)).thenReturn(3);
		when(document.getFirstVisibleRow()).thenReturn(1);
		assertTrue(Arrays.equals(new int[] { 1, 3 }, bracketFinder.getRelativeBracketCoordinates()));
	}

	@Test
	public void testFindCurrentBracket() throws Exception {
		Line line = mock(Line.class);
		Token t1 = mock(Token.class);
		Token t2 = mock(Token.class);
		when(document.getCurrentLine()).thenReturn(line);
		when(line.getCurrentToken()).thenReturn(t1);
		when(line.getPreviousToken()).thenReturn(t2);

		assertThatThrownBy(() -> bracketFinder.findCurrentBracket()).isInstanceOf(Exception.class);
		
		when(bracketService.isBracket(t2)).thenReturn(true);
		bracketFinder.findCurrentBracket();
		assertThat(bracketFinder.currentBracketToken, is(t2));
		
		when(bracketService.isBracket(t1)).thenReturn(true);
		bracketFinder.findCurrentBracket();
		assertThat(bracketFinder.currentBracketToken, is(t1));
	}

}
