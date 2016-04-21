package ru.tesei7.textEditor.app.menuActions;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

public class SaveLoadFileService {
	
	public String loadFile(JPanel parent) {
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
	
	public void saveFile(JPanel parent, String text) {
		JFileChooser chooser = new JFileChooser();
		int val = chooser.showSaveDialog(parent);
		if (val == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try {
				FileUtils.writeByteArrayToFile(selectedFile, text.getBytes("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

}
