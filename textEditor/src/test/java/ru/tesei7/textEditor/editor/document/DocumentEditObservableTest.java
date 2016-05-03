package ru.tesei7.textEditor.editor.document;

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
public class DocumentEditObservableTest {
	@InjectMocks
	private DocumentEditObservable observerable;
	@Mock
	private List<DocumentEditListener> listeners;
	@Mock
	private DocumentEditListener listener;
	@Mock
	private DocumentEditEvent event;

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
		verify(listener, times(2)).onDocumentEdited(event);
	}

}
