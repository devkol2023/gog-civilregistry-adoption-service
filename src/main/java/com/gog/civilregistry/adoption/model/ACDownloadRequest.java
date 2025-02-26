package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class ACDownloadRequest {
	
	private String childCivilRegistryNumber;
	private String childName;
    private String motherName;
    private String fatherName;
    private String dateOfBirthStr;
    private Integer genderId;
    private Integer parishId;

}