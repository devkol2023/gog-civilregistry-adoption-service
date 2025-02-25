package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class DocListRequest {

	private Long applicationRegisterId;
	private Integer applicationTypeId;
	private String loggedInRoleCode;
	private Integer loggedInRoleId;
	private Integer citizenId;
}
