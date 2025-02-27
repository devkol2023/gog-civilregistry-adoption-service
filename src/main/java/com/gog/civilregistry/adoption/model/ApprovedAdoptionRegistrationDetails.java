package com.gog.civilregistry.adoption.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApprovedAdoptionRegistrationDetails {
	
	@JsonProperty("registration_id")
	private Long applicationRegistrationId;
	
	@JsonProperty("registration_no")
	private String marriageRegistrationNumber;

}
