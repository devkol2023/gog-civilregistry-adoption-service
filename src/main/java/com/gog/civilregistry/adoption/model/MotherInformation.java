package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class MotherInformation {

	private String motherFirstName;
	private String motherMiddleName;
	private String motherSurname;
	private String motherMaidenSurname;
	private String motherAddress;
	private Integer motherParish;
	private Short motherAge;
	private String motherMaritalStatus;
	private String motherQualification;
	private String motherOccupation;
	private String motherReligion;
	private String motherDOB;

	private Integer motherCitizenId; // conflictiong DB type
	private String motherCivilRegistryNumber;
	private String motherMobileNumber; // conflictiong DB type
	private String motherEmailId;
	private Integer motherVillageTown;

}
