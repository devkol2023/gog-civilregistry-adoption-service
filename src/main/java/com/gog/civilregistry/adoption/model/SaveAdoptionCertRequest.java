package com.gog.civilregistry.adoption.model;

import java.util.List;

import com.gog.civilregistry.adoption.model.DeletedFileListModel;
import com.gog.civilregistry.adoption.model.UploadFileData;
import com.gog.civilregistry.adoption.model.WorkflowInformation;

import lombok.Data;

@Data
public class SaveAdoptionCertRequest {
	
	private AdoptionCertInformation adoptionCertInformation;
	private List<UploadFileData> uploadFileData;
	private WorkflowInformation workflowInformation;
	private Integer isDraft;
	private Integer loginUserId;
	private List<DeletedFileListModel> deletedFiles;

}
