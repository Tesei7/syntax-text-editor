package ru.tesei7.textEditor.editor;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import ru.tesei7.textEditor.editor.text.BaseKeyListener;
import ru.tesei7.textEditor.editor.text.SCaret;
import ru.tesei7.textEditor.editor.utils.FontUtils;

public class SyntaxTextEditor extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1485541136343010484L;

	protected FontUtils fontUtils = new FontUtils();
	
	private SyntaxDocument document = new SyntaxDocument();
	protected SCaret caret = new SCaret(0, 0);

	public SyntaxTextEditor() {
		super();
		setFocusable(true);
		addKeyListener(this);
		addKeyListener(new BaseKeyListener(this));
	}
	
	public SyntaxDocument getDocument() {
		return document;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(fontUtils.getFont());
		
		paintCaret(g);
		
//		FontMetrics fontMetrics = g.getFontMetrics();
//		int height = fontMetrics.getHeight();
		
//		for (Entry<Integer, Line> e : document.getDemaged().entrySet()) {
//			Integer row = e.getKey();
//			e.getValue().paint(g, row);
//		}
//		document.getDemaged().clear();
	}
	
	private void paintCaret(Graphics g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		int height = fontMetrics.getHeight();
		int width = fontMetrics.stringWidth("a");
		Line currentLine = document.getCurrentLine();
		g.drawRect(currentLine.getOffset() * width, (1 + caret.getRow()) * height, 0, height);
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.VK_BACK_SPACE && keyChar != KeyEvent.VK_DELETE && keyChar != KeyEvent.CHAR_UNDEFINED) {
			document.addChar(keyChar);
			repaint();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			document.backspaceChar();
			repaint();
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	
}
