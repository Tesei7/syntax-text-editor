package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import ru.tesei7.textEditor.editor.scroll.Direction;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollObserverable;

public class ScrollBarListener extends SyntaxScrollObserverable implements AdjustmentListener {

	private Direction direction;
	private SyntaxScrollObserverable scrollObserverable;

	public ScrollBarListener(SyntaxScrollObserverable scrollObserverable, Direction direction) {
		this.scrollObserverable = scrollObserverable;
		this.direction = direction;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		scrollObserverable.notifyListeners(new SyntaxScrollEvent(direction, e.getAdjustmentType(), e.getValue()));
	}

}
