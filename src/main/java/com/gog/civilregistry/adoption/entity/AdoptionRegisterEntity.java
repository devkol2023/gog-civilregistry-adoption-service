package com.gog.civilregistry.adoption.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * The persistent class for the t_adoption_register database table.
 * 
 */
@Entity
@Data
@Table(name = "t_adoption_register", schema = "adoption")
@NamedQuery(name = "AdoptionRegisterEntity.findAll", query = "SELECT t FROM AdoptionRegisterEntity t")
public class AdoptionRegisterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adoption_registration_id")
	private Long adoptionRegistrationId;

	@Column(name = "adoption_certifcate_dms_id")
	private String adoptionCertifcateDmsId;

	@Column(name = "adoption_certificate_number")
	private String adoptionCertificateNumber;

	@Column(name = "adoption_registration_number")
	private String adoptionRegistrationNumber;

	@Column(name = "application_adoption_certificate_id")
	private Long applicationAdoptionCertificateId;

	@Column(name = "application_adoption_id")
	private Long applicationAdoptionId;

	@Column(name = "citizen_id")
	private Integer citizenId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_on")
	private Timestamp createdOn;

	@Column(name = "entry_no")
	private String entryNo;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "marginal_note")
	private String marginalNote;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_on")
	private Timestamp updatedOn;

	public AdoptionRegisterEntity() {
	}

}