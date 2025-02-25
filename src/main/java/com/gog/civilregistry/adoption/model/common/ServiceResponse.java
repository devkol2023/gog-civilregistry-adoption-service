package com.gog.civilregistry.adoption.model.common;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ServiceResponse {

	private String message;
	private String status;
	private Object responseObject;
	public ServiceResponse() {}
	
	public ServiceResponse(String status, String message, Object responseObject) {
        this.status = status;
        this.message = message;
        this.responseObject = responseObject;
    }

}
