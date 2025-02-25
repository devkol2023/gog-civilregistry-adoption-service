package com.gog.civilregistry.adoption.service;

import org.springframework.web.multipart.MultipartFile;

import com.gog.civilregistry.adoption.model.NewFileUploadResponse;


public interface DMSService {

	NewFileUploadResponse uploadFileToAlfresco(MultipartFile[] files);
}
