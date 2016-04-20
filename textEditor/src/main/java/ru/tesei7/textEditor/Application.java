package ru.tesei7.textEditor;

import java.awt.FlowLayout;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.JFrame;

import ru.tesei7.textEditor.app.TextEditor;

@ApplicationScoped
public class Application {
	@Inject
	private TextEditor editor;
	
	public void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Text Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setSize(800, 600);

		// Create and set up the content pane.
		frame.setJMenuBar(editor.createMenuBar());
		frame.setContentPane(editor.createContentPane());
		frame.setVisible(true);
		// frame.pack();
	}
}
