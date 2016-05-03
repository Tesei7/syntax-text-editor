package ru.tesei7.textEditor.editor.painter;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
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
	private Token reletiveBracketToken;

	@Test
	public void testSetReletiveBracketType() throws Exception {
		bracketFinder.currentBracketToken = currentBracketToken;

		when(currentBracketToken.getType()).thenReturn(TokenTypes.LBRACE);
		bracketFinder.setReletiveBracketType();
		assertThat(bracketFinder.reletiveBracketType, is(TokenTypes.RBRACE));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.LBRACK);
		bracketFinder.setReletiveBracketType();
		assertThat(bracketFinder.reletiveBracketType, is(TokenTypes.RBRACK));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.LPAREN);
		bracketFinder.setReletiveBracketType();
		assertThat(bracketFinder.reletiveBracketType, is(TokenTypes.RPAREN));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.RBRACE);
		bracketFinder.setReletiveBracketType();
		assertThat(bracketFinder.reletiveBracketType, is(TokenTypes.LBRACE));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.RBRACK);
		bracketFinder.setReletiveBracketType();
		assertThat(bracketFinder.reletiveBracketType, is(TokenTypes.LBRACK));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.RPAREN);
		bracketFinder.setReletiveBracketType();
		assertThat(bracketFinder.reletiveBracketType, is(TokenTypes.LPAREN));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.IDENTIFIER);
		assertThatThrownBy(() -> bracketFinder.setReletiveBracketType()).isInstanceOf(Exception.class);
	}

	@Test
	public void testSetSearchDirection() throws Exception {
		bracketFinder.currentBracketToken = currentBracketToken;

		when(currentBracketToken.getType()).thenReturn(TokenTypes.LBRACE);
		bracketFinder.setSearchDirection();
		assertThat(bracketFinder.direction, is(1));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.RBRACE);
		bracketFinder.setSearchDirection();
		assertThat(bracketFinder.direction, is(-1));

		when(currentBracketToken.getType()).thenReturn(TokenTypes.IDENTIFIER);
		assertThatThrownBy(() -> bracketFinder.setSearchDirection()).isInstanceOf(Exception.class);
	}

	@Test
	public void testFind() throws Exception {
		doReturn(bracketFinder).when(bracketFinder).findCurrentBracket();
		doReturn(bracketFinder).when(bracketFinder).setReletiveBracketType();
		doReturn(bracketFinder).when(bracketFinder).setSearchDirection();
		doReturn(bracketFinder).when(bracketFinder).findReletiveBracket();
		int[] toBeReturned = new int[] { 1, 2 };
		doReturn(toBeReturned).when(bracketFinder).getReletiveBracketCoordinates();
		assertThat(bracketFinder.find(), is(toBeReturned));
		verify(bracketFinder).findCurrentBracket();
		verify(bracketFinder).setReletiveBracketType();
		verify(bracketFinder).setSearchDirection();
		verify(bracketFinder).findReletiveBracket();
		verify(bracketFinder).getReletiveBracketCoordinates();

		Exception toBeThrown = new Exception();
		doThrow(toBeThrown).when(bracketFinder).setSearchDirection();
		assertNull(bracketFinder.find());
	}

	@Test
	public void testGetReletiveBracketCoordinates() throws Exception {
		bracketFinder.reletiveBracketToken = mock(Token.class);
		bracketFinder.reletiveBracketLine = 2;
		when(bracketFinder.reletiveBracketToken.getOffset()).thenReturn(1);
		Line line = mock(Line.class);
		when(document.getLineByIndex(2)).thenReturn(line);
		when(document.getXToPaint(line, 1)).thenReturn(3);
		when(document.getFirstVisibleRow()).thenReturn(1);
		assertTrue(Arrays.equals(new int[] { 1, 3 }, bracketFinder.getReletiveBracketCoordinates()));
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
