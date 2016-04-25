package ru.tesei7.textEditor.editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.JPanel;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.DocumentEditEvent;
import ru.tesei7.textEditor.editor.document.DocumentEditListener;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollListener;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameListener;
import ru.tesei7.textEditor.editor.utils.FontUtils;

/**
 * Text editor component
 * 
 * @author Ilya
 *
 */
public class SyntaxTextPanel extends JPanel
		implements SyntaxCaretListener, DocumentEditListener, SyntaxScrollListener, FrameListener {
	private static final long serialVersionUID = -8378730572715369064L;

	private SyntaxTextEditor editor;

	public SyntaxTextPanel(SyntaxTextEditor editor) {
		this.editor = editor;

		setBackground(Color.WHITE);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setFont(FontUtils.DEFAULT);
		setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		editor.getCaretPainter().paintBackground(g);
		editor.getDocumentPainter().paint(g);
		editor.getCaretPainter().paintCaret(g, editor.isCaretVisible());
	}

	private void freezeCaretAndRepaint() {
		editor.freezeCaret();
		repaint();
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		freezeCaretAndRepaint();
	}

	@Override
	public void onDocumentEdited(DocumentEditEvent e) {
		freezeCaretAndRepaint();
	}

	@Override
	public void onScrollChanged(SyntaxScrollEvent e) {
		freezeCaretAndRepaint();
	}

	@Override
	public void onFrameChanged(FrameEvent e) {
		freezeCaretAndRepaint();
	}
}
