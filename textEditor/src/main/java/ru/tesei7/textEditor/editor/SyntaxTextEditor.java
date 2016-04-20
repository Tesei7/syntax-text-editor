package ru.tesei7.textEditor.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JPanel;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.document.SyntaxDocument;
import ru.tesei7.textEditor.editor.listeners.key.CaretKeyListener;
import ru.tesei7.textEditor.editor.listeners.key.TextKeyListener;
import ru.tesei7.textEditor.editor.painter.SyntaxDocumentPainter;
import ru.tesei7.textEditor.editor.utils.FontUtils;

public class SyntaxTextEditor extends JPanel {
	private static final long serialVersionUID = 1485541136343010484L;

	@Inject
	protected FontUtils fontUtils;
	@Inject
	protected CaretKeyListener baseKeyListener;
	@Inject
	protected TextKeyListener textKeyListener;

	@Inject
	protected SyntaxDocumentPainter painter;

	@Inject
	private SyntaxDocument document;
	@Inject
	private SyntaxCaret caret;

	public SyntaxTextEditor() {
		super();
	}

	@PostConstruct
	public void init() {
		setFocusable(true);
		setBackground(Color.WHITE);
		setFont(fontUtils.getFont());

		baseKeyListener.setEditor(this);
		textKeyListener.setEditor(this);

		painter.setEditor(this);
		caret.setDocument(document);
		addKeyListener(textKeyListener);
		addKeyListener(baseKeyListener);
	}

	public SyntaxDocument getDocument() {
		return document;
	}

	public SyntaxCaret getCaret() {
		return caret;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		painter.paint(g);
	}

}
