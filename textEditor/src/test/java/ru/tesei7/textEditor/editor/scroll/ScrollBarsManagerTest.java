package ru.tesei7.textEditor.editor.scroll;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

import java.awt.event.AdjustmentListener;

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
	@Mock
	private AdjustmentListener l1;
	@Mock
	private AdjustmentListener l2;

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
		scrollBarsManager.vbar = vbar;
		scrollBarsManager.hbar = hbar;

		doNothing().when(scrollBarsManager).setBarValue(vbar, 42);
		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.VERTICAL, 42));
		verify(scrollBarsManager).setBarValue(vbar, 42);

		doNothing().when(scrollBarsManager).setBarValue(hbar, 43);
		scrollBarsManager.onFrameChanged(new FrameEvent(FrameEventType.HORIZONTAL, 43));
		verify(scrollBarsManager).setBarValue(hbar, 43);
	}

	@Test
	public void testRecalcMaxRows() throws Exception {
		scrollBarsManager.vbar = vbar;
		when(document.getSize()).thenReturn(40);
		when(document.getRows()).thenReturn(80);
		doNothing().when(scrollBarsManager).setBarFields(vbar, 80, 80);
		scrollBarsManager.recalcMaxRows();
		verify(scrollBarsManager).setBarFields(vbar, 80, 80);

		when(document.getSize()).thenReturn(140);
		doNothing().when(scrollBarsManager).setBarFields(vbar, 140, 80);
		scrollBarsManager.recalcMaxRows();
		verify(scrollBarsManager).setBarFields(vbar, 140, 80);
	}

	@Test
	public void testRecalcMaxCols() throws Exception {
		scrollBarsManager.hbar = hbar;
		when(document.getMaxCols()).thenReturn(40);
		when(document.getCols()).thenReturn(80);
		doNothing().when(scrollBarsManager).setBarFields(hbar, 80, 80);
		scrollBarsManager.recalcMaxCols();
		verify(scrollBarsManager).setBarFields(hbar, 80, 80);

		when(document.getMaxCols()).thenReturn(140);
		doNothing().when(scrollBarsManager).setBarFields(hbar, 140, 80);
		scrollBarsManager.recalcMaxCols();
		verify(scrollBarsManager).setBarFields(hbar, 140, 80);
	}

	@Test
	public void testSetBarFields() throws Exception {
		when(hbar.getAdjustmentListeners()).thenReturn(new AdjustmentListener[] { l1, l2 });
		scrollBarsManager.setBarFields(hbar, 100, 10);
		verify(hbar).removeAdjustmentListener(l1);
		verify(hbar).removeAdjustmentListener(l2);
		verify(hbar).setMaximum(100);
		verify(hbar).setVisibleAmount(10);
		verify(hbar).addAdjustmentListener(l1);
		verify(hbar).addAdjustmentListener(l2);
	}

	@Test
	public void testSetBarValue() throws Exception {
		when(hbar.getAdjustmentListeners()).thenReturn(new AdjustmentListener[] { l1, l2 });
		scrollBarsManager.setBarValue(hbar, 10);
		verify(hbar).removeAdjustmentListener(l1);
		verify(hbar).removeAdjustmentListener(l2);
		verify(hbar).setValue(10);
		verify(hbar).addAdjustmentListener(l1);
		verify(hbar).addAdjustmentListener(l2);
	}

}
