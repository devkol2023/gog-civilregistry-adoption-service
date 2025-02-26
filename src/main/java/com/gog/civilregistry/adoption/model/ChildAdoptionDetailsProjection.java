package com.gog.civilregistry.adoption.model;

public interface ChildAdoptionDetailsProjection {

	// tcr.citizen_id as childCitizenId,
//	tcr.citizen_civil_registry_number as childCivilRegistryNumber,
//	tcr.first_name as childFirstName,
//	tcr.last_name as childSurname,
//	tcr.middle_name as childMiddleName,
//	TO_CHAR(tcr.date_of_birth, 'DD/MM/YYYY')as  dateOfBirth,
//	mg.geography_name as parishOfBirth,
//	mmdv.data_description as gender,
//	select tcr.father_citizen_id as fatherCitizenId, 
//	tcr.father_civil_registry_number as fatherCivilRegistryNumber, 
//	tcr.father_first_name as fatherFirstName,
//	tcr.father_middle_name as fatherMiddleName,
//	 tcr.father_last_name as fatherSurname,
//	 tcr.mother_citizen_id as motherCitizenId,
//	 tcr.mother_civil_registry_number as motherCivilRegistryNumber,
//	 tcr.mother_first_name as motherFirstName,
//	 tcr.mother_middle_name as motherMiddleName,
//	 tcr.mother_last_name as motherSurname,
//	 tcr.mother_maiden_surname as motherMaidenSurname
//	from citizen.t_citizen_register tcr 
//	where tcr.citizen_civil_registry_number = :civilRegistryNumber

	Integer getChildCitizenId();

	String getChildCivilRegistryNumber();

	String getChildFirstName();

	String getChildSurname();

	String getChildMiddleName();

	String getDateOfBirth();

	Integer getParishOfBirth();

	Integer getGender();

	Integer getFatherCitizenId();

	String getFatherCivilRegistryNumber();

	String getFatherFirstName();

	String getFatherMiddleName();

	String getFatherSurname();

	Integer getMotherCitizenId();

	String getMotherCivilRegistryNumber();

	String getMotherFirstName();

	String getMotherMiddleName();

	String getMotherSurname();

	String getMotherMaidenSurname();
}
