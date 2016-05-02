package ru.tesei7.textEditor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.JFrame;
import javax.swing.JRadioButtonMenuItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
	@InjectMocks
	private Application application;
	@Mock
	private JRadioButtonMenuItem javaMenuItem;
	@Mock
	private JRadioButtonMenuItem jsMenuItem;
	@Mock
	private JRadioButtonMenuItem plainTextMenuItem;
	@Mock
	private LoadedFile lf;
	@Mock
	private JFrame frame;
	@Mock
	private SyntaxTextEditor ste;

	@Test
	public void testSelectSyntaxMenuItem() throws Exception {
		application.selectSyntaxMenuItem(Language.PLAIN_TEXT);
		verify(plainTextMenuItem).setSelected(true);
		
		application.selectSyntaxMenuItem(Language.JAVA);
		verify(javaMenuItem).setSelected(true);
		
		application.selectSyntaxMenuItem(Language.JAVA_SCRIPT);
		verify(jsMenuItem).setSelected(true);
	}

	@Test
	public void testChangeTitle() throws Exception {
		application.setLoadFile(null);
		when(ste.isDirty()).thenReturn(false);
		
		application.changeTitle();
		verify(frame).setTitle(Application.TITLE + " - New Document");
		
		when(ste.isDirty()).thenReturn(true);
		application.changeTitle();
		verify(frame).setTitle(Application.TITLE + " - New Document *");
		
		application.setLoadFile(lf);
		when(lf.getFileName()).thenReturn("123");
		application.changeTitle();
		verify(frame).setTitle(Application.TITLE + " - 123 *");
	}

}
