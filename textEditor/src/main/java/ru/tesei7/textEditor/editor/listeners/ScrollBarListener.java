package ru.tesei7.textEditor.editor.listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import ru.tesei7.textEditor.editor.scroll.ScrollDirection;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollObserverable;

public class ScrollBarListener extends SyntaxScrollObserverable implements AdjustmentListener {

	private ScrollDirection direction;

	public ScrollBarListener(ScrollDirection direction) {
		this.direction = direction;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		notifyListeners(new SyntaxScrollEvent(direction, e.getAdjustmentType(), e.getValue()));
	}

}
