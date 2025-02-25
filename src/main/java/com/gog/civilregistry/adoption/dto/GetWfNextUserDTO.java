package com.gog.civilregistry.adoption.dto;

import lombok.Data;

@Data
public class GetWfNextUserDTO {
	
	private Long nextUserId;
	private Integer nextInstituteId;
	private String nextUserName;

}
