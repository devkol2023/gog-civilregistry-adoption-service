package com.gog.civilregistry.adoption.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the t_application_adoption_details database table.
 * 
 */
@Entity
@Table(name="t_application_adoption_details", schema = "adoption")
@NamedQuery(name="ApplicationAdoptionDetailEntity.findAll", query="SELECT t FROM ApplicationAdoptionDetailEntity t")
public class ApplicationAdoptionDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="application_adoption_id")
	private Long applicationAdoptionId;

	@Column(name="application_register_id")
	private Long applicationRegisterId;

	@Column(name="apply_mode")
	private String applyMode;

	@Column(name="child_citizen_id")
	private Integer childCitizenId;

	@Column(name="child_date_of_birth")
	private Timestamp childDateOfBirth;

	@Column(name="child_first_name")
	private String childFirstName;

	@Column(name="child_gender")
	private String childGender;

	@Column(name="child_middle_name")
	private String childMiddleName;

	@Column(name="child_parish")
	private Short childParish;

	@Column(name="child_surname")
	private String childSurname;

	@Column(name="court_order_date")
	private Timestamp courtOrderDate;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_on")
	private Timestamp createdOn;

	@Column(name="entry_no")
	private Integer entryNo;

	@Column(name="father_address")
	private String fatherAddress;

	@Column(name="father_age")
	private Short fatherAge;

	@Column(name="father_citizen_id")
	private Integer fatherCitizenId;

	@Column(name="father_email_id")
	private String fatherEmailId;

	@Column(name="father_first_name")
	private String fatherFirstName;

	@Column(name="father_marital_status")
	private String fatherMaritalStatus;

	@Column(name="father_middle_name")
	private String fatherMiddleName;

	@Column(name="father_mobile_number")
	private Long fatherMobileNumber;

	@Column(name="father_occupation")
	private String fatherOccupation;

	@Column(name="father_parish")
	private Short fatherParish;

	@Column(name="father_qualification")
	private String fatherQualification;

	@Column(name="father_religion")
	private String fatherReligion;

	@Column(name="father_surname")
	private String fatherSurname;

	private Short half;

	@Column(name="institute_id")
	private Short instituteId;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_both_parent_deceased")
	private Short isBothParentDeceased;

	@Column(name="is_single_parent")
	private Short isSingleParent;

	@Column(name="marginal_note")
	private String marginalNote;

	@Column(name="mode_of_signature")
	private String modeOfSignature;

	@Column(name="mother_address")
	private String motherAddress;

	@Column(name="mother_age")
	private Short motherAge;

	@Column(name="mother_citizen_id")
	private Integer motherCitizenId;

	@Column(name="mother_email_id")
	private String motherEmailId;

	@Column(name="mother_first_name")
	private String motherFirstName;

	@Column(name="mother_maiden_surname")
	private String motherMaidenSurname;

	@Column(name="mother_marital_status")
	private String motherMaritalStatus;

	@Column(name="mother_middle_name")
	private String motherMiddleName;

	@Column(name="mother_mobile_number")
	private Long motherMobileNumber;

	@Column(name="mother_occupation")
	private String motherOccupation;

	@Column(name="mother_parish")
	private Short motherParish;

	@Column(name="mother_qualification")
	private String motherQualification;

	@Column(name="mother_religion")
	private String motherReligion;

	@Column(name="mother_surname")
	private String motherSurname;

	@Column(name="nod_entry_no")
	private Integer nodEntryNo;

	private Short page;

	private String remarks;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_on")
	private Timestamp updatedOn;

	public ApplicationAdoptionDetailEntity() {
	}

	public Long getApplicationAdoptionId() {
		return applicationAdoptionId;
	}

	public void setApplicationAdoptionId(Long applicationAdoptionId) {
		this.applicationAdoptionId = applicationAdoptionId;
	}

	public Long getApplicationRegisterId() {
		return applicationRegisterId;
	}

	public void setApplicationRegisterId(Long applicationRegisterId) {
		this.applicationRegisterId = applicationRegisterId;
	}

	public String getApplyMode() {
		return applyMode;
	}

	public void setApplyMode(String applyMode) {
		this.applyMode = applyMode;
	}

	public Integer getChildCitizenId() {
		return childCitizenId;
	}

	public void setChildCitizenId(Integer childCitizenId) {
		this.childCitizenId = childCitizenId;
	}

	public Timestamp getChildDateOfBirth() {
		return childDateOfBirth;
	}

	public void setChildDateOfBirth(Timestamp childDateOfBirth) {
		this.childDateOfBirth = childDateOfBirth;
	}

	public String getChildFirstName() {
		return childFirstName;
	}

	public void setChildFirstName(String childFirstName) {
		this.childFirstName = childFirstName;
	}

	public String getChildGender() {
		return childGender;
	}

	public void setChildGender(String childGender) {
		this.childGender = childGender;
	}

	public String getChildMiddleName() {
		return childMiddleName;
	}

	public void setChildMiddleName(String childMiddleName) {
		this.childMiddleName = childMiddleName;
	}

	public Short getChildParish() {
		return childParish;
	}

	public void setChildParish(Short childParish) {
		this.childParish = childParish;
	}

	public String getChildSurname() {
		return childSurname;
	}

	public void setChildSurname(String childSurname) {
		this.childSurname = childSurname;
	}

	public Timestamp getCourtOrderDate() {
		return courtOrderDate;
	}

	public void setCourtOrderDate(Timestamp courtOrderDate) {
		this.courtOrderDate = courtOrderDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getEntryNo() {
		return entryNo;
	}

	public void setEntryNo(Integer entryNo) {
		this.entryNo = entryNo;
	}

	public String getFatherAddress() {
		return fatherAddress;
	}

	public void setFatherAddress(String fatherAddress) {
		this.fatherAddress = fatherAddress;
	}

	public Short getFatherAge() {
		return fatherAge;
	}

	public void setFatherAge(Short fatherAge) {
		this.fatherAge = fatherAge;
	}

	public Integer getFatherCitizenId() {
		return fatherCitizenId;
	}

	public void setFatherCitizenId(Integer fatherCitizenId) {
		this.fatherCitizenId = fatherCitizenId;
	}

	public String getFatherEmailId() {
		return fatherEmailId;
	}

	public void setFatherEmailId(String fatherEmailId) {
		this.fatherEmailId = fatherEmailId;
	}

	public String getFatherFirstName() {
		return fatherFirstName;
	}

	public void setFatherFirstName(String fatherFirstName) {
		this.fatherFirstName = fatherFirstName;
	}

	public String getFatherMaritalStatus() {
		return fatherMaritalStatus;
	}

	public void setFatherMaritalStatus(String fatherMaritalStatus) {
		this.fatherMaritalStatus = fatherMaritalStatus;
	}

	public String getFatherMiddleName() {
		return fatherMiddleName;
	}

	public void setFatherMiddleName(String fatherMiddleName) {
		this.fatherMiddleName = fatherMiddleName;
	}

	public Long getFatherMobileNumber() {
		return fatherMobileNumber;
	}

	public void setFatherMobileNumber(Long fatherMobileNumber) {
		this.fatherMobileNumber = fatherMobileNumber;
	}

	public String getFatherOccupation() {
		return fatherOccupation;
	}

	public void setFatherOccupation(String fatherOccupation) {
		this.fatherOccupation = fatherOccupation;
	}

	public Short getFatherParish() {
		return fatherParish;
	}

	public void setFatherParish(Short fatherParish) {
		this.fatherParish = fatherParish;
	}

	public String getFatherQualification() {
		return fatherQualification;
	}

	public void setFatherQualification(String fatherQualification) {
		this.fatherQualification = fatherQualification;
	}

	public String getFatherReligion() {
		return fatherReligion;
	}

	public void setFatherReligion(String fatherReligion) {
		this.fatherReligion = fatherReligion;
	}

	public String getFatherSurname() {
		return fatherSurname;
	}

	public void setFatherSurname(String fatherSurname) {
		this.fatherSurname = fatherSurname;
	}

	public Short getHalf() {
		return half;
	}

	public void setHalf(Short half) {
		this.half = half;
	}

	public Short getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Short instituteId) {
		this.instituteId = instituteId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Short getIsBothParentDeceased() {
		return isBothParentDeceased;
	}

	public void setIsBothParentDeceased(Short isBothParentDeceased) {
		this.isBothParentDeceased = isBothParentDeceased;
	}

	public Short getIsSingleParent() {
		return isSingleParent;
	}

	public void setIsSingleParent(Short isSingleParent) {
		this.isSingleParent = isSingleParent;
	}

	public String getMarginalNote() {
		return marginalNote;
	}

	public void setMarginalNote(String marginalNote) {
		this.marginalNote = marginalNote;
	}

	public String getModeOfSignature() {
		return modeOfSignature;
	}

	public void setModeOfSignature(String modeOfSignature) {
		this.modeOfSignature = modeOfSignature;
	}

	public String getMotherAddress() {
		return motherAddress;
	}

	public void setMotherAddress(String motherAddress) {
		this.motherAddress = motherAddress;
	}

	public Short getMotherAge() {
		return motherAge;
	}

	public void setMotherAge(Short motherAge) {
		this.motherAge = motherAge;
	}

	public Integer getMotherCitizenId() {
		return motherCitizenId;
	}

	public void setMotherCitizenId(Integer motherCitizenId) {
		this.motherCitizenId = motherCitizenId;
	}

	public String getMotherEmailId() {
		return motherEmailId;
	}

	public void setMotherEmailId(String motherEmailId) {
		this.motherEmailId = motherEmailId;
	}

	public String getMotherFirstName() {
		return motherFirstName;
	}

	public void setMotherFirstName(String motherFirstName) {
		this.motherFirstName = motherFirstName;
	}

	public String getMotherMaidenSurname() {
		return motherMaidenSurname;
	}

	public void setMotherMaidenSurname(String motherMaidenSurname) {
		this.motherMaidenSurname = motherMaidenSurname;
	}

	public String getMotherMaritalStatus() {
		return motherMaritalStatus;
	}

	public void setMotherMaritalStatus(String motherMaritalStatus) {
		this.motherMaritalStatus = motherMaritalStatus;
	}

	public String getMotherMiddleName() {
		return motherMiddleName;
	}

	public void setMotherMiddleName(String motherMiddleName) {
		this.motherMiddleName = motherMiddleName;
	}

	public Long getMotherMobileNumber() {
		return motherMobileNumber;
	}

	public void setMotherMobileNumber(Long motherMobileNumber) {
		this.motherMobileNumber = motherMobileNumber;
	}

	public String getMotherOccupation() {
		return motherOccupation;
	}

	public void setMotherOccupation(String motherOccupation) {
		this.motherOccupation = motherOccupation;
	}

	public Short getMotherParish() {
		return motherParish;
	}

	public void setMotherParish(Short motherParish) {
		this.motherParish = motherParish;
	}

	public String getMotherQualification() {
		return motherQualification;
	}

	public void setMotherQualification(String motherQualification) {
		this.motherQualification = motherQualification;
	}

	public String getMotherReligion() {
		return motherReligion;
	}

	public void setMotherReligion(String motherReligion) {
		this.motherReligion = motherReligion;
	}

	public String getMotherSurname() {
		return motherSurname;
	}

	public void setMotherSurname(String motherSurname) {
		this.motherSurname = motherSurname;
	}

	public Integer getNodEntryNo() {
		return nodEntryNo;
	}

	public void setNodEntryNo(Integer nodEntryNo) {
		this.nodEntryNo = nodEntryNo;
	}

	public Short getPage() {
		return page;
	}

	public void setPage(Short page) {
		this.page = page;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	
}