package com.aims.solum.spring_batch.model;

import lombok.Data;

@Data
public class UploadConfig{
	private String bucket;
	private String hostname;
	private int uploadMode;
	private String secret;
	private String key;
}