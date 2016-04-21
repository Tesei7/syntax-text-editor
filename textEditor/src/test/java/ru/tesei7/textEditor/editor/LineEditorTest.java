package ru.tesei7.textEditor.editor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.Line;

@RunWith(MockitoJUnitRunner.class)
public class LineEditorTest {
	@InjectMocks
	private LineEditor editor;
	@Mock
	private Line line;
	private List<Character> text = new LinkedList<>();

	@Before
	public void init() {
		text.add('1');
		text.add('2');
		text.add('3');
		when(line.getText()).thenReturn(text);
		when(line.getOffset()).thenReturn(1);
		when(line.getLenght()).thenReturn(3);
	}

	@Test
	public final void testPrintChar() throws Exception {
		editor.printChar('a', line);
		assertThat(text).containsExactly('1', 'a', '2', '3');
		
		text.clear();
		when(line.getOffset()).thenReturn(0);
		editor.printChar('a', line);
		assertThat(text).containsExactly('a');
	}

	@Test
	public final void testDelete() throws Exception {
		editor.delete(line);
		assertThat(text).containsExactly('1', '3');
		
		when(line.getOffset()).thenReturn(2);
		when(line.getLenght()).thenReturn(2);
		editor.delete(line);
		assertThat(text).containsExactly('1', '3');
	}

	@Test
	public final void testBackspace() throws Exception {
		editor.backspace(line);
		assertThat(text).containsExactly('2', '3');
		
		when(line.getOffset()).thenReturn(0);
		editor.backspace(line);
		assertThat(text).containsExactly('2', '3');
	}

}
