package com.gog.civilregistry.adoption.service;

import java.io.ByteArrayInputStream;

import org.springframework.web.multipart.MultipartFile;

import com.gog.civilregistry.adoption.model.ApplicationTrackStatus;
import com.gog.civilregistry.adoption.model.DocListRequest;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;

public interface AdoptionService {

	ServiceResponse saveARDraft(MultipartFile[] attachments, String request);

}
