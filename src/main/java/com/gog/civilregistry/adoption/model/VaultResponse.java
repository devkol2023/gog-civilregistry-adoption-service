package com.gog.civilregistry.adoption.model;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class VaultResponse {

	private Long applicationRegisterId;
	private String applicationNumber;
	private String childName;
	private String motherName;
	private String fatherName;
	private Date dateOfBirth;
	private String applicationTypeCode;
	private String createdBy;
	private LocalDate createdOn;
	private Integer currentStageId;	
	private Integer applicationTypeId;
	private String applicationTypeShortCode;
	private Integer childCitizenId;
	private Integer motherCitizenId;
    private Integer fatherCitizenId;
	private String childCivilRegistryNumber;
	private Integer assignedFromUser;
	private String assignedFromRole;
	private Integer isReleaseApplicable; 
	private Integer isOnBehalf; 
	private String parishOfBirth; 
	private Integer totalCount;
}
