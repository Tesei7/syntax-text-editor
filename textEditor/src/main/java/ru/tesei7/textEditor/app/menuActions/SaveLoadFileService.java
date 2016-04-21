package ru.tesei7.textEditor.app.menuActions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SaveLoadFileService {

	public String loadFileAsText(JPanel parent) {
		JFileChooser chooser = new JFileChooser();
		int val = chooser.showOpenDialog(parent);
		if (val == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try {
				return FileUtils.readFileToString(selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		} else {
			return "";
		}
	}

}
