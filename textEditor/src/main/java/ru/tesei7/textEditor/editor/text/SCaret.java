package ru.tesei7.textEditor.editor.text;

import java.awt.Rectangle;

public class SCaret {
	private int row;
	private int col;

	public SCaret(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void left() {
		col = col <= 0 ? 0 : col - 1;
	}
	
	public void right() {
		col = col+1;
	}
}
