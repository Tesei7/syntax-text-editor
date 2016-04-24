package ru.tesei7.textEditor.app;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ru.tesei7.textEditor.app.menuActions.MenuActions;
import ru.tesei7.textEditor.app.menuActions.SaveLoadFileService;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class TextEditor implements ActionListener {
	private JPanel contentPane;
	private SyntaxTextEditor textArea;

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem openItem = new JMenuItem(MenuActions.OPEN, KeyEvent.VK_O);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		openItem.addActionListener(this);
		menu.add(openItem);
		JMenuItem saveItem = new JMenuItem(MenuActions.SAVE, KeyEvent.VK_S);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveItem.addActionListener(this);
		menu.add(saveItem);

		menu.addSeparator();

		JMenuItem exitItem = new JMenuItem(MenuActions.EXIT);
		exitItem.addActionListener(this);
		menu.add(exitItem);
		return menuBar;
	}

	public Container createContentPane() {
		contentPane = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		contentPane.setOpaque(true);
		textArea = new SyntaxTextEditor();
		textArea.setText("12345678901234567890123456789012345678901234567890123456789012345678901234567890\n"
				+ "2\n3\n4\n5\n6\n7\n8\n9\n0\n" + "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n" + "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n"
				+ "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n");
		contentPane.add(textArea);
		return contentPane;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		SaveLoadFileService saveLoadFileService = new SaveLoadFileService();
		switch (actionCommand) {
		case MenuActions.EXIT:
			System.exit(0);
			break;
		case MenuActions.OPEN:
			textArea.setText(saveLoadFileService.loadFile(contentPane));
			break;
		case MenuActions.SAVE:
			saveLoadFileService.saveFile(contentPane, textArea.getText());
			break;
		}
	}

}
