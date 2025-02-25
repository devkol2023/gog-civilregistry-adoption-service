package com.gog.civilregistry.adoption.dto;

import lombok.Data;

@Data
public class GetWfNextStageDTO {
	
	private Short stageId;
	private String internalStageName;
	private Short actionId;
	private String actionName;
	private String nextRoleCode;
	private Integer nextRoleId;
	private String nextRoleName;
	private String nextInstituteTypeCode;
	private Integer nextInstituteTypeId;
	private Integer workflowId;
	private String flowType;
	private Short isUserVisible;
	
	

}
