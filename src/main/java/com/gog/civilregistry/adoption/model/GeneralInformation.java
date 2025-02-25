package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class GeneralInformation {

	private String instituteName;
	private Integer instituteId;
	private String instituteAddress;
	private Integer instituteParish;
//	private String deliDatStr; // conflicting DB type
//	private String placeOfBirth;
//	private Short isBirthWithinHospital; // conflicting db type
	private Long applicationRegisterId;
	private Long entryNo;
//	private String marginalNote;
	private String modeOfSignature;
//	private Integer parishOfBirth;
	private String remarks;
	private Integer status;

	private String parishCode;
	private Long applicationAdoptionId;
	private String applicationNo;
//	private String applicationNoNod;
	private Integer currentStageId;

	private String applicationTypeCode;
	private Integer applicationTypeId;

}
