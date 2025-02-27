package com.gog.civilregistry.adoption.model;

public interface BirthCertificateApplicationResponseProjection {
	
Integer getApplicationRegisterId();
	
	String getApplicationNo();
	
	String getAdoptionRegistrationNumber();
	
	Integer getChildCitizenId();

	String getChildCivilRegistryNumber();

	String getChildName();
	
	String getMotherName();
	
	String getFatherName();

	String getDateOfBirth();

	String getParishOfChild();

	Integer getIsAdoptionCertificateApprove();

}
