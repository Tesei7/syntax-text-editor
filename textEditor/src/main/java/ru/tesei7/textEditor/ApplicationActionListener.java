package ru.tesei7.textEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import ru.tesei7.textEditor.editor.Language;
import ru.tesei7.textEditor.editor.SyntaxTextEditor;
import ru.tesei7.textEditor.editor.document.model.SyntaxTextEditorViewMode;

public class ApplicationActionListener implements ActionListener {

	private Application app;
	private SaveLoadFileService saveLoadFileService;

	public ApplicationActionListener(Application application) {
		this.app = application;
		saveLoadFileService = new SaveLoadFileService();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SyntaxTextEditor textArea = app.getTextArea();
		String actionCommand = e.getActionCommand();

		switch (actionCommand) {
		case MenuActions.EXIT:
			System.exit(0);
			break;
		case MenuActions.NEW:
			newFile();
			break;
		case MenuActions.OPEN:
			open();
			break;
		case MenuActions.SAVE:
			LoadedFile loadFile = app.getLoadFile();
			if (loadFile != null && loadFile.getFile() != null) {
				save();
			} else {
				saveAs();
			}
			break;
		case MenuActions.SAVE_AS:
			saveAs();
			break;
		case MenuActions.DEFAULT_VIEW:
			textArea.setViewMode(SyntaxTextEditorViewMode.DEFAULT);
			break;
		case MenuActions.FIXED_WIDTH_VIEW:
			textArea.setViewMode(SyntaxTextEditorViewMode.FIXED_WIDTH);
			break;
		case MenuActions.PLAIN_TEXT:
			changeSyntax(Language.PLAIN_TEXT);
			break;
		case MenuActions.JAVA:
			changeSyntax(Language.JAVA);
			break;
		case MenuActions.JAVA_SCRIPT:
			changeSyntax(Language.JAVA_SCRIPT);
			break;
		}
	}

	private void changeSyntax(Language language) {
		new ProgressDialog(null, "Changing syntax...", () -> app.getTextArea().setLanguage(language));
	}

	void newFile() {
		if (!app.getTextArea().isDirty()) {
			newFileWithoutPromt();
		} else {
			String ObjButtons[] = { "Yes", "No" };
			String message = "Create new file and loose unsaved changes?";
			String title = "New document";
			int PromptResult = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
			if (PromptResult == JOptionPane.YES_OPTION) {
				newFileWithoutPromt();
			}
		}
	}

	private void newFileWithoutPromt() {
		SyntaxTextEditor textArea = app.getTextArea();
		app.setLoadFile(null);
		textArea.setText("", textArea.getLanguage());
		app.selectSyntaxMenuItem(textArea.getLanguage());
	}

	void open() {
		if (!app.getTextArea().isDirty()) {
			openWithoutPromt();
		} else {
			String ObjButtons[] = { "Yes", "No" };
			String message = "Open file and loose unsaved changes?";
			String title = "Open document";
			int PromptResult = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
			if (PromptResult == JOptionPane.YES_OPTION) {
				openWithoutPromt();
			}
		}
	}

	private void openWithoutPromt() {
		LoadedFile loadFile = saveLoadFileService.loadFile(null);
		app.setLoadFile(loadFile);
		if (loadFile != null) {
			Language language = getLanguage(loadFile.getExtension());
			new ProgressDialog(null, "Loading file...", () -> app.getTextArea().setText(loadFile.getData(), language));
			app.selectSyntaxMenuItem(language);
		}
	}

	void save() {
		try {
			FileUtils.writeByteArrayToFile(app.getLoadFile().getFile(), app.getTextArea().getText().getBytes("UTF-8"));
			app.getTextArea().clearDirty();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void saveAs() {
		LoadedFile saveFile = saveLoadFileService.saveFile(null, app.getTextArea().getText());
		if (saveFile != null) {
			app.setLoadFile(saveFile);
			app.getTextArea().clearDirty();
		}
	}

	Language getLanguage(String extension) {
		switch (extension) {
		case "java":
			return Language.JAVA;
		case "js":
			return Language.JAVA_SCRIPT;
		default:
			return Language.PLAIN_TEXT;
		}
	}

}
