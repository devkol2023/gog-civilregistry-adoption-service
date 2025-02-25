package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class TrackAppUserDto {
	
	private String applicationNo;
	private String childCivilRegistryId;
    private String childName;
    private String motherName;
    private String fatherName;
	private String dateOfBirth;
	private String status;
	private String applicationType;

}
