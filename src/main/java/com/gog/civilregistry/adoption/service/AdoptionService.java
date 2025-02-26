package com.gog.civilregistry.adoption.service;

import java.io.ByteArrayInputStream;

import org.springframework.web.multipart.MultipartFile;

import com.gog.civilregistry.adoption.model.ACDownloadRequest;
import com.gog.civilregistry.adoption.model.ApplicationTrackStatus;
import com.gog.civilregistry.adoption.model.ChildInformation;
import com.gog.civilregistry.adoption.model.DocListRequest;
import com.gog.civilregistry.adoption.model.GeneralInformation;
import com.gog.civilregistry.adoption.model.SearchApplicationACRequest;
import com.gog.civilregistry.adoption.model.SearchApplicationARRequest;
import com.gog.civilregistry.adoption.model.TrackAppUserRequest;
import com.gog.civilregistry.adoption.model.VaultRequest;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;

public interface AdoptionService {

	ServiceResponse trackApplicationStatus(ApplicationTrackStatus request);

	ServiceResponse saveARDraft(MultipartFile[] attachments, String request);

	ServiceResponse getAR(GeneralInformation request);

	ServiceResponse submitAdoptionRegistration(MultipartFile[] attachments, String request);

	ServiceResponse getDocList(DocListRequest request);

	ServiceResponse searchApplicationAR(SearchApplicationARRequest request);
	
	ServiceResponse searchApplicationAC(SearchApplicationACRequest request);
	
	ServiceResponse searchACDownload(ACDownloadRequest request);

	ServiceResponse trackAppUser(TrackAppUserRequest request);

	ServiceResponse getVault(VaultRequest request);

	ServiceResponse saveAndSubmitByDepartmentUsers(MultipartFile[] attachments, String request);

	ServiceResponse getChildDetailsForAdoption(ChildInformation request);

}
