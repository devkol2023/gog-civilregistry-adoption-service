package com.gog.civilregistry.adoption.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApprovedAdoptionCertDetails {
	
	@JsonProperty("certificate_id")
	private Long applicationCertificateId;
	
	@JsonProperty("certificate_no")
	private String marriageCertificateNumber;


}
