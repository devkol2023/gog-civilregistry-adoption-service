package com.gog.civilregistry.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gog.civilregistry.adoption.model.ACDownloadRequest;
import com.gog.civilregistry.adoption.model.ApplicationTrackStatus;
import com.gog.civilregistry.adoption.model.ChildInformation;
import com.gog.civilregistry.adoption.model.ClaimApplicationWfRequest;
import com.gog.civilregistry.adoption.model.DocListRequest;
import com.gog.civilregistry.adoption.model.GeneralInformation;
import com.gog.civilregistry.adoption.model.GetWfNextStageRequest;
import com.gog.civilregistry.adoption.model.GetWfNextUserRequest;
import com.gog.civilregistry.adoption.model.ReleaseApplicationWfRequest;
import com.gog.civilregistry.adoption.model.SearchApplicationACRequest;
import com.gog.civilregistry.adoption.model.SearchApplicationARRequest;
import com.gog.civilregistry.adoption.model.TrackAppUserRequest;
import com.gog.civilregistry.adoption.model.VaultRequest;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;
import com.gog.civilregistry.adoption.service.AdoptionService;
import com.gog.civilregistry.adoption.service.WorkflowService;
import com.gog.civilregistry.adoption.util.CommonConstants;

@RestController
@RequestMapping("/api")
public class AdoptionController {

	@Autowired
	private WorkflowService workflowService;

	private final AdoptionService adoptionService;

	public AdoptionController(AdoptionService adoptionService) {
		this.adoptionService = adoptionService;
	}

	@PostMapping("/claimApplicationWorkflow")
	public ServiceResponse claimApplicationWorkflow(@RequestBody ClaimApplicationWfRequest request) {

		ServiceResponse response = new ServiceResponse();
		try {
			response = workflowService.claimApplicationWorkflow(request);
		} catch (Exception e) {
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR_MSG);
		}
		return response;

	}

	@PostMapping("/releaseApplicationWorkflow")
	public ServiceResponse releaseApplicationWorkflow(@RequestBody ReleaseApplicationWfRequest request) {

		ServiceResponse response = new ServiceResponse();
		try {
			response = workflowService.releaseApplicationWorkflow(request);
		} catch (Exception e) {
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR_MSG);
		}
		return response;

	}

	@PostMapping("/getWfNextStage")
	public ServiceResponse getWfNextStage(@RequestBody GetWfNextStageRequest request) {
		return workflowService.getWfNextStage(request);
	}

	@PostMapping("/getWfNextUser")
	public ServiceResponse getWfNextUser(@RequestBody GetWfNextUserRequest request) {
		return workflowService.getWfNextUser(request);
	}

	@PostMapping("/trackApplicationStatus")
	public ServiceResponse trackApplicationStatus(@RequestBody ApplicationTrackStatus request) {
		return adoptionService.trackApplicationStatus(request);
	}

	@PostMapping("/getAdoptionRegistrationDetails")
	public ServiceResponse getAR(@RequestBody GeneralInformation request) {
		return adoptionService.getAR(request);
	}

	@PostMapping("/getDocList")
	public ServiceResponse getDocList(@RequestBody DocListRequest request) {
		return adoptionService.getDocList(request);
	}

	@PostMapping("/saveAdoptionRegistrationDraft")
	public ServiceResponse saveARDraft(
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments, String request) {
		// SaveNODDraftRequest
		ServiceResponse response = new ServiceResponse();
		try {
			response = adoptionService.saveARDraft(attachments, request);
		} catch (Exception e) {
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR_MSG);
		}
		return response;
	}

	@PostMapping("/submitAdoptionRegistration")
	public ServiceResponse submitAdoptionRegistration(
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments, String request) {
		// SaveNODDraftRequest
		ServiceResponse response = new ServiceResponse();
		try {
			response = adoptionService.submitAdoptionRegistration(attachments, request);
		} catch (Exception e) {
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR_MSG);
		}
		return response;
	}

	@PostMapping("/searchApplicationAR")
	public ServiceResponse searchApplicationAR(@RequestBody SearchApplicationARRequest request) {
		return adoptionService.searchApplicationAR(request);
	}

	@PostMapping("/trackAppUser")
	public ServiceResponse trackAppUser(@RequestBody TrackAppUserRequest request) {
		return adoptionService.trackAppUser(request);
	}

	@PostMapping("/getVault")
	public ServiceResponse getVault(@RequestBody VaultRequest request) {
		return adoptionService.getVault(request);
	}

	@PostMapping("/getChildDetailsForAdoption")
	public ServiceResponse getChildDetailsForAdoption(@RequestBody ChildInformation request) {
		return adoptionService.getChildDetailsForAdoption(request);
	}

	@PostMapping("/saveAndSubmitByDepartmentUsers")
	public ServiceResponse saveAndSubmitByDepartmentUsers(
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments, String request) {
		ServiceResponse response = new ServiceResponse();
		try {
			response = adoptionService.saveAndSubmitByDepartmentUsers(attachments, request);
		} catch (Exception e) {
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR_MSG);
		}
		return response;
	}

	
	@PostMapping("/searchApplicationAC")
	public ServiceResponse searchApplicationAC(@RequestBody SearchApplicationACRequest request) {
		return adoptionService.searchApplicationAC(request);
	}
	
	@PostMapping("/searchACDownload")
	public ServiceResponse searchACDownload(@RequestBody ACDownloadRequest request) {
		return adoptionService.searchACDownload(request);
	}
	
	@PostMapping("/submitAdoptionCertificate")
	public ServiceResponse submitAdoptionCertificate(
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments, String request) {
		// SaveNODDraftRequest
		ServiceResponse response = new ServiceResponse();
		try {
			response = adoptionService.submitAdoptionCertificate(attachments, request);
		} catch (Exception e) {
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR_MSG);
		}
		return response;
	}

}
