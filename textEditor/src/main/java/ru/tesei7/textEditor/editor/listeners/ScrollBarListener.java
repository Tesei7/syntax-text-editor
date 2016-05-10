package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollObservable;

public class ScrollBarListener extends SyntaxScrollObservable implements AdjustmentListener {

	Direction direction;
	private SyntaxScrollObservable scrollObservable;

	public ScrollBarListener(SyntaxScrollObservable scrollObservable, Direction direction) {
		this.scrollObservable = scrollObservable;
		this.direction = direction;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		scrollObservable.notifyListeners(new SyntaxScrollEvent(direction, e.getValue()));
	}

}
