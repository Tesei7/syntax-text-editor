package ru.tesei7.textEditor.editor;

import java.awt.Graphics;

import javax.swing.JPanel;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.DocumentEditEvent;
import ru.tesei7.textEditor.editor.document.DocumentEditListener;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollListener;
import ru.tesei7.textEditor.editor.scroll.bar.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.bar.FrameListener;

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
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		editor.getCaretPainter().paint(g, editor.isCaretVisible());
		editor.getDocumentPainter().paint(g);
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
