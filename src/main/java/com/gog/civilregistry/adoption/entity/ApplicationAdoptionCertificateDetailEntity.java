package com.gog.civilregistry.adoption.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the t_application_adoption_certificate_details database table.
 * 
 */
@Entity
@Table(name="t_application_adoption_certificate_details", schema = "adoption")
@NamedQuery(name="ApplicationAdoptionCertificateDetailEntity.findAll", query="SELECT t FROM ApplicationAdoptionCertificateDetailEntity t")
public class ApplicationAdoptionCertificateDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="application_adoption_certificate_id")
	private Long applicationAdoptionCertificateId;

	@Column(name="adoption_certifcate_dms_id")
	private String adoptionCertifcateDmsId;

	@Column(name="adoption_certificate_number")
	private String adoptionCertificateNumber;

	@Column(name="adoption_registration_number")
	private String adoptionRegistrationNumber;

	@Column(name="application_adoption_id")
	private Long applicationAdoptionId;

	@Column(name="application_register_id")
	private Long applicationRegisterId;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_on")
	private Timestamp createdOn;

	@Column(name="fees_per_copy")
	private float feesPerCopy;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="no_of_copies")
	private Short noOfCopies;

	@Column(name="payment_mode")
	private String paymentMode;

	@Column(name="payment_status")
	private Short paymentStatus;

	private String remarks;

	@Column(name="total_amount")
	private float totalAmount;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_on")
	private Timestamp updatedOn;

	public ApplicationAdoptionCertificateDetailEntity() {
	}

	
	
	public Long getApplicationAdoptionCertificateId() {
		return this.applicationAdoptionCertificateId;
	}

	public void setApplicationAdoptionCertificateId(Long applicationAdoptionCertificateId) {
		this.applicationAdoptionCertificateId = applicationAdoptionCertificateId;
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

	public Long getApplicationAdoptionId() {
		return this.applicationAdoptionId;
	}

	public void setApplicationAdoptionId(Long applicationAdoptionId) {
		this.applicationAdoptionId = applicationAdoptionId;
	}

	public Long getApplicationRegisterId() {
		return this.applicationRegisterId;
	}

	public void setApplicationRegisterId(Long applicationRegisterId) {
		this.applicationRegisterId = applicationRegisterId;
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

	public float getFeesPerCopy() {
		return this.feesPerCopy;
	}

	public void setFeesPerCopy(float feesPerCopy) {
		this.feesPerCopy = feesPerCopy;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Short getNoOfCopies() {
		return this.noOfCopies;
	}

	public void setNoOfCopies(Short noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	public String getPaymentMode() {
		return this.paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Short getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(Short paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public float getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
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