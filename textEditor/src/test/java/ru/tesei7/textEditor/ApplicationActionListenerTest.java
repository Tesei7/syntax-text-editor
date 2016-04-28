package ru.tesei7.textEditor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.Language;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationActionListenerTest {
	@InjectMocks
	private ApplicationActionListener applicationActionListener;
	@Mock
	private Application app;
	@Mock
	private SaveLoadFileService saveLoadFileService;

	@Test
	@Ignore
	public void testActionPerformed() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	@Ignore
	public void testNewFile() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	@Ignore
	public void testOpen() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	@Ignore
	public void testSave() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	@Ignore
	public void testSaveAs() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	@Ignore
	public void testChangeTitle() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public void testGetLanguage() throws Exception {
		assertThat(applicationActionListener.getLanguage(""), is(Language.PLAIN_TEXT));
		assertThat(applicationActionListener.getLanguage("java"), is(Language.JAVA));
		assertThat(applicationActionListener.getLanguage("js"), is(Language.JAVA_SCRIPT));
	}

}
