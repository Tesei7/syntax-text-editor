package ru.tesei7.textEditor.editor.scroll;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FrameObserverableTest {
	@InjectMocks
	private FrameObserverable frameObserverable;
	@Mock
	private List<FrameListener> listeners;
	@Mock
	private FrameListener listener;
	@Mock
	private FrameEvent event;

	@Test
	public final void testAddListener() throws Exception {
		frameObserverable.addListener(listener);
		verify(listeners).add(listener);
	}

	@Test
	public final void testRemoveListener() throws Exception {
		frameObserverable.removeListener(listener);
		verify(listeners).remove(listener);
	}

	@Test
	public final void testNotifyListeners() throws Exception {
		frameObserverable.listeners = new ArrayList<>();
		frameObserverable.listeners.add(listener);
		frameObserverable.listeners.add(listener);
		frameObserverable.notifyListeners(event);
		verify(listener, times(2)).onFrameChanged(event);
	}

}
