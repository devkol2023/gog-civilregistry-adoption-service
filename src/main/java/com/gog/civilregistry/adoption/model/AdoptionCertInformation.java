package com.gog.civilregistry.adoption.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AdoptionCertInformation {
	
	private String applicationNo;
	private Long applicationAdoptionCertificateId;
	private String adoptionCertificateNumber;
	private String adoptionRegistrationNumber;
	private Long applicationAdoptionId;
	private Long applicationRegisterId;
	private Integer motherParish;
	private float feesPerCopy;
	private Short noOfCopies;
	private String paymentMode;
	private Short paymentStatus;
	private String remarks;
	private float totalAmount;
	private Integer currentStageId;
	private String applicationTypeCode;
	private Integer applicationTypeId;
	private String paymentReceiptNumber;

}
