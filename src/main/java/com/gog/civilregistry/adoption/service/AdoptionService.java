package com.gog.civilregistry.adoption.service;

import java.io.ByteArrayInputStream;

import org.springframework.web.multipart.MultipartFile;

import com.gog.civilregistry.adoption.model.ApplicationTrackStatus;
import com.gog.civilregistry.adoption.model.DocListRequest;
import com.gog.civilregistry.adoption.model.GeneralInformation;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;

public interface AdoptionService {

	ServiceResponse trackApplicationStatus(ApplicationTrackStatus request);

	ServiceResponse saveARDraft(MultipartFile[] attachments, String request);

	ServiceResponse getAR(GeneralInformation request);

	ServiceResponse submitAdoptionRegistration(MultipartFile[] attachments, String request);

}
