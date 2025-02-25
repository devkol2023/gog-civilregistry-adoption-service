package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class ApplicationTrackStatus {
	
	private String submittedBy;
	private String submittedTo;
	private String dateOfSubmission;
	private String claimedBy;
	private String claimedDate;
	private String releasedDate;
	private String actionStage;
	private Long applicationId;
	private Long workflowId;


}
