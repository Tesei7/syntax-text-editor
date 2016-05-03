package ru.tesei7.textEditor.editor.listeners;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.AdjustmentEvent;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollListener;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollObserverable;

@RunWith(MockitoJUnitRunner.class)
public class ScrollBarListenerTest {
	@InjectMocks
	private ScrollBarListener scrollBarListener;
	@Mock
	private List<SyntaxScrollListener> listeners;
	@Mock
	private AdjustmentEvent e;
	@Mock
	private SyntaxScrollObserverable scrollObserverable;

	@Test
	public void testAdjustmentValueChanged() throws Exception {
		when(e.getValue()).thenReturn(10);
		scrollBarListener.direction = Direction.HORIZONTAL;
		scrollBarListener.adjustmentValueChanged(e);
		verify(scrollObserverable).notifyListeners(argThat(isSyntaxScrollEvent(Direction.HORIZONTAL, 10)));
	}

	@SuppressWarnings("unchecked")
	private Matcher<SyntaxScrollEvent> isSyntaxScrollEvent(Direction direction, int value) {
		return allOf(hasProperty("direction", equalTo(direction)), hasProperty("value", equalTo(value)));
	}

}
