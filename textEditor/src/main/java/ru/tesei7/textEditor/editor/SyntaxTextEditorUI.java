package ru.tesei7.textEditor.editor;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class SyntaxTextEditorUI extends ComponentUI {

	public static ComponentUI createUI(JComponent ta) {
		return new SyntaxTextEditorUI(ta);
	}

	public SyntaxTextEditorUI(JComponent rSyntaxTextArea) {
		super();
		// TODO Auto-generated constructor stub
	}

}
