package com.gog.civilregistry.adoption.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gog.civilregistry.adoption.model.common.EmailRequest;
import com.gog.civilregistry.adoption.service.IntegrationApiService;
import com.google.gson.Gson;

@Service
public class IntegrationApiServiceImpl implements IntegrationApiService {
	private static final Logger logger = LoggerFactory.getLogger(IntegrationApiServiceImpl.class);

	private final RestTemplate restTemplate;

	public IntegrationApiServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Value("${integration.sendEmail}")
	String sendEmail;

	@Override
	public void sendEmail(EmailRequest emailRequest) {
		logger.info("Start Method " + "sendEmail");
		try {
			Gson gson = new Gson();
			String gsonObject = gson.toJson(emailRequest);
//			LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//			map.add("request", gsonObject);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> requestEntity = new HttpEntity<>(gsonObject, headers);
			restTemplate.postForObject(sendEmail, requestEntity, String.class);
			logger.info("Email sent");
		} catch (Exception e) {
			logger.error("Exception - {}", e.getMessage(), e);
		}
		logger.info("Exit Method " + "sendEmail");

	}

}
