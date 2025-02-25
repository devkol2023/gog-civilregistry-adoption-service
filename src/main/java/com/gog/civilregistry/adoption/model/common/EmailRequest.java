package com.gog.civilregistry.adoption.model.common;

import java.util.List;

import lombok.Data;
@Data
public class EmailRequest {

	private String[] to;
	private String subject;
	private String content;
	private String name;
	List<String> params;

}
