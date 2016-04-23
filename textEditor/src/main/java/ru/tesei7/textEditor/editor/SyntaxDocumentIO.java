package ru.tesei7.textEditor.editor;

import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;

public class SyntaxDocumentIO {

	private SyntaxDocument document;

	public SyntaxDocumentIO(SyntaxDocument document) {
		this.document = document;
	}

	public String getText() {
		StringBuilder sb = new StringBuilder();
		Line line = document.getFirstLine();
		do {
			sb.append(line.getChars());
			if (line.hasNext()) {
				sb.append("\n");
			}
			line = line.getNext();
		} while (line != null);
		return sb.toString();
	}

	public void setText(String text) {
		long t1 = System.currentTimeMillis();
		System.out.println("Start loading file");
		
		String[] split = text.split("\n");
		Line prev = null;
		for (int i = 0; i < split.length; i++) {
			Line l = null;
			if (i == 0) {
				l = document.getFirstLine();
			} else {
				l = new Line();
				prev.linkWith(l);
			}
			l.setText(split[i].toCharArray());
			prev = l;
			l.setOffset(0);
		}
		document.setCurrentLine(document.getFirstLine());
		document.setFirstVisibleLine(document.getFirstLine());
		document.setFirstVisibleCol(0);

		long t3 = System.currentTimeMillis();
		System.out.println("File loaded: " + (t3 - t1) + "ms");
	}

}
