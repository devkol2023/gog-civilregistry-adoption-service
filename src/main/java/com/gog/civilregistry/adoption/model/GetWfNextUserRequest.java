package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class GetWfNextUserRequest {
	
	private Long applicationRegisterId;
	private Integer currentInstituteId;
	private Integer currentRoleId;
	private Integer currentUserId;
	private Integer currentParishId;
	private Integer nextRoleId;
	private Integer nextWorkflowId;
	private Short isUserVisible;
	private Integer nextInstituteTypeId;

}
