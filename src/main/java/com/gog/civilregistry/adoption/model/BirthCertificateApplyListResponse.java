package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class BirthCertificateApplyListResponse {
	
	private Integer applicationRegisterId;
    private String applicationNo;  
    private String adoptionRegistrationNumber;
    private Integer childCitizenId;
    private String childCivilRegistryNumber;
    private String childName;
    private String motherName;
    private String fatherName;
    private String dateOfBirth;  
    private String parishOfChild;
    private Integer isAdoptionCertificateApprove;

}
