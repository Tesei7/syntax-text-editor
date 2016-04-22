package ru.tesei7.textEditor.editor;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

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
		System.out.println("start craete Doc");
		
		String[] split = text.split("\n");
		
		long t2 = System.currentTimeMillis();
		System.out.println("splited string:" + (t2 - t1) + "ms");
		
		Line prev = null;
		for (int i = 0; i < split.length; i++) {
			Line l = null;
			if (i == 0) {
				l = document.getFirstLine();
			} else {
				l = new Line();
				prev.linkWith(l);
			}
//			List<Character> list = split[i].chars().mapToObj(c -> (char) c).collect(toList());
//			l.setText(list);
			l.setText(split[i].toCharArray());
			prev = l;
			l.setOffset(0);
		}
		
		long t3 = System.currentTimeMillis();
		System.out.println("fill doc:" + (t3 - t2) + "ms");
	}

	public void setText(byte[] text) {
		Line prev = null;
		for (int i = 0; i < text.length; i++) {
		}
	}
}
