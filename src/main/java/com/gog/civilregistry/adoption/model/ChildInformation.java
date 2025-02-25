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

}
