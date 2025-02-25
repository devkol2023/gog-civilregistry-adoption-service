package com.gog.civilregistry.adoption.model;

import java.util.List;

import lombok.Data;

@Data
public class SaveARDraftRequest {

	private FatherInformation fatherInformation;
	private MotherInformation motherInformation;
	private ChildInformation childInformation;
	private GeneralInformation generalInformation;
	private Integer loginUserId;
	private List<UploadFileData> uploadFileData;
	private WorkflowInformation workflowInformation;
	private Integer isDraft;
	private List<DeletedFileListModel> deletedFiles;
	private String motherTownName;
	private String fatherTownName;

}
