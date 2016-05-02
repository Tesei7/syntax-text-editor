package ru.tesei7.textEditor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.SyntaxTextEditorViewMode;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationActionListenerTest {
	@InjectMocks
	@Spy
	private ApplicationActionListener applicationActionListener;
	@Mock
	private Application app;
	@Mock
	private SaveLoadFileService saveLoadFileService;
	@Mock
	private SyntaxTextEditor ste;
	@Mock
	private ActionEvent e;
	@Mock
	private LoadedFile lfile;

	@Test
	public void testActionPerformed() throws Exception {
		when(app.getTextArea()).thenReturn(ste);
		
		when(e.getActionCommand()).thenReturn(MenuActions.EXIT);
		applicationActionListener.actionPerformed(e);
		verify(app).close();
		
		doNothing().when(applicationActionListener).newFile();
		when(e.getActionCommand()).thenReturn(MenuActions.NEW);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).newFile();
		
		doNothing().when(applicationActionListener).open();
		when(e.getActionCommand()).thenReturn(MenuActions.OPEN);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).open();
		
		doNothing().when(applicationActionListener).saveAs();
		when(e.getActionCommand()).thenReturn(MenuActions.SAVE);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).saveAs();
		
		when(app.getLoadFile()).thenReturn(lfile);
		when(lfile.getFile()).thenReturn(new File(""));
		doNothing().when(applicationActionListener).save();
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).save();
		
		doNothing().when(applicationActionListener).saveAs();
		when(e.getActionCommand()).thenReturn(MenuActions.SAVE_AS);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener, times(2)).saveAs();
		
		when(e.getActionCommand()).thenReturn(MenuActions.DEFAULT_VIEW);
		applicationActionListener.actionPerformed(e);
		verify(ste).setViewMode(SyntaxTextEditorViewMode.DEFAULT);
		
		when(e.getActionCommand()).thenReturn(MenuActions.FIXED_WIDTH_VIEW);
		applicationActionListener.actionPerformed(e);
		verify(ste).setViewMode(SyntaxTextEditorViewMode.FIXED_WIDTH);
		
		doNothing().when(applicationActionListener).changeSyntax(any());
		when(e.getActionCommand()).thenReturn(MenuActions.PLAIN_TEXT);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).changeSyntax(Language.PLAIN_TEXT);
		
		when(e.getActionCommand()).thenReturn(MenuActions.JAVA);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).changeSyntax(Language.JAVA);
		
		when(e.getActionCommand()).thenReturn(MenuActions.JAVA_SCRIPT);
		applicationActionListener.actionPerformed(e);
		verify(applicationActionListener).changeSyntax(Language.JAVA_SCRIPT);
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
