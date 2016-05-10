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
public class FrameObservableTest {
	@InjectMocks
	private FrameObservable frameObservable;
	@Mock
	private List<FrameListener> listeners;
	@Mock
	private FrameListener listener;
	@Mock
	private FrameEvent event;

	@Test
	public final void testAddListener() throws Exception {
		frameObservable.addListener(listener);
		verify(listeners).add(listener);
	}

	@Test
	public final void testRemoveListener() throws Exception {
		frameObservable.removeListener(listener);
		verify(listeners).remove(listener);
	}

	@Test
	public final void testNotifyListeners() throws Exception {
		frameObservable.listeners = new ArrayList<>();
		frameObservable.listeners.add(listener);
		frameObservable.listeners.add(listener);
		frameObservable.notifyListeners(event);
		verify(listener, times(2)).onFrameChanged(event);
	}

}
