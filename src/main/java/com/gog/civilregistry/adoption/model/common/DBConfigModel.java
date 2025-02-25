package com.gog.civilregistry.adoption.model.common;

import lombok.Data;

@Data
public class DBConfigModel {
	private String url;
	private String user;
	private String password;
	private Integer maxpoolsize;
	private Integer minPoolSize;
	private Long maxLifeTime;
	private Long idleTimeout;
	private Long connectionTimeout;
	private Long leakDetectionThreshold;

}
