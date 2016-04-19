package ru.tesei7.textEditor.editor.ui;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextUI;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaUI;

public class SyntaxTextEditorUI extends ComponentUI {
	
	public static ComponentUI createUI(JComponent ta) {
		return new SyntaxTextEditorUI(ta);
	}

    public SyntaxTextEditorUI(JComponent rSyntaxTextArea) {
		super();
		// TODO Auto-generated constructor stub
	}


}
