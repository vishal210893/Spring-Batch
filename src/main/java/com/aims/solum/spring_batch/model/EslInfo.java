package com.aims.solum.spring_batch.model;

import lombok.Data;

@Data
public class EslInfo{
	private CameraConfig cameraConfig;
	private int sequence;
	private String txSequence;
	private FilenameConfig filenameConfig;
	private WifiConfig wifiConfig;
	private String customerCode;
	private SLabelType sLabelType;
	private UploadConfig uploadConfig;
	private String storeCode;
}