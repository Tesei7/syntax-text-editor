package ru.tesei7.textEditor.editor.document.model;

public class TextSelection {
	Integer startLine;
	Integer startOffset;
	Integer endLine;
	Integer endOffset;
	private SyntaxDocument document;

	public TextSelection(SyntaxDocument document) {
		this.document = document;
	}

	public void clear() {
		startLine = null;
		startOffset = null;
		endLine = null;
		endOffset = null;
	}

	public Integer getStartLine() {
		return startLine;
	}

	public Integer getStartOffset(Line l) {
		return document.getXToPaint(l, startOffset);
	}

	public Integer getEndLine() {
		return endLine;
	}

	public Integer getEndOffset(Line l) {
		return document.getXToPaint(l, endOffset);
	}

	public boolean notSelected() {
		return startLine == null || startOffset == null || endLine == null || endOffset == null;
	}

}
