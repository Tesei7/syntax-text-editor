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
public class SyntaxScrollObserverableTest {
	@InjectMocks
	private SyntaxScrollObserverable observerable;
	@Mock
	private List<SyntaxScrollListener> listeners;
	@Mock
	private SyntaxScrollListener listener;
	@Mock
	private SyntaxScrollEvent event;

	@Test
	public final void testAddListener() throws Exception {
		observerable.addListener(listener);
		verify(listeners).add(listener);
	}

	@Test
	public final void testRemoveListener() throws Exception {
		observerable.removeListener(listener);
		verify(listeners).remove(listener);
	}

	@Test
	public final void testNotifyListeners() throws Exception {
		observerable.listeners = new ArrayList<>();
		observerable.listeners.add(listener);
		observerable.listeners.add(listener);
		observerable.notifyListeners(event);
		verify(listener, times(2)).onScrollChanged(event);
	}
}
