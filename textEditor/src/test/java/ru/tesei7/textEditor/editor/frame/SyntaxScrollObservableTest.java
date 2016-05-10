package ru.tesei7.textEditor.editor.frame;

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
public class SyntaxScrollObservableTest {
	@InjectMocks
	private SyntaxScrollObservable observable;
	@Mock
	private List<SyntaxScrollListener> listeners;
	@Mock
	private SyntaxScrollListener listener;
	@Mock
	private SyntaxScrollEvent event;

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
		verify(listener, times(2)).onScrollChanged(event);
	}
}
