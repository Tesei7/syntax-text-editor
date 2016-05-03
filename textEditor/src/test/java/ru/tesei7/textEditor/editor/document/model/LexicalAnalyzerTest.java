package ru.tesei7.textEditor.editor.document.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.syntax.JavaTokenizer;
import ru.tesei7.textEditor.editor.syntax.Token;
import ru.tesei7.textEditor.editor.syntax.Tokenizer;

@RunWith(MockitoJUnitRunner.class)
public class LexicalAnalyzerTest {
	@InjectMocks
	@Spy
	private LexicalAnalyzer lexicalAnalyzer;
	@Mock
	private TokenizerFactory tokenizerFactory;
	@Mock
	private Tokenizer tokenizer;
	@Mock
	private Line l;
	@Mock
	private List<Token> tokens;

	@Test
	public void testReadTokensFromLine() throws Exception {
		lexicalAnalyzer.tokenizerFactory = tokenizerFactory;
		char[] text = new char[0];
		when(l.getText()).thenReturn(text);
		when(tokenizerFactory.createTokenizer(Language.JAVA, text, JavaTokenizer.INITIAL)).thenReturn(tokenizer);
		doReturn(1).when(lexicalAnalyzer).readTokens(tokens, tokenizer);
		assertThat(lexicalAnalyzer.readTokensFromLine(tokens, l, Language.JAVA, JavaTokenizer.INITIAL), is(1));
	}

}
