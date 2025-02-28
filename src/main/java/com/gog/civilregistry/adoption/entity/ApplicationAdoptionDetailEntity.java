package com.gog.civilregistry.adoption.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * The persistent class for the t_application_adoption_details database table.
 * 
 */
@Entity
@Data
@Table(name = "t_application_adoption_details", schema = "adoption")
@NamedQuery(name = "ApplicationAdoptionDetailEntity.findAll", query = "SELECT t FROM ApplicationAdoptionDetailEntity t")
public class ApplicationAdoptionDetailEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "application_adoption_id")
	private Long applicationAdoptionId;

	@Column(name = "application_register_id")
	private Long applicationRegisterId;

	@Column(name = "apply_mode")
	private String applyMode;

	@Column(name = "child_citizen_id")
	private Integer childCitizenId;

	@Column(name = "child_date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date childDateOfBirth;

	@Column(name = "father_date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date fatherDateOfBirth;

	@Column(name = "mother_date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date motherDateOfBirth;

	@Column(name = "child_first_name")
	private String childFirstName;

	@Column(name = "child_gender")
	private Integer childGender;

	@Column(name = "child_middle_name")
	private String childMiddleName;

	@Column(name = "child_parish")
	private Short childParish;

	@Column(name = "child_surname")
	private String childSurname;

	@Column(name = "court_order_date")
	@Temporal(TemporalType.DATE)
	private Date courtOrderDate;

	@Column(name = "entry_no")
	private String entryNo;

	@Column(name = "father_address")
	private String fatherAddress;

	@Column(name = "father_age")
	private Short fatherAge;

	@Column(name = "father_citizen_id")
	private Integer fatherCitizenId;

	@Column(name = "father_citizen_id_old")
	private Integer fatherCitizenIdOld;

	@Column(name = "father_email_id")
	private String fatherEmailId;

	@Column(name = "father_first_name")
	private String fatherFirstName;
	
	@Column(name = "father_first_name_old")
	private String fatherFirstNameOld;

	@Column(name = "father_marital_status")
	private String fatherMaritalStatus;

	@Column(name = "father_middle_name")
	private String fatherMiddleName;
	
	@Column(name = "father_middle_name_old")
	private String fatherMiddleNameOld;

	@Column(name = "father_mobile_number")
	private Long fatherMobileNumber;

	@Column(name = "father_occupation")
	private String fatherOccupation;

	@Column(name = "father_parish")
	private Short fatherParish;

	@Column(name = "father_village_town")
	private Integer fatherVillageTown;

	@Column(name = "mother_village_town")
	private Integer motherVillageTown;

	@Column(name = "father_qualification")
	private String fatherQualification;

	@Column(name = "father_religion")
	private String fatherReligion;

	@Column(name = "father_surname")
	private String fatherSurname;
	

	@Column(name = "father_surname_old")
	private String fatherSurnameOld;

	private Short half;

	@Column(name = "institute_id")
	private Short instituteId;

	@Column(name = "is_both_parent_deceased")
	private Short isBothParentDeceased;

	@Column(name = "is_single_parent")
	private Short isSingleParent;

	@Column(name = "marginal_note")
	private String marginalNote;

	@Column(name = "mode_of_signature")
	private String modeOfSignature;

	@Column(name = "mother_address")
	private String motherAddress;

	@Column(name = "mother_age")
	private Short motherAge;

	@Column(name = "mother_citizen_id")
	private Integer motherCitizenId;
	
	@Column(name = "mother_citizen_id_old")
	private Integer motherCitizenIdOld;

	@Column(name = "mother_email_id")
	private String motherEmailId;

	@Column(name = "mother_first_name")
	private String motherFirstName;
	
	@Column(name = "mother_first_name_old")
	private String motherFirstNameOld;

	@Column(name = "mother_maiden_surname")
	private String motherMaidenSurname;
	
	@Column(name = "mother_maiden_surname_old")
	private String motherMaidenSurnameOld;
	

	@Column(name = "mother_marital_status")
	private String motherMaritalStatus;

	@Column(name = "mother_middle_name")
	private String motherMiddleName;
	
	@Column(name = "mother_middle_name_old")
	private String motherMiddleNameOld;

	@Column(name = "mother_mobile_number")
	private Long motherMobileNumber;

	@Column(name = "mother_occupation")
	private String motherOccupation;

	@Column(name = "mother_parish")
	private Short motherParish;

	@Column(name = "mother_qualification")
	private String motherQualification;

	@Column(name = "mother_religion")
	private String motherReligion;

	@Column(name = "mother_surname")
	private String motherSurname;

	@Column(name = "nod_entry_no")
	private String nodEntryNo;

	private Short page;

	private String remarks;

	public ApplicationAdoptionDetailEntity() {
	}

}