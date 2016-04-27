package ru.tesei7.textEditor;

import java.io.File;

public class LoadedFile {
	private String data;
	private File file;

	public LoadedFile(String data, File file) {
		this.data = data;
		this.file = file;
	}

	public LoadedFile(File file) {
		this(null, file);
	}

	public String getData() {
		return data;
	}

	public File getFile() {
		return file;
	}

	public String getFileName() {
		if (file == null) {
			return "";
		}
		return file.getName();
	}

	public String getExtension() {
		String fileName = getFileName();
		if (fileName == null) {
			return "";
		}
		int lastIndexOf = fileName.lastIndexOf(".");
		if (lastIndexOf < 0) {
			return "";
		}
		return fileName.substring(lastIndexOf + 1).toLowerCase();
	}

}
