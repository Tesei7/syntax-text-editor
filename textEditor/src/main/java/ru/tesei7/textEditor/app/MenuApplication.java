package ru.tesei7.textEditor.app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import ru.tesei7.textEditor.app.menuActions.LoadFileService;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class MenuApplication implements ActionListener {
	private LoadFileService loadFileService = new LoadFileService();
	private SyntaxTextEditor textArea;
	private JScrollPane scrollPane;

	public JMenuBar createMenuBar() {
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem openItem = new JMenuItem(MenuActions.OPEN, KeyEvent.VK_O);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_DOWN_MASK));
		openItem.addActionListener(this);
		menu.add(openItem);
		JMenuItem saveItem = new JMenuItem(MenuActions.SAVE, KeyEvent.VK_S);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_DOWN_MASK));
		saveItem.addActionListener(this);
		menu.add(saveItem);

		menu.addSeparator();

		JMenuItem exitItem = new JMenuItem(MenuActions.EXIT);
		exitItem.addActionListener(this);
		menu.add(exitItem);
		return menuBar;
	}

	public Container createContentPane() {
		// Create the content-pane-to-be.
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);

		// Create a scrolled text area.
//		textArea = new RSyntaxTextArea(40, 120);
//		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
//		textArea.setCodeFoldingEnabled(true);
		textArea = new SyntaxTextEditor();
//		scrollPane = new RTextScrollPane(textArea);
		scrollPane = new JScrollPane(textArea);
//		scrollPane.setBounds(50, 30, 300, 50);

		// Add the text area to the content pane.
		contentPane.add(scrollPane, BorderLayout.CENTER);
//		contentPane.setPreferredSize(new Dimension(500, 400));
		return contentPane;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
		case MenuActions.EXIT:
			System.exit(0);
			break;
		case MenuActions.OPEN:
			String text = loadFileService.loadFileAsText();
//			textArea.append(text);
			break;
		case MenuActions.SAVE:
			System.exit(0);
			break;
		default:
			break;
		}
	}

}
