package ru.tesei7.textEditor.editor.caret;

import java.util.Iterator;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class CaretService {

	private SyntaxDocument document;

	public CaretService(SyntaxDocument document) {
		this.document = document;
	}

	public int getTargetLineOffset(Line targetLine) {
		int xToPaint = getXToPaint();

		int i = 0;
		for (Iterator<Character> iterator = targetLine.getText().iterator(); iterator.hasNext();) {
			Character c = iterator.next();
			xToPaint -= c.equals('\t') ? SyntaxTextEditor.TAB_INDENT : 1;
			if (xToPaint < 0) {
				break;
			}
			i++;
		}
		return i;
	}
	
	public int getXToPaint() {
		int x = 0;
		Line currentLine = document.getCurrentLine();
		for (int i = 0; i < currentLine.getOffset(); i++) {
			Character c = currentLine.getText().get(i);
			if (c.equals('\t')) {
				x += SyntaxTextEditor.TAB_INDENT;
			} else {
				x += 1;
			}
		}
		return x;
	}
}
