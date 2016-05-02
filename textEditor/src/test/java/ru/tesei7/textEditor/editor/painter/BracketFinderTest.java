package ru.tesei7.textEditor.editor.painter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.TokenTypes;

@RunWith(MockitoJUnitRunner.class)
public class BracketFinderTest {
	@InjectMocks
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

}
