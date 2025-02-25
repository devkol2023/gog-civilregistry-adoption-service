package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class UpdateCertificateFileRequest {
	
	private Integer citizenId;
	private Integer currentUserId;
	private String dmsRefId;
	private String applicationTypeCode;
	

}
