package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class ACDownloadResponse {
	
	private Integer childCitizenId;
	private Integer motherCitizenId;
	private Integer fatherCitizenId;
	private String childCivilRegistryNumber;
    private String childName;
    private String motherName;
    private String fatherName;
    private String dateOfBirth;  
    private String parishOfBirth;
    private String gender;
    private String adoptionCertificateNumber;
    private String adoptionCertificateDmsId;

}
