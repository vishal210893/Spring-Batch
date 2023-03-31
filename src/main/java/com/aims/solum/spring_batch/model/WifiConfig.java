package com.aims.solum.spring_batch.model;

import lombok.Data;

@Data
public class WifiConfig{
	private String password;
	private String keyType;
	private String sSID;
}