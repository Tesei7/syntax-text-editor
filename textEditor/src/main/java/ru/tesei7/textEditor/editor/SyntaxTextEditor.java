package ru.tesei7.textEditor.editor;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.UIManager;

public class SyntaxTextEditor extends JPanel implements KeyListener {

	private final static String ID = "SyntaxTextEditorUI";
	private String document = "asdfsdf";

	public SyntaxTextEditor() {
		super();
		setSize(400, 20000);
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.drawString("sdfgdfgdfg", 0, 10);
		String[] lines = document.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			g.drawChars(line.toCharArray(), 0, line.length(), 0, 12 * (i + 1));
		}

	}

	@Override
	public void updateUI() {
		setUI(UIManager.getUI(this));
	}

	@Override
	public String getUIClassID() {
		return ID;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.VK_BACK_SPACE && keyChar != KeyEvent.VK_DELETE && keyChar != KeyEvent.CHAR_UNDEFINED) {
			document += keyChar;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			int endIndex = document.length() - 1;
			document = document.substring(0, endIndex < 0 ? 0 : endIndex);
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
