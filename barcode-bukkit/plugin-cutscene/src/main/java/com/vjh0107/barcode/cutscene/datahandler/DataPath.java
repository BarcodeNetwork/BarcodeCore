package com.vjh0107.barcode.cutscene.datahandler;

public class DataPath {

	private String fileName;
	private String[] filePath;
	
	public DataPath(String fileName, String... filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String[] getFilePath() {
		return filePath;
	}
}
