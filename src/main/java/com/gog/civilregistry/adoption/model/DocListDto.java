package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class DocListDto {

	private Integer docTypeId;
	private String dataValue;
	private String dataDescription;
	private Integer masterDataTypeId;
	private Integer citizenId;
	private String docName;
	private String docSubject;
	private String applicationDocDmsId;
	private Short isGeneratedBySystem;
}
