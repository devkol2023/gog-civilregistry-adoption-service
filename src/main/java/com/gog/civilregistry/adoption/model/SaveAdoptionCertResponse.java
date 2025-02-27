package com.gog.civilregistry.adoption.model;

import java.util.List;

import lombok.Data;

@Data
public class SaveAdoptionCertResponse {
	
	private AdoptionCertInformation adoptionCertInformation;
	private List<UploadFileData> uploadFileData;
	private WorkflowInformation workflowInformation;
	private Integer isDraft;
	private Integer loginUserId;
	private List<DeletedFileListModel> deletedFiles;

}
