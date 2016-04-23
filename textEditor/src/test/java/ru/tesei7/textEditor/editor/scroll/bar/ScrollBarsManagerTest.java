package ru.tesei7.textEditor.editor.scroll.bar;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
	public void testOnFrameChanged() throws Exception {
		doNothing().when(scrollBarsManager).setVScrollValue(l);
		doNothing().when(scrollBarsManager).setVScrollValue(null);
		doNothing().when(scrollBarsManager).setHScrollValue(42);
		doNothing().when(scrollBarsManager).setHScrollValue(null);

		scrollBarsManager.onFrameChanged(new FrameEvent(42));
		verify(scrollBarsManager).setVScrollValue(null);
		verify(scrollBarsManager).setHScrollValue(42);

		scrollBarsManager.onFrameChanged(new FrameEvent(l));
		verify(scrollBarsManager).setVScrollValue(null);
		verify(scrollBarsManager).setHScrollValue(null);
	}

	@Test
	@Ignore
	public void testSetVScrollValue() throws Exception {
		doNothing().when(scrollBarsManager).setBarValue(vbar, 42);
		scrollBarsManager.setVScrollValue(null);
		verify(scrollBarsManager, never()).setBarValue(vbar, 42);

		doNothing().when(scrollBarsManager).setBarValue(vbar, 42);
		when(document.getLineIndex(l)).thenReturn(42);
		scrollBarsManager.setVScrollValue(l);
		verify(scrollBarsManager).setBarValue(vbar, 42);
	}

}
