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
 * The persistent class for the t_adoption_application_documents database table.
 * 
 */
@Entity
@Data
@Table(name = "t_adoption_application_documents", schema = "adoption")
@NamedQuery(name = "AdoptionApplicationDocumentEntity.findAll", query = "SELECT t FROM AdoptionApplicationDocumentEntity t")
public class AdoptionApplicationDocumentEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adoption_application_doc_id")
	private Long applicationDocId;

	@Column(name = "application_doc_dms_id")
	private String applicationDocDmsId;

	@Column(name = "application_doc_name")
	private String applicationDocName;

	@Column(name = "application_doc_subject")
	private String applicationDocSubject;

	@Column(name = "application_register_id")
	private Long applicationRegisterId;

	@Column(name = "citizen_id")
	private Integer citizenId;

	@Column(name = "document_type_id")
	private Integer documentTypeId;

	@Column(name = "document_type_code")
	private String documentTypeCode;

	public AdoptionApplicationDocumentEntity() {
	}

}