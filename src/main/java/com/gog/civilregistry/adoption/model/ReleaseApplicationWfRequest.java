package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class ReleaseApplicationWfRequest {
	
	private Integer applicationRegisterId;
	private Integer currentRoleId;
	private Integer currentUserId;
	private Integer currentInstituteId;
	private Integer currentStageId;

}
