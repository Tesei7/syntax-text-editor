package ru.tesei7.textEditor;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import ru.tesei7.textEditor.app.MenuApplication;

public class Main {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	protected static void createAndShowGUI() {
		//let the ui manager know of our component
		// the value must be the fully qualified classname
		UIManager.put("SyntaxTextEditorUI", "ru.tesei7.textEditor.editor.ui.SyntaxTextEditorUI");
		// Create and set up the window.
		JFrame frame = new JFrame("Text Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setSize(800, 600);
				
		//Create and set up the content pane.
        MenuApplication app = new MenuApplication();
        frame.setJMenuBar(app.createMenuBar());
        frame.setContentPane(app.createContentPane());
		frame.setVisible(true);
//		frame.pack();
	}
}
