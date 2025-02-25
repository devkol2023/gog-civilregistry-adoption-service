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
	private String motherName;
	private String fatherName;
	private Long childCount;
	private String childName;
	private Date deliveryDate;
	private String applicationTypeCode;
	private String createdBy;
	private LocalDate createdOn;
	private String location;
	private String instituteName;
	private String instituteParish;
	private Integer currentStageId;
	private Integer isReleaseApplicable;
	private Integer applicationTypeId;
	private String applicationTypeShortCode;
	private Integer isBirthCertificateApproved;
	private Integer citizenId;
	private String civilRegistryNumber;
	private Integer isOnBehalf;
	private Date dateOfBirth;
	private String parishOfBirth;
	private Integer assignedFromUser;
	private String assignedFromRoleCode;
	private Integer totalCount;
}
