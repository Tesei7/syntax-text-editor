package ru.tesei7.textEditor.editor.scroll;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import javax.swing.JScrollBar;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.scroll.DimensionType;
import ru.tesei7.textEditor.editor.scroll.DimensionsEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.ScrollBarsManager;

@RunWith(MockitoJUnitRunner.class)
public class ScrollBarsManagerTest {
	@InjectMocks
	@Spy
	private ScrollBarsManager scrollBarsManager;
	@Mock
	private SyntaxDocument document;
	@Mock
	private JScrollBar hbar;
	@Mock
	private JScrollBar vbar;
	@Mock
	private Line l;

	@Test
	public void testOnDimensionsChanged() throws Exception {
		// doNothing().when(scrollBarsManager).setBarMaximum(vbar, 100);
		doNothing().when(scrollBarsManager).recalcMaxRows();
		doNothing().when(scrollBarsManager).recalcMaxCols();
		scrollBarsManager.onDimensionsChanged(new DimensionsEvent(DimensionType.ONLY_X));
		verify(scrollBarsManager).recalcMaxCols();
		verify(scrollBarsManager, never()).recalcMaxRows();

		scrollBarsManager.onDimensionsChanged(new DimensionsEvent(DimensionType.X_AND_Y));
		verify(scrollBarsManager, times(2)).recalcMaxCols();
		verify(scrollBarsManager).recalcMaxRows();
	}

	@Test
	@Ignore
	public void testOnFrameChanged() throws Exception {
		doNothing().when(scrollBarsManager).setBarValue(vbar, 42);
		doNothing().when(scrollBarsManager).setBarValue(hbar, 43);

		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.VERTICAL, 42));
		verify(scrollBarsManager).setBarValue(vbar, 42);
		
		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.HORIZONTAL, 43));
		verify(scrollBarsManager).setBarValue(hbar, 43);
	}

}
