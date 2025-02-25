package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class GetWfNextStageRequest {
	
	private Long applicationRegisterId;
	private Integer applicationTypeId;
	private Integer currentStageId;
	private String currentRoleCode;
	private Integer nextInstituteTypeId;
	private String nextRoleCode;

}
