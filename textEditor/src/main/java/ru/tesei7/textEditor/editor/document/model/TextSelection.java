package ru.tesei7.textEditor.editor.document.model;

import java.awt.Point;

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

	public Integer getStartOffset() {
		return document.getXToPaint(startOffset);
	}

	public Integer getEndLine() {
		return endLine;
	}

	public Integer getEndOffset() {
		return document.getXToPaint(endOffset);
	}

	public boolean isReversed() {
		if (notSelected()) {
			return false;
		}
		if (startLine > endLine)
			return true;
		if (startLine == endLine && startOffset > endOffset)
			return true;
		return false;
	}

	public boolean notSelected() {
		return startLine == null || startOffset == null || endLine == null || endOffset == null;
	}

	public Point getFrom() {
		if (notSelected()) {
			return null;
		}
		if (startLine < endLine) {
			return new Point(startOffset, startLine);
		} else if (startLine > endLine) {
			return new Point(endOffset, endLine);
		} else if (startOffset < endOffset) {
			return new Point(startOffset, startLine);
		} else if (startOffset > endOffset) {
			return new Point(endOffset, endLine);
		} else {
			return null;
		}
	}

	public Point getTo() {
		if (notSelected()) {
			return null;
		}
		if (startLine > endLine) {
			return new Point(startOffset, startLine);
		} else if (startLine < endLine) {
			return new Point(endOffset, endLine);
		} else if (startOffset > endOffset) {
			return new Point(startOffset, startLine);
		} else if (startOffset < endOffset) {
			return new Point(endOffset, endLine);
		} else {
			return null;
		}
	}

}
