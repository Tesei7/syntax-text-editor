package ru.tesei7.textEditor.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;
import ru.tesei7.textEditor.editor.listeners.key.CaretKeyListener;
import ru.tesei7.textEditor.editor.listeners.key.TextKeyListener;
import ru.tesei7.textEditor.editor.painter.CaretPainter;
import ru.tesei7.textEditor.editor.painter.SyntaxDocumentPainter;
import ru.tesei7.textEditor.editor.utils.FontUtils;

public class SyntaxTextEditor extends JPanel {
	private static final long serialVersionUID = 1485541136343010484L;
	public static final int TAB_INDENT = 4;
	static final int DEFAULT_ROWS = 40;
	static final int DEFAULT_COLS = 80;

	private SyntaxDocument document;

	protected CaretKeyListener baseKeyListener;
	protected TextKeyListener textKeyListener;

	private SyntaxDocumentPainter documentPainter;
	private CaretPainter caretPainter;
	private SyntaxCaret caret;
	private SyntaxDocumentIO io;

	private int rows;
	private int cols;

	public SyntaxTextEditor() {
		super();
		rows = SyntaxTextEditor.DEFAULT_ROWS;
		cols = SyntaxTextEditor.DEFAULT_COLS;
		recalcSize();

		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.WHITE);
		setFont(FontUtils.DEFAULT);

		this.document = new SyntaxDocument(this);
		this.caret = new SyntaxCaret(this);
		this.caretPainter = new CaretPainter(caret);
		this.documentPainter = new SyntaxDocumentPainter(this);
		this.io = new SyntaxDocumentIO(document);

		this.baseKeyListener = new CaretKeyListener(this);
		this.textKeyListener = new TextKeyListener(this);
		addKeyListener(textKeyListener);
		addKeyListener(baseKeyListener);
	}

	public SyntaxDocument getDocument() {
		return document;
	}

	public SyntaxDocumentPainter getPainter() {
		return documentPainter;
	}

	public SyntaxCaret getCaret() {
		return caret;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		caretPainter.paint(g);
		documentPainter.paint(g);
	}

	public String getText() {
		return io.getText();
	}

	public void setText(String text) {
		io.setText(text);
		repaint();
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		recalcSize();
		repaint();
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
		recalcSize();
		repaint();
	}

	private void recalcSize() {
		FontMetrics fm = getFontMetrics(getFont());
		int height = fm.getHeight() * (rows + 1) + fm.getDescent();
		int width = fm.charWidth('a') * cols;
		setPreferredSize(new Dimension(width, height));
	}

}
