package com.gog.civilregistry.adoption.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.gog.civilregistry.adoption.model.NewFileUploadResponse;
import com.gog.civilregistry.adoption.service.DMSService;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class DMSServiceImpl implements DMSService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(DMSServiceImpl.class);

	private final RestTemplate restTemplate;
	private final HttpServletRequest httpServletRequest;

	public DMSServiceImpl(RestTemplate restTemplate, HttpServletRequest httpServletRequest) {
		this.restTemplate = restTemplate;
		this.httpServletRequest = httpServletRequest;
	}

	@Value("${dms.uploadFilesToAlfresco}")
	String uploadFilesToAlfresco;

	@Override
	public NewFileUploadResponse uploadFileToAlfresco(MultipartFile[] files) {
		logger.info("Start Method " + "uploadFilesToAlfresco");
		NewFileUploadResponse response = new NewFileUploadResponse();
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		try {
			for (MultipartFile file : files) {
				map.add("file", file.getResource());
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
			response = restTemplate.postForObject(uploadFilesToAlfresco, requestEntity, NewFileUploadResponse.class);

		} catch (Exception e) {
			throw new RuntimeException("uploadFileToAlfresco", e);
		}
		logger.info("Exit Method " + "uploadFilesToAlfresco");
		return response;
	}
}
