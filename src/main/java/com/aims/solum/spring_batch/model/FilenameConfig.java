package com.aims.solum.spring_batch.model;

import lombok.Data;

@Data
public class FilenameConfig{
	private String shelfId;
	private String linkedShelfID;
	private String store;
	private int cameraIndex;
}