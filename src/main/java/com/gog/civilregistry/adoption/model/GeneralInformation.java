package com.gog.civilregistry.adoption.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class GeneralInformation {

//	private String instituteName;
	private Integer instituteId;
//	private String instituteAddress;
	private Integer instituteParish;
	private Long applicationRegisterId;
	private Long entryNo;
	private Long nodEntryNo;
	private String marginalNote;
	private String modeOfSignature;
	private String remarks;
	private Integer currentStatus;
	private Short isSingleParent;
	private Short isBothParentDeceased;
	private String applyMode;
	private Long applicationAdoptionId;
	private String applicationNo;
	private Integer currentStageId;
	private String cod; // court order date
	private String applicationTypeCode;
	private Integer applicationTypeId;

}
