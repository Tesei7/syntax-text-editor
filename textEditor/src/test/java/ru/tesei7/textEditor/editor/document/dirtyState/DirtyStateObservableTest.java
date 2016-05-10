package ru.tesei7.textEditor.editor.document.dirtyState;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DirtyStateObservableTest {
	@InjectMocks
	private DirtyStateObservable observable;
	@Mock
	private List<DirtyStateListener> listeners;
	@Mock
	private DirtyStateListener listener;
	@Mock
	private DirtyStateEvent event;

	@Test
	public final void testAddListener() throws Exception {
		observable.addListener(listener);
		verify(listeners).add(listener);
	}

	@Test
	public final void testRemoveListener() throws Exception {
		observable.removeListener(listener);
		verify(listeners).remove(listener);
	}

	@Test
	public final void testNotifyListeners() throws Exception {
		observable.listeners = new ArrayList<>();
		observable.listeners.add(listener);
		observable.listeners.add(listener);
		observable.notifyListeners(event);
		
		when(event.getOldDirtyState()).thenReturn(true);
		when(event.getNewDirtyState()).thenReturn(true);
		verify(listener, never()).onDirtyStateChanged(event);
		
		when(event.getNewDirtyState()).thenReturn(false);
		observable.notifyListeners(event);
		verify(listener, times(2)).onDirtyStateChanged(event);
	}

}
