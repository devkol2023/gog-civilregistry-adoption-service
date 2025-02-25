package com.gog.civilregistry.adoption.model.common;

import lombok.Data;

@Data
public class ClaimApplicationWfRequest {
	
	private Long applicationRegisterId;
	private Integer currentRoleId;
	private String currentRoleCode;
	private Integer currentUserId;
	private Integer currentInstituteId;
	private Integer currentStageId;
	private Integer nextStageId;
	private Short actionId;
	private Integer workflowId;

}
