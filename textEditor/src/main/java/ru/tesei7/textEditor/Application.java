package ru.tesei7.textEditor;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import ru.tesei7.textEditor.app.TextEditor;

public class Application {

	public void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Text Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());

		// Create and set up the content pane.
		TextEditor editor = new TextEditor();
		frame.setJMenuBar(editor.createMenuBar());
		frame.setContentPane(editor.createContentPane());
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
	}
}
