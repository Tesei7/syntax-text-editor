package ru.tesei7.textEditor;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

public class SaveLoadFileService {

	public LoadedFile loadFile(JPanel parent) {
		JFileChooser chooser = new JFileChooser();
		int val = chooser.showOpenDialog(parent);
		if (val == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try {
				String data = FileUtils.readFileToString(selectedFile, "UTF-8");
				return new LoadedFile(data, selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public LoadedFile saveFile(JPanel parent, String text) {
		JFileChooser chooser = new JFileChooser();
		int val = chooser.showSaveDialog(parent);
		if (val == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try {
				FileUtils.writeByteArrayToFile(selectedFile, text.getBytes("UTF-8"));
				return new LoadedFile(selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

}
