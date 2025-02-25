package com.gog.civilregistry.adoption.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gog.civilregistry.adoption.entity.AdoptionApplicationDocumentEntity;
import com.gog.civilregistry.adoption.entity.ApplicationAdoptionDetailEntity;
import com.gog.civilregistry.adoption.entity.ApplicationRegisterEntity;
import com.gog.civilregistry.adoption.model.ChildInformation;
import com.gog.civilregistry.adoption.model.DeletedFileListModel;
import com.gog.civilregistry.adoption.model.NewFileUploadResponse;
import com.gog.civilregistry.adoption.model.ProcessApplicationModel;
import com.gog.civilregistry.adoption.model.SaveARDraftRequest;
import com.gog.civilregistry.adoption.model.SaveARDraftResponse;
import com.gog.civilregistry.adoption.model.TownCodeProjection;
import com.gog.civilregistry.adoption.model.TownProjection;
import com.gog.civilregistry.adoption.model.UploadFileData;
import com.gog.civilregistry.adoption.model.WorkflowInformation;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;
import com.gog.civilregistry.adoption.repository.AdoptionApplicationDocumentRepository;
import com.gog.civilregistry.adoption.repository.ApplicationAdoptionDetailRepository;
import com.gog.civilregistry.adoption.repository.ApplicationRegisterRepository;
import com.gog.civilregistry.adoption.repository.custom.AdoptionRepositoryCustom;
import com.gog.civilregistry.adoption.service.AdoptionService;
import com.gog.civilregistry.adoption.service.DMSService;
import com.gog.civilregistry.adoption.service.IntegrationApiService;
import com.gog.civilregistry.adoption.util.CommonConstants;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdoptionServiceImpl implements AdoptionService {

	private static final Logger logger = LoggerFactory.getLogger(AdoptionServiceImpl.class);

	@Value("${spring.servlet.multipart.max-file-size}")
	String multipartMaxFileSize;
	@Value("${spring.servlet.multipart.max-request-size}")
	String multipartMaxRequestSize;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DMSService dmsService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	IntegrationApiService integrationApiService;

	@Autowired
	AdoptionRepositoryCustom adoptionRepositoryCustom;

	@Autowired
	AdoptionApplicationDocumentRepository documentRepository;

	@Autowired
	ApplicationRegisterRepository applicationRegisterRepository;

	@Autowired
	ApplicationAdoptionDetailRepository adoptionDetailRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse saveARDraft(MultipartFile[] files, String requeststr) {
		logger.info("Entry Method " + "saveARDraft");
		ServiceResponse response = new ServiceResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		ApplicationRegisterEntity applicationRegisterEntity = new ApplicationRegisterEntity();
		List<ChildInformation> childEntityResponseList = new ArrayList<ChildInformation>();
		ApplicationAdoptionDetailEntity applicationEntity = new ApplicationAdoptionDetailEntity();

		SaveARDraftResponse responseObj = new SaveARDraftResponse();
		List<UploadFileData> docEntityResponseList = new ArrayList<UploadFileData>();
		Long applicationRegisterId = null;
		String applicationNumber = null;
		TownProjection fatherTown = null;
		TownProjection motherTown = null;

		try {
			// generating model request from string request

			ObjectMapper mapper = new ObjectMapper();
			SaveARDraftRequest request = null;
			requeststr = requeststr.replaceAll("\\n", "").replaceAll("\\t", "");
			request = mapper.readValue(requeststr, SaveARDraftRequest.class);
			Integer currentStatusId = Integer.valueOf(CommonConstants.APP_DRAFT_STATUS);

			// first save application in register entity
			// check if application_no of generalInformation is not 0 or null - then only
			// insert

			applicationNumber = request.getGeneralInformation().getApplicationNo();
			applicationRegisterId = request.getGeneralInformation().getApplicationRegisterId();

			if (request.getGeneralInformation().getApplicationNo() == null) {

				ProcessApplicationModel processAr = new ProcessApplicationModel();
				processAr.setFlag("D");
				WorkflowInformation workflowInfoRequest = request.getWorkflowInformation();
				processAr.setInstituteId(request.getGeneralInformation().getInstituteId());
				processAr.setRoleId(workflowInfoRequest.getAssignedByRoleId());
//				processNod.setUserId(3);
				processAr.setApplicationTypeId(request.getGeneralInformation().getApplicationTypeId());
				// processNod.setApplicationTypeId(Integer.valueOf(CommonConstants.APP_TYPE_NOD));
//				processNod.setParishId(3);
				processAr.setStatusId(currentStatusId);
				processAr.setUserId(workflowInfoRequest.getAssignedByUserId());
				processAr.setParishId(request.getGeneralInformation().getInstituteParish());
				processAr.setCitizenId(null);
				// processNod.setCivilRegistryNumber(null);
				// processNod.setApplicationRegisterId(applicationRegisterEntity.getApplicationRegisterId());

				// processNod.setFlag("D");
				// processNod.setInstituteId(request.getGeneralInformation().getInstituteId());
				// processNod.setRoleId(2);

//				processNod.setApplicationTypeId(1);

				Map<String, Object> resultMap = adoptionRepositoryCustom.updateApplicationProcess(processAr);

				String retcode = (String) resultMap.get("re_code");
				String retmsg = (String) resultMap.get("re_msg");
				applicationNumber = (String) resultMap.get("applicationNumber");
				applicationRegisterId = (Long) resultMap.get("applicationRegisterId");

				if (retcode != null && !retcode.equals("")) {
					if (retcode.equals("0")) {
						throw new RuntimeException("Unable to update Adoption Registration");
					}
				}

			} else {
				ProcessApplicationModel processAr = new ProcessApplicationModel();
				processAr.setFlag("D");
				processAr.setApplicationTypeId(request.getGeneralInformation().getApplicationTypeId());
				processAr.setStatusId(request.getGeneralInformation().getStatus());
				processAr.setApplicationRegisterId(applicationRegisterId);
				processAr.setUserId(request.getLoginUserId());

				Map<String, Object> resultMap = adoptionRepositoryCustom.updateApplicationProcess(processAr);

				String retcode = (String) resultMap.get("re_code");
				String retmsg = (String) resultMap.get("re_msg");

				if (retcode != null && !retcode.equals("")) {
					if (retcode.equals("0")) {
						throw new RuntimeException("Unable to update NOD");
					}
				}

			}

			// uploading uploaded documents to alfresco

			// Start: Commented by Sayan

			if (files != null) {
				if (files.length > 0) {
					// only upload those files whole id = 0 or null
					int length = files.length;
					for (int j = 0; j < length; j++) {
						if (request.getUploadFileData().get(j).getApplicationDocId() == 0L
								|| request.getUploadFileData().get(j).getApplicationDocId() == null) {
							MultipartFile[] fileToUpload = new MultipartFile[1];
							fileToUpload[0] = files[j];
							NewFileUploadResponse fileUploadResponse = dmsService.uploadFileToAlfresco(fileToUpload);
							if (request.getUploadFileData() != null && fileUploadResponse != null
									&& !fileUploadResponse.getFileUploadResponse().isEmpty()) {
								int i = 0;
//							for (UploadFileData citizenDocumentAttachment : request.getUploadFileData()) {
//								citizenDocumentAttachment
//										.setReferenceId(fileUploadResponse.getFileUploadResponse().get(i));
//								citizenDocumentAttachment.setFileName(files[i].getOriginalFilename());
//								i++;
//							}
								// set attributes of uploaded file in alfresco in request

								request.getUploadFileData().get(j)
										.setReferenceId(fileUploadResponse.getFileUploadResponse().get(0));
								request.getUploadFileData().get(j)
										.setApplicationDocDmsId(fileUploadResponse.getFileUploadResponse().get(0));
								request.getUploadFileData().get(j).setFileName(files[j].getOriginalFilename());
							}
						}
					}
				}
			}

			// save application documents in t_application_documents table

			for (UploadFileData citizenDocumentAttachment : request.getUploadFileData()) {
				// save only if file id is null or 0
				AdoptionApplicationDocumentEntity docEntity = new AdoptionApplicationDocumentEntity();
				docEntity.setApplicationDocDmsId(citizenDocumentAttachment.getReferenceId());
				docEntity.setApplicationDocName(citizenDocumentAttachment.getFileName());
				docEntity.setApplicationRegisterId(applicationRegisterId);
				docEntity.setDocumentTypeId(citizenDocumentAttachment.getDocTypeId());
				docEntity.setApplicationDocSubject(citizenDocumentAttachment.getFileSubject());
				docEntity.setCreatedBy(request.getLoginUserId());
				docEntity.setUpdatedBy(request.getLoginUserId());
				docEntity.setUpdatedOn(LocalDateTime.now());
				docEntity.setCreatedOn(LocalDateTime.now());
				docEntity.setIsActive(true);
				docEntity.setDocumentTypeCode(citizenDocumentAttachment.getDocTypeCode());
				// save or update based on applicationDocId
				if (citizenDocumentAttachment.getApplicationDocId() != 0L
						|| citizenDocumentAttachment.getApplicationDocId() != null) {
					docEntity.setApplicationDocId(citizenDocumentAttachment.getApplicationDocId());
				}
				docEntity = documentRepository.save(docEntity);
				// setting details in uploadFileData
				citizenDocumentAttachment.setApplicationRegisterId(applicationRegisterId);
				citizenDocumentAttachment.setApplicationDocDmsId(citizenDocumentAttachment.getReferenceId());
				citizenDocumentAttachment.setApplicationDocId(docEntity.getApplicationDocId());
				docEntityResponseList.add(citizenDocumentAttachment);
			}

			// for father town
			if (request.getFatherTownName() != null) {

				fatherTown = adoptionDetailRepository.getTown(request.getFatherTownName());
				if (fatherTown == null) {

					TownCodeProjection townCodeProjection = adoptionDetailRepository
							.getCountForTownCode(request.getFatherInformation().getFatherParish());
					int count = townCodeProjection.getCount();
					count += 1;

					String townCode = request.getFatherTownName().toUpperCase() + String.valueOf(count);

					adoptionDetailRepository.saveTown(request.getFatherTownName(), townCode,
							request.getFatherInformation().getFatherParish());

					fatherTown = adoptionDetailRepository.getTown(request.getFatherTownName());
				}

			}

			if (request.getMotherTownName() != null) {

				motherTown = adoptionDetailRepository.getTown(request.getMotherTownName());
				if (motherTown == null) {

					TownCodeProjection townCodeProjection = adoptionDetailRepository
							.getCountForTownCode(request.getMotherInformation().getMotherParish());
					int count = townCodeProjection.getCount();
					count += 1;

					String townCode = request.getMotherTownName().toUpperCase() + String.valueOf(count);

					adoptionDetailRepository.saveTown(request.getMotherTownName(), townCode,
							request.getMotherInformation().getMotherParish());

					motherTown = adoptionDetailRepository.getTown(request.getMotherTownName());
				}

			}

			// setting town id in father, mother and kin info
			if (fatherTown != null)
				request.getFatherInformation().setFatherVillageTown(fatherTown.getId());

			if (motherTown != null)
				request.getMotherInformation().setMotherVillageTown(motherTown.getId());

			// End: Commented by Sayan

			// normal application save process for parent and child

			// Start: Added by Sayan

			if (request.getGeneralInformation().getApplicationAdoptionId() == null
					|| request.getGeneralInformation().getApplicationAdoptionId() == 0L) {

				Date deliveryDate = null;

				applicationEntity = modelMapper.map(request.getFatherInformation(),
						ApplicationAdoptionDetailEntity.class);
				modelMapper.map(request.getMotherInformation(), applicationEntity);
				modelMapper.map(request.getGeneralInformation(), applicationEntity);
				modelMapper.map(request.getChildInformation(), applicationEntity);

				applicationEntity.setCreatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedOn(LocalDateTime.now());
				applicationEntity.setCreatedOn(LocalDateTime.now());
				applicationEntity.setIsActive(true);
				// set default status

				applicationEntity.setApplicationRegisterId(applicationRegisterId);

				ApplicationAdoptionDetailEntity savedApplicationEntity = adoptionDetailRepository
						.save(applicationEntity);

				request.getGeneralInformation()
						.setApplicationAdoptionId(savedApplicationEntity.getApplicationAdoptionId());
				request.getGeneralInformation().setApplicationNo(applicationNumber);

			} else {

				ApplicationAdoptionDetailEntity arDetailsEntity = adoptionDetailRepository
						.findById(request.getGeneralInformation().getApplicationAdoptionId())
						.orElseThrow(() -> new IllegalArgumentException("Adoption Details not found"));

				Integer createdBy = arDetailsEntity.getCreatedBy();
				LocalDateTime createdOn = arDetailsEntity.getCreatedOn();

				ModelMapper modelMapper = new ModelMapper();
				Date deliveryDate = null;

				applicationEntity = modelMapper.map(request.getFatherInformation(),
						ApplicationAdoptionDetailEntity.class);

				modelMapper.map(request.getMotherInformation(), applicationEntity);
				modelMapper.map(request.getGeneralInformation(), applicationEntity);
				modelMapper.map(request.getChildInformation(), applicationEntity);

				applicationEntity.setUpdatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedOn(LocalDateTime.now());
				applicationEntity.setIsActive(true);
				// set default status
//				applicationEntity.setNodStatus(1);
				applicationEntity.setApplicationRegisterId(applicationRegisterId);

				applicationEntity.setCreatedBy(createdBy);
				applicationEntity.setCreatedOn(createdOn);

				// modelMapper.map(applicationEntity, nodDetailsEntity);

				// nodDetailsEntity.setCreatedBy(createdBy);
				// nodDetailsEntity.setCreatedOn(createdOn);

				ApplicationAdoptionDetailEntity savedApplicationEntity = adoptionDetailRepository
						.save(applicationEntity);

			}

			// End: Added by Sayan

			applicationRegisterEntity = applicationRegisterRepository
					.findByApplicationNo(request.getGeneralInformation().getApplicationNo());

//			// list - to delete id's
//			if (request.getDeletedFileIdList() != null)
//				for (Integer i : request.getDeletedFileIdList()) {
//					applicationDocumentRepository.deleteByFileId(i);
//				}

			// update files
			if (request.getDeletedFiles() != null) {
				List<Integer> deletedFileIds = new ArrayList<Integer>();
				for (DeletedFileListModel item : request.getDeletedFiles()) {
					deletedFileIds.add(item.getFileId());
				}
				documentRepository.updateFileIdIsActive(deletedFileIds);
			}

			// setting all values in response
			responseObj.setChildInformation(request.getChildInformation());
			responseObj.setFatherInformation(request.getFatherInformation());

			responseObj.setMotherInformation(request.getMotherInformation());
			responseObj.setUploadFileData(docEntityResponseList);
			request.getGeneralInformation().setApplicationRegisterId(applicationRegisterId);
			request.getGeneralInformation().setCurrentStageId(applicationRegisterEntity.getCurrentStatusId());
			responseObj.setGeneralInformation(request.getGeneralInformation());
			responseObj.setLoginUserId(request.getLoginUserId());

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(responseObj);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR);
			throw new RuntimeException("Error during saving AR draft, rolling back transaction", e);
		}
		logger.info("Exit Method " + "saveARDraft");
		return response;
	}

}
