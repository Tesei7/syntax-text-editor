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
                app.close();
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

    void changeSyntax(Language language) {
        new ProgressDialog(app.getFrame(), "Changing syntax...",
                "Changing syntax, please wait", false, () -> app.getTextArea().setLanguage(language));
    }

    void newFile() {
        if (!app.getTextArea().isDirty()) {
            newFileWithoutPrompt();
        } else {
            String ObjButtons[] = {"Yes", "No"};
            String message = "Create new file and loose unsaved changes?";
            String title = "New document";
            int PromptResult = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
            if (PromptResult == JOptionPane.YES_OPTION) {
                newFileWithoutPrompt();
            }
        }
    }

    private void newFileWithoutPrompt() {
        SyntaxTextEditor textArea = app.getTextArea();
        app.setLoadFile(null);
        textArea.setText("", textArea.getLanguage());
        app.selectSyntaxMenuItem(textArea.getLanguage());
    }

    void open() {
        if (!app.getTextArea().isDirty()) {
            openWithoutPrompt();
        } else {
            String ObjButtons[] = {"Yes", "No"};
            String message = "Open file and loose unsaved changes?";
            String title = "Open document";
            int PromptResult = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
            if (PromptResult == JOptionPane.YES_OPTION) {
                openWithoutPrompt();
            }
        }
    }

    private void openWithoutPrompt() {
        LoadedFile loadFile = saveLoadFileService.loadFile(null);
        app.setLoadFile(loadFile);
        if (loadFile != null) {
            Language language = getLanguage(loadFile.getExtension());
            ProgressDialog d = new ProgressDialog(app.getFrame(), "Loading file...", "Loading file, please wait",
                    true, () -> app.getTextArea().setText(loadFile.getData(), language));
            // remove link to data, because it is not necessary to store it any more
            loadFile.clearData();
            if (d.getResult()) {
                app.selectSyntaxMenuItem(language);
            }
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
