package ru.tesei7.textEditor;

import static org.mockito.Mockito.verify;

import javax.swing.JRadioButtonMenuItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.Language;

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

	@Test
	public void testSelectSyntaxMenuItem() throws Exception {
		application.selectSyntaxMenuItem(Language.PLAIN_TEXT);
		verify(plainTextMenuItem).setSelected(true);
		
		application.selectSyntaxMenuItem(Language.JAVA);
		verify(javaMenuItem).setSelected(true);
		
		application.selectSyntaxMenuItem(Language.JAVA_SCRIPT);
		verify(jsMenuItem).setSelected(true);
	}

}
