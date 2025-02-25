package com.gog.civilregistry.adoption.model;

import java.util.List;

import lombok.Data;

@Data
public class SaveARDraftResponse {

	private FatherInformation fatherInformation;
	private MotherInformation motherInformation;
	private ChildInformation childInformation;
	private GeneralInformation generalInformation;
	private Integer loginUserId;
	private List<UploadFileData> uploadFileData;
}
