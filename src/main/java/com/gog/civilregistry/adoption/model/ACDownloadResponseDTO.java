package com.gog.civilregistry.adoption.model;


public interface ACDownloadResponseDTO {
	
	Integer getChildCitizenId();
	Integer getMotherCitizenId();
	Integer getFatherCitizenId();
	String getChildCivilRegistryNumber(); 
	String getChildName();
	String getMotherName(); 
	String getFatherName(); 
	String getDateOfBirth(); 
	String getParishOfBirth(); 
	String getGender(); 
	String getAdoptionCertificateNumber(); 
	String getAdoptionCertificateDmsId(); 

}
