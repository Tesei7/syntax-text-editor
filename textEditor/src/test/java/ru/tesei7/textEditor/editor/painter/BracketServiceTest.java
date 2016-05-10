package ru.tesei7.textEditor.editor.painter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;

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
public class BracketServiceTest {
	@InjectMocks
	@Spy
	private BracketService bracketService;
	@Mock
	private SyntaxDocument document;
	@Mock
	private Line line;
	@Mock
	private Token t1;
	@Mock
	private Token t2;

	@Test
	public void testGetBracketAtCurrentPosition() throws Exception {
		when(document.getCurrentLine()).thenReturn(line);
		when(line.getCurrentToken()).thenReturn(t1);
		when(line.getPreviousToken()).thenReturn(t2);
		
		doReturn(false).when(bracketService).isBracket(t1);
		doReturn(false).when(bracketService).isBracket(t2);
		assertThat(bracketService.getBracketAtCurrentPosition(), is(-1));
		
		doReturn(true).when(bracketService).isBracket(t2);
		assertThat(bracketService.getBracketAtCurrentPosition(), is(1));
		
		doReturn(true).when(bracketService).isBracket(t1);
		assertThat(bracketService.getBracketAtCurrentPosition(), is(0));
	}

	@Test
	public void testIsBracket() throws Exception {
		assertThat(bracketService.isBracket(null), is(false));
		when(t1.getType()).thenReturn(TokenTypes.IDENTIFIER);
		assertThat(bracketService.isBracket(t1), is(false));
		when(t1.getType()).thenReturn(TokenTypes.L_BRACE);
		assertThat(bracketService.isBracket(t1), is(true));
	}

}
