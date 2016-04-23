package ru.tesei7.textEditor.editor;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.FontMetrics;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SyntaxTextEditorTest {
	@InjectMocks
	@Spy
	private SyntaxTextEditor editor;
	@Mock
	private FontMetrics fm;

	@Test
	@Ignore
	public void testRecalcSize() throws Exception {
//		editor.rows = 1;
//		editor.cols = 1;
		doReturn(fm).when(editor).getFontMetrics(any());
		when(fm.getHeight()).thenReturn(10);
		when(fm.getDescent()).thenReturn(2);
		when(fm.charWidth('a')).thenReturn(7);
		
		editor.recalcSize();
		verify(editor).setPreferredSize(any());
	}

}
