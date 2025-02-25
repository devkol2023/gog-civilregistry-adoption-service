package com.gog.civilregistry.adoption.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the t_adoption_register database table.
 * 
 */
@Entity
@Table(name="t_adoption_register", schema = "adoption")
@NamedQuery(name="AdoptionRegisterEntity.findAll", query="SELECT t FROM AdoptionRegisterEntity t")
public class AdoptionRegisterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="adoption_registration_id")
	private Long adoptionRegistrationId;

	@Column(name="adoption_certifcate_dms_id")
	private String adoptionCertifcateDmsId;

	@Column(name="adoption_certificate_number")
	private String adoptionCertificateNumber;

	@Column(name="adoption_registration_number")
	private String adoptionRegistrationNumber;

	@Column(name="application_adoption_certificate_id")
	private Long applicationAdoptionCertificateId;

	@Column(name="application_adoption_id")
	private Long applicationAdoptionId;

	@Column(name="citizen_id")
	private Integer citizenId;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_on")
	private Timestamp createdOn;

	@Column(name="entry_no")
	private Integer entryNo;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="marginal_note")
	private String marginalNote;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_on")
	private Timestamp updatedOn;

	public AdoptionRegisterEntity() {
	}

	public Long getAdoptionRegistrationId() {
		return this.adoptionRegistrationId;
	}

	public void setAdoptionRegistrationId(Long adoptionRegistrationId) {
		this.adoptionRegistrationId = adoptionRegistrationId;
	}

	public String getAdoptionCertifcateDmsId() {
		return this.adoptionCertifcateDmsId;
	}

	public void setAdoptionCertifcateDmsId(String adoptionCertifcateDmsId) {
		this.adoptionCertifcateDmsId = adoptionCertifcateDmsId;
	}

	public String getAdoptionCertificateNumber() {
		return this.adoptionCertificateNumber;
	}

	public void setAdoptionCertificateNumber(String adoptionCertificateNumber) {
		this.adoptionCertificateNumber = adoptionCertificateNumber;
	}

	public String getAdoptionRegistrationNumber() {
		return this.adoptionRegistrationNumber;
	}

	public void setAdoptionRegistrationNumber(String adoptionRegistrationNumber) {
		this.adoptionRegistrationNumber = adoptionRegistrationNumber;
	}

	public Long getApplicationAdoptionCertificateId() {
		return this.applicationAdoptionCertificateId;
	}

	public void setApplicationAdoptionCertificateId(Long applicationAdoptionCertificateId) {
		this.applicationAdoptionCertificateId = applicationAdoptionCertificateId;
	}

	public Long getApplicationAdoptionId() {
		return this.applicationAdoptionId;
	}

	public void setApplicationAdoptionId(Long applicationAdoptionId) {
		this.applicationAdoptionId = applicationAdoptionId;
	}

	public Integer getCitizenId() {
		return this.citizenId;
	}

	public void setCitizenId(Integer citizenId) {
		this.citizenId = citizenId;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getEntryNo() {
		return this.entryNo;
	}

	public void setEntryNo(Integer entryNo) {
		this.entryNo = entryNo;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getMarginalNote() {
		return this.marginalNote;
	}

	public void setMarginalNote(String marginalNote) {
		this.marginalNote = marginalNote;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

}