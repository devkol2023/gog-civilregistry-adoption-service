package com.gog.civilregistry.adoption.service;

import org.springframework.stereotype.Component;

import com.gog.civilregistry.adoption.model.common.EmailRequest;

@Component
public interface IntegrationApiService {
	
	public void sendEmail(EmailRequest emailRequest);
	
}