package ru.tesei7.textEditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class Application {
	static final String TITLE = "Syntax Text Editor";

	JPanel contentPane;
	SyntaxTextEditor textArea;
	JFrame frame;
	LoadedFile loadFile;
	private ApplicationActionListener actionListener;

	public void createAndShowGUI() {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());

		// Create and set up the content pane.
		frame.setJMenuBar(createMenuBar());
		frame.setContentPane(createContentPane());
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.addComponentListener(new ResizeListener(textArea));
	}

	public JMenuBar createMenuBar() {
		actionListener = new ApplicationActionListener(this);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem newItem = new JMenuItem(MenuActions.NEW, KeyEvent.VK_O);
		newItem.addActionListener(actionListener);
		menu.add(newItem);

		menu.addSeparator();

		JMenuItem openItem = new JMenuItem(MenuActions.OPEN, KeyEvent.VK_O);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		openItem.addActionListener(actionListener);
		menu.add(openItem);
		JMenuItem saveItem = new JMenuItem(MenuActions.SAVE, KeyEvent.VK_S);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveItem.addActionListener(actionListener);
		menu.add(saveItem);
		JMenuItem saveAsItem = new JMenuItem(MenuActions.SAVE_AS, KeyEvent.VK_S);
		saveAsItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		saveAsItem.addActionListener(actionListener);
		menu.add(saveAsItem);

		menu.addSeparator();

		JMenuItem exitItem = new JMenuItem(MenuActions.EXIT);
		exitItem.addActionListener(actionListener);
		menu.add(exitItem);

		menu = new JMenu("View");
		menuBar.add(menu);
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(MenuActions.DEFAULT_VIEW);
		rbMenuItem.setSelected(true);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);
		rbMenuItem.addActionListener(actionListener);

		rbMenuItem = new JRadioButtonMenuItem(MenuActions.FIXED_WIDTH_VIEW);
		rbMenuItem.addActionListener(actionListener);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		return menuBar;
	}

	public Container createContentPane() {
		contentPane = new JPanel(new BorderLayout());
		// contentPane.setBackground(Color.GREEN);
		contentPane.setOpaque(true);
		textArea = new SyntaxTextEditor();
		textArea.setText("class 12345678901234567890123456789012345678901234567890123456789012345678901234567890\n"
		// + "2\n3\n4\n5\n6\n7\n8\n9\n0\n" + "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n" +
		// "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n"
		// + "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n"
		);
		contentPane.add(textArea, BorderLayout.CENTER);
		return contentPane;
	}

}
