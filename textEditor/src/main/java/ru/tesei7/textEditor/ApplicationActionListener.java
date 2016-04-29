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
			newFile();
			break;
		case MenuActions.OPEN:
			open();
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
		new ProgressDialog(app.frame, "Changing syntax...",  () -> app.textArea.setLanguage(language));
	}

	void newFile() {
		app.loadFile = null;
		app.textArea.setText("", app.textArea.getLanguage());
		changeTitle();
		app.selectSyntaxMenuItem(Language.PLAIN_TEXT);
	}

	void open() {
		LoadedFile loadFile = saveLoadFileService.loadFile(app.contentPane);
		app.loadFile = loadFile;
		if (app.loadFile != null) {
			Language language = getLanguage(app.loadFile.getExtension());
			new ProgressDialog(app.frame, "Loading file...",  () -> app.textArea.setText(app.loadFile.getData(), language));
			changeTitle();
			app.selectSyntaxMenuItem(language);
		}
	}

	void save() {
		try {
			FileUtils.writeByteArrayToFile(app.loadFile.getFile(), app.textArea.getText().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void saveAs() {
		LoadedFile saveFile = saveLoadFileService.saveFile(app.contentPane, app.textArea.getText());
		app.loadFile = saveFile;
		changeTitle();
	}

	void changeTitle() {
		if (app.loadFile != null) {
			app.frame.setTitle(Application.TITLE + " - " + app.loadFile.getFileName());
		} else {
			app.frame.setTitle(Application.TITLE + " - New Document");
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
