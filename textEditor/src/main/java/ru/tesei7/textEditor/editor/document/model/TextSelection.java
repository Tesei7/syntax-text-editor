package ru.tesei7.textEditor.editor.document.model;

public class TextSelection {
	private Integer startLine;
	private Integer startOffset;
	private Integer endLine;
	private Integer endOffset;
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

	Integer _getStartLine() {
		return startLine;
	}

	Integer _getEndLine() {
		return endLine;
	}

	void _setStartLine(Integer startLine) {
		this.startLine = startLine;
	}

	void _setEndLine(Integer endLine) {
		this.endLine = endLine;
	}

	public void setStartLine(Integer startLine) {
		this.startLine = document.getCorrectLineIndex(startLine);
	}

	public void setEndLine(Integer endLine) {
		this.endLine = document.getCorrectLineIndex(endLine);
	}

	public void setStartOffset(Integer startOffset) {
		this.startOffset = startOffset;
	}

	public void setEndOffset(Integer endOffset) {
		this.endOffset = endOffset;
	}

	Integer getStartOffsetToPaint(Line l) {
		return document.getXToPaint(l, startOffset);
	}

	Integer getEndOffsetToPaint(Line l) {
		return document.getXToPaint(l, endOffset);
	}

	public Integer getLineFrom() {
		return isReversedLines() ? endLine : startLine;
	}

	public Integer getLineTo() {
		return isReversedLines() ? startLine : endLine;
	}

	public Integer getOffsetFrom() {
		return isReversed() ? endOffset : startOffset;
	}

	public Integer getOffsetTo() {
		return isReversed() ? startOffset : endOffset;
	}

	public Integer getOffsetToPaintFrom(Line l) {
		return isReversedOffset(l) ? getEndOffsetToPaint(l) : getStartOffsetToPaint(l);
	}

	public Integer getOffsetToPaintTo(Line l) {
		return isReversedOffset(l) ? getStartOffsetToPaint(l) : getEndOffsetToPaint(l);
	}

	public boolean isReversedLines() {
		if (notSelected()) {
			return false;
		}
		return startLine > endLine;
	}

	public boolean isReversedOffset(Line l) {
		if (notSelected()) {
			return false;
		}
		return isReversedLines() || (startLine.equals(endLine) && getStartOffsetToPaint(l) > getEndOffsetToPaint(l));
	}

	boolean isReversed() {
		return isReversedLines() || (startLine.equals(endLine) && startOffset > endOffset);
	}

	public boolean notSelected() {
		return startLine == null || startOffset == null || endLine == null || endOffset == null;
	}

}
