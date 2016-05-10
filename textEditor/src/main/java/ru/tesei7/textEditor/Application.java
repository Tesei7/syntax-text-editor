package ru.tesei7.textEditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;

public class Application {
	static final String TITLE = "Syntax Text Editor";

	private SyntaxTextEditor textArea;
	private JFrame frame;
	private LoadedFile loadFile;
	private ApplicationActionListener actionListener;

	private JRadioButtonMenuItem plainTextMenuItem;
	private JRadioButtonMenuItem javaMenuItem;
	private JRadioButtonMenuItem jsMenuItem;

	public void createAndShowGUI() {
		frame = new JFrame(TITLE + " - New Document");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (!textArea.isDirty()) {
					System.exit(0);
				} else {
					String ObjButtons[] = { "Yes", "No" };
					String message = "Exit and loose unsaved changes?";
					int PromptResult = JOptionPane.showOptionDialog(null, message, TITLE, JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
			}
		});
		frame.setLayout(new FlowLayout());

		// Create and set up the content pane.
		frame.setJMenuBar(createMenuBar());
		frame.setContentPane(createContentPane());
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.addComponentListener(new ResizeListener(textArea));
	}

	public Container createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);
		textArea = new SyntaxTextEditor();
		textArea.addDirtyStateListener((e) -> changeTitle());
		contentPane.add(textArea, BorderLayout.CENTER);
		return contentPane;
	}

	public JMenuBar createMenuBar() {
		actionListener = new ApplicationActionListener(this);
		JMenuBar menuBar = new JMenuBar();
		createFileMenu(menuBar);
		createViewMenu(menuBar);
		createSyntaxMenu(menuBar);
		return menuBar;
	}

	private void createSyntaxMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Syntax");
		menuBar.add(menu);
		ButtonGroup group = new ButtonGroup();
		plainTextMenuItem = new JRadioButtonMenuItem(MenuActions.PLAIN_TEXT);
		plainTextMenuItem.setSelected(true);
		group.add(plainTextMenuItem);
		menu.add(plainTextMenuItem);
		plainTextMenuItem.addActionListener(actionListener);

		javaMenuItem = new JRadioButtonMenuItem(MenuActions.JAVA);
		javaMenuItem.addActionListener(actionListener);
		group.add(javaMenuItem);
		menu.add(javaMenuItem);

		jsMenuItem = new JRadioButtonMenuItem(MenuActions.JAVA_SCRIPT);
		jsMenuItem.addActionListener(actionListener);
		group.add(jsMenuItem);
		menu.add(jsMenuItem);
	}

	private void createViewMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("View");
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
	}

	private void createFileMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem newItem = new JMenuItem(MenuActions.NEW, KeyEvent.VK_N);
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
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
	}

	void selectSyntaxMenuItem(Language language) {
		switch (language) {
		case PLAIN_TEXT:
			plainTextMenuItem.setSelected(true);
			break;
		case JAVA:
			javaMenuItem.setSelected(true);
			break;
		case JAVA_SCRIPT:
			jsMenuItem.setSelected(true);
			break;
		}
	}

	void changeTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append(Application.TITLE);
		sb.append(" - ");
		if (loadFile != null) {
			sb.append(loadFile.getFileName());
		} else {
			sb.append("New Document");
		}
		if (textArea.isDirty()) {
			sb.append(" *");
		}
		frame.setTitle(sb.toString());
	}

	SyntaxTextEditor getTextArea() {
		return textArea;
	}

	LoadedFile getLoadFile() {
		return loadFile;
	}

	void setLoadFile(LoadedFile loadFile) {
		this.loadFile = loadFile;
	}

	JFrame getFrame() {
		return frame;
	}

	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

}
