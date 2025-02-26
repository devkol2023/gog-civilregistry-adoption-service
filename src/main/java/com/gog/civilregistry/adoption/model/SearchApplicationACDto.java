package com.gog.civilregistry.adoption.model;

import lombok.Data;

@Data
public class SearchApplicationACDto {
	
	private Long applicationRegisterId;
    private String applicationNumber;
    private String childName;
    private String motherName;
	private String fatherName;
	private String parishOfBirth;
	private String dateOfBirth;
	private String gender;

}
