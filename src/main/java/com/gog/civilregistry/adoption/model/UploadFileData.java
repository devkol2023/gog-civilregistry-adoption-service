package com.gog.civilregistry.adoption.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UploadFileData {

	private String fileName;
	private Integer docTypeId;
	private String fileSubject;
	private String referenceId;
	private String applicationDocDmsId;
	private Long applicationDocId;
	private Long applicationRegisterId;
	private String docTypeCode;
	private Short isGeneratedBySystem;
	private Integer citizenId;
	private Boolean isActive;
	
}
