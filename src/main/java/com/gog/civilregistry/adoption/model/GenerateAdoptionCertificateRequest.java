package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class GenerateAdoptionCertificateRequest {
	
	private Long applicationRegisterId;
	private Integer userId;
	private Integer roleId;
	private String roleCode;

}