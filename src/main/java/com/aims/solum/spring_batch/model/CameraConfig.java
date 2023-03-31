package com.aims.solum.spring_batch.model;

import lombok.Data;

@Data
public class CameraConfig{
	private int brightness;
	private int iso;
	private int imgSize;
	private int expTime;
	private int contrast;
	private int obstacleDetectVal;
	private int quality;
}