package com.gog.civilregistry.adoption.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the t_adoption_application_documents database table.
 * 
 */
@Entity
@Table(name="t_adoption_application_documents", schema = "adoption")
@NamedQuery(name="AdoptionApplicationDocumentEntity.findAll", query="SELECT t FROM AdoptionApplicationDocumentEntity t")
public class AdoptionApplicationDocumentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="adoption_application_doc_id")
	private Long adoptionApplicationDocId;

	@Column(name="application_doc_dms_id")
	private String applicationDocDmsId;

	@Column(name="application_doc_name")
	private String applicationDocName;

	@Column(name="application_doc_subject")
	private String applicationDocSubject;

	@Column(name="application_register_id")
	private Long applicationRegisterId;

	@Column(name="citizen_id")
	private Integer citizenId;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_on")
	private Timestamp createdOn;

	@Column(name="document_type_id")
	private Integer documentTypeId;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_on")
	private Timestamp updatedOn;

	public AdoptionApplicationDocumentEntity() {
	}

	public Long getAdoptionApplicationDocId() {
		return this.adoptionApplicationDocId;
	}

	public void setAdoptionApplicationDocId(Long adoptionApplicationDocId) {
		this.adoptionApplicationDocId = adoptionApplicationDocId;
	}

	public String getApplicationDocDmsId() {
		return this.applicationDocDmsId;
	}

	public void setApplicationDocDmsId(String applicationDocDmsId) {
		this.applicationDocDmsId = applicationDocDmsId;
	}

	public String getApplicationDocName() {
		return this.applicationDocName;
	}

	public void setApplicationDocName(String applicationDocName) {
		this.applicationDocName = applicationDocName;
	}

	public String getApplicationDocSubject() {
		return this.applicationDocSubject;
	}

	public void setApplicationDocSubject(String applicationDocSubject) {
		this.applicationDocSubject = applicationDocSubject;
	}

	public Long getApplicationRegisterId() {
		return this.applicationRegisterId;
	}

	public void setApplicationRegisterId(Long applicationRegisterId) {
		this.applicationRegisterId = applicationRegisterId;
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

	public Integer getDocumentTypeId() {
		return this.documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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