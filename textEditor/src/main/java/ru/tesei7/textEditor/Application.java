package ru.tesei7.textEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import ru.tesei7.textEditor.editor.document.model.SyntaxTextEditorViewMode;

public class Application implements ActionListener {
	private JPanel contentPane;
	private SyntaxTextEditor textArea;
	private JFrame frame;

	public void createAndShowGUI() {
		frame = new JFrame("Syntax Text Editor");
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
		
		menu = new JMenu("View");
		menuBar.add(menu);
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(MenuActions.DEFAULT_VIEW);
		rbMenuItem.setSelected(true);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);
		rbMenuItem.addActionListener(this);

		rbMenuItem = new JRadioButtonMenuItem(MenuActions.FIXED_WIDTH_VIEW);
		rbMenuItem.addActionListener(this);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);
		
		return menuBar;
	}

	public Container createContentPane() {
		contentPane = new JPanel(new BorderLayout());
//		contentPane.setBackground(Color.GREEN);
		contentPane.setOpaque(true);
		textArea = new SyntaxTextEditor();
		textArea.setText("class 12345678901234567890123456789012345678901234567890123456789012345678901234567890\n"
//				+ "2\n3\n4\n5\n6\n7\n8\n9\n0\n" + "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n" + "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n"
//				+ "1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n"
				);
//		GridBagConstraints c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = 0;
//		c.fill = GridBagConstraints.BOTH;
		contentPane.add(textArea, BorderLayout.CENTER);
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
		case MenuActions.DEFAULT_VIEW:
			textArea.setViewMode(SyntaxTextEditorViewMode.DEFAULT);
			break;
		case MenuActions.FIXED_WIDTH_VIEW:
			textArea.setViewMode(SyntaxTextEditorViewMode.FIXED_WIDTH);
			break;
		}
	}
}
