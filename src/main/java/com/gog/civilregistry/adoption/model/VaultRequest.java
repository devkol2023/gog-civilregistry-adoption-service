package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class VaultRequest {

	private String applicationTypeCode;
	private String state;
	private Integer userId;
	private Integer roleId;
	private Integer instituteId;
	private Integer pageNumber;
	private Integer perPageCount;
	private String searchText;
}
