package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class FatherInformation {

	private Short isFatherInfoAvailable; // conflicting DB type
	private String fatherFirstName;
	private String fatherMiddleName;
	private String fatherSurname;
	private String fatherAddress;
	private Integer fatherParish;
	private Short fatherAge;
	private String fatherQualification;
	private String fatherOccupation;
	private String fatherReligion;
	private Integer fatherCitizenId; // conflictiong DB type
	private String fatherCivilRegistryNumber;
	private String fatherMobileNumber; // conflictiong DB type
	private String fatherEmailId;
	private String fatherMaritalStatus;
	private Integer fatherVillageTown;
	private String fatherDOB;

}
