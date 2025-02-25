package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class WorkflowUpdateModel {
	private Long applicationRegisterId;
	private Integer applicationTypeId;
	private Integer isDraft;
	private String assignedByRoleCode;
	private Integer assignedByRoleId;
	private Integer assignedByUserId;
	private Integer assignedByInstituteId;
	private String assignedToRoleCode;
	private Integer assignedToRoleId;
	private Integer assignedToUserId;
	private Integer assignedToInstituteId;
	private Integer workflowId;
	private Integer currentStatusId;
	private Integer nextStatusId;
	private Integer actionId;
	private String comments;

}
