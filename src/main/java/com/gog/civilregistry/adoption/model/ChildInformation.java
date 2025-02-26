package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class ChildInformation {

	private String childFirstName;
	private String childMiddleName;
	private String childSurname;
	private String dob; // child date of birth

	private Integer childGender;
	private Short childParish;

	private Integer childCitizenId;
	private String childCivilRegistryNumber;
	private Integer fatherCitizenIdOld;
	private String fatherFirstNameOld;
	private String fatherMiddleNameOld;
	private String fatherSurnameOld;
	private String fatherCivilRegistryNumberOld;

	private Integer motherCitizenIdOld;
	private String motherFirstNameOld;
	private String motherMiddleNameOld;
	private String motherMaidenSurnameOld;
	private String motherCivilRegistryNumberOld;

}
