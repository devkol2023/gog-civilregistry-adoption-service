package com.gog.civilregistry.adoption.dto;

public interface GenerateAdoptionCertificateDTO {
	
	String getEntry_no(); 
	String getChild_gender(); 
	String getChild_name();
	String getFather_name(); 
	String getFather_occupation(); 
	String getFather_address();
	String getMother_name(); 
	String getMother_occupation(); 
	String getMother_address();
	String getChild_date_of_birth(); 
	String getPlace_of_birth(); 
	String getCourt_order_date(); 
	String getAdoption_registration_number(); 
	String getAdoption_certificate_number();
	String getNod_entry_no();
	String getAdoption_certificate_approval_date();
	String getApproved_by_role();
	String getApproved_by_user();

}
