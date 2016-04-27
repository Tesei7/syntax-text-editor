package ru.tesei7.textEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import ru.tesei7.textEditor.editor.Language;
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
		String actionCommand = e.getActionCommand();

		switch (actionCommand) {
		case MenuActions.EXIT:
			System.exit(0);
			break;
		case MenuActions.NEW:
			app.loadFile = null;
			app.frame.setTitle(Application.TITLE);
			app.textArea.setText("");
			break;
		case MenuActions.OPEN:
			LoadedFile loadFile = saveLoadFileService.loadFile(app.contentPane);
			changeFile(loadFile);
			if (app.loadFile != null) {
				app.textArea.setText(app.loadFile.getData(), getLanguage(app.loadFile.getExtension()));
			}
			break;
		case MenuActions.SAVE:
			if (app.loadFile != null && app.loadFile.getFile() != null) {
				save();
			} else {
				saveAs();
			}
			break;
		case MenuActions.SAVE_AS:
			saveAs();
			break;
		case MenuActions.DEFAULT_VIEW:
			app.textArea.setViewMode(SyntaxTextEditorViewMode.DEFAULT);
			break;
		case MenuActions.FIXED_WIDTH_VIEW:
			app.textArea.setViewMode(SyntaxTextEditorViewMode.FIXED_WIDTH);
			break;
		}
	}

	private void save() {
		try {
			FileUtils.writeByteArrayToFile(app.loadFile.getFile(), app.textArea.getText().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveAs() {
		LoadedFile saveFile = saveLoadFileService.saveFile(app.contentPane, app.textArea.getText());
		changeFile(saveFile);
	}

	private void changeFile(LoadedFile saveFile) {
		app.loadFile = saveFile;
		if (app.loadFile != null) {
			app.frame.setTitle(Application.TITLE + " - " + app.loadFile.getFileName());
		}
	}

	private Language getLanguage(String extension) {
		switch (extension) {
		case "java":
			return Language.JAVA;
		case "jS":
			return Language.JAVA_SCRIPT;
		default:
			return Language.PLAIN_TEXT;
		}
	}

}
