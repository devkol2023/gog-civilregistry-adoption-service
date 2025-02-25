package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class SearchApplicationARRequest {
	
	private String applicationTypeCode;
	private String applicationNumber;
	private String childName;
    private String motherName;
	private String fatherName;
	private String dateOfBirthStr;
	private Integer parishId;
	private Integer genderId;
	
}