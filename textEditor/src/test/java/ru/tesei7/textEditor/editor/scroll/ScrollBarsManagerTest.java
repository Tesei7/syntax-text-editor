package ru.tesei7.textEditor.editor.scroll;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

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
	private JScrollBar hBar;
	@Mock
	private JScrollBar vBar;
	@Mock
	private Line l;
	@Mock
	private AdjustmentListener l1;
	@Mock
	private AdjustmentListener l2;

	@Test
	public void testOnDimensionsChanged() throws Exception {
		// doNothing().when(scrollBarsManager).setBarMaximum(vBar, 100);
		doNothing().when(scrollBarsManager).recalculateMaxRows();
		doNothing().when(scrollBarsManager).recalculateMaxCols();
		scrollBarsManager.onDimensionsChanged(new DimensionsEvent(DimensionType.ONLY_X));
		verify(scrollBarsManager).recalculateMaxCols();
		verify(scrollBarsManager, never()).recalculateMaxRows();

		scrollBarsManager.onDimensionsChanged(new DimensionsEvent(DimensionType.X_AND_Y));
		verify(scrollBarsManager, times(2)).recalculateMaxCols();
		verify(scrollBarsManager).recalculateMaxRows();
	}

	@Test
	public void testOnFrameChanged() throws Exception {
		scrollBarsManager.vBar = vBar;
		scrollBarsManager.hBar = hBar;

		doNothing().when(scrollBarsManager).setBarValue(vBar, 42);
		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.VERTICAL, 42));
		verify(scrollBarsManager).setBarValue(vBar, 42);

        when(vBar.getValue()).thenReturn(10);
        doNothing().when(scrollBarsManager).setBarValue(vBar, 52);
		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.VERTICAL_ADD, 42));
		verify(scrollBarsManager).setBarValue(vBar, 52);

		doNothing().when(scrollBarsManager).setBarValue(hBar, 43);
		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.HORIZONTAL, 43));
		verify(scrollBarsManager).setBarValue(hBar, 43);
	}

	@Test
	public void testRecalculateMaxRows() throws Exception {
		scrollBarsManager.vBar = vBar;
		when(document.getSize()).thenReturn(40);
		when(document.getRows()).thenReturn(80);
		doNothing().when(scrollBarsManager).setBarFields(vBar, 80, 80);
		scrollBarsManager.recalculateMaxRows();
		verify(scrollBarsManager).setBarFields(vBar, 80, 80);

		when(document.getSize()).thenReturn(140);
		doNothing().when(scrollBarsManager).setBarFields(vBar, 140, 80);
		scrollBarsManager.recalculateMaxRows();
		verify(scrollBarsManager).setBarFields(vBar, 140, 80);
	}

	@Test
	public void testRecalculateMaxCols() throws Exception {
		scrollBarsManager.hBar = hBar;
		when(document.getMaxCols()).thenReturn(40);
		when(document.getCols()).thenReturn(80);
		doNothing().when(scrollBarsManager).setBarFields(hBar, 80, 80);
		scrollBarsManager.recalculateMaxCols();
		verify(scrollBarsManager).setBarFields(hBar, 80, 80);

		when(document.getMaxCols()).thenReturn(140);
		doNothing().when(scrollBarsManager).setBarFields(hBar, 140, 80);
		scrollBarsManager.recalculateMaxCols();
		verify(scrollBarsManager).setBarFields(hBar, 140, 80);
	}

	@Test
	public void testSetBarFields() throws Exception {
		when(hBar.getAdjustmentListeners()).thenReturn(new AdjustmentListener[] { l1, l2 });
		scrollBarsManager.setBarFields(hBar, 100, 10);
		verify(hBar).removeAdjustmentListener(l1);
		verify(hBar).removeAdjustmentListener(l2);
		verify(hBar).setMaximum(100);
		verify(hBar).setVisibleAmount(10);
		verify(hBar).addAdjustmentListener(l1);
		verify(hBar).addAdjustmentListener(l2);
	}

	@Test
	public void testSetBarValue() throws Exception {
		when(hBar.getAdjustmentListeners()).thenReturn(new AdjustmentListener[] { l1, l2 });
		scrollBarsManager.setBarValue(hBar, 10);
		verify(hBar).removeAdjustmentListener(l1);
		verify(hBar).removeAdjustmentListener(l2);
		verify(hBar).setValue(10);
		verify(hBar).addAdjustmentListener(l1);
		verify(hBar).addAdjustmentListener(l2);
	}

}
