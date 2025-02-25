package com.gog.civilregistry.adoption.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "t_application_register", schema = "applications")
@NamedQuery(name = "ApplicationRegisterEntity.findAll", query = "SELECT t FROM ApplicationRegisterEntity t")
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationRegisterEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "application_register_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicationRegisterId;

	@Column(name = "application_no")
	private String applicationNo;

	@Column(name = "application_type_id")
	private Integer applicationTypeId;

	@Column(name = "current_status_id")
	private Integer currentStatusId;

}