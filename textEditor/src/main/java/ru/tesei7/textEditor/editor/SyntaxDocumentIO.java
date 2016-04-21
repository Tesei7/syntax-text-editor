package ru.tesei7.textEditor.editor;

import static java.util.stream.Collectors.toList;

import java.util.List;

import ru.tesei7.textEditor.editor.document.Line;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;

public class SyntaxDocumentIO {

	private SyntaxDocument document;

	public void setDocument(SyntaxDocument document) {
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
			List<Character> list = split[i].chars().mapToObj(c -> (char) c).collect(toList());
			l.setText(list);
			prev = l;
			l.setOffset(0);
		}
	}
}
