package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class ProcessApplicationModel {

	private String flag;
	private Long applicationRegisterId;
	private Integer instituteId;
	private Integer roleId;
	private Integer userId;
	private Integer applicationTypeId;
	private Integer parishId;
	private Integer statusId;
	private Integer citizenId;

}
