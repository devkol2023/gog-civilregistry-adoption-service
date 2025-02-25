package com.gog.civilregistry.adoption.model;

import java.sql.Time;
import java.util.Date;

import lombok.Data;

@Data
public class ChildInformation {

	private String childFirstName;
	private String childMiddleName;
	private String childLastName;
	private String dob; // conflicting db type
	private Time timeOfBirth;
	private String outcome;
	private Integer gender; // conflicting db type
	private Short birthWeightGram; // conflicting db type - Short
	private String ageOfDeadInfant;
	private Short birthOrder;
	private Short noOfPreviousLiveBirths;
	private Integer intervalLastBirthUnitId;
	private Short intervalLastBirthValue;
	private String birthRegistrationNumber;
	private Integer childCitizenId;
	private String childCivilRegistryNumber;
	private Short noInBirth;
	private Short noPrevLiveBirth;
	private Short yearOfBirth;
	private Long applicationNodChildId;
	private String entryNo;

}
