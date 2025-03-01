package com.gog.civilregistry.adoption.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.gog.civilregistry.adoption.dto.GenerateAdoptionCertificateDTO;
import com.gog.civilregistry.adoption.dto.GetCivilRegistryNumberDTO;
import com.gog.civilregistry.adoption.entity.AdoptionApplicationDocumentEntity;
import com.gog.civilregistry.adoption.entity.ApplicationAdoptionCertificateDetailEntity;
import com.gog.civilregistry.adoption.entity.ApplicationAdoptionDetailEntity;
import com.gog.civilregistry.adoption.entity.ApplicationRegisterEntity;
import com.gog.civilregistry.adoption.model.ACDownloadRequest;
import com.gog.civilregistry.adoption.model.ACDownloadResponse;
import com.gog.civilregistry.adoption.model.ACDownloadResponseDTO;
import com.gog.civilregistry.adoption.model.AdoptionCertInformation;
import com.gog.civilregistry.adoption.model.ApplicationTrackStatus;
import com.gog.civilregistry.adoption.model.ApplicationTrackStatusResponse;
import com.gog.civilregistry.adoption.model.ApplyBirthCertificateRequest;
import com.gog.civilregistry.adoption.model.ApprovedAdoptionCertDetails;
import com.gog.civilregistry.adoption.model.ApprovedAdoptionRegistrationDetails;
import com.gog.civilregistry.adoption.model.BirthCertificateApplicationResponseProjection;
import com.gog.civilregistry.adoption.model.BirthCertificateApplyListResponse;
import com.gog.civilregistry.adoption.model.ByteArrayMultipartFile;
import com.gog.civilregistry.adoption.model.ChildAdoptionDetailsProjection;
import com.gog.civilregistry.adoption.model.ChildInformation;
import com.gog.civilregistry.adoption.model.DeletedFileListModel;
import com.gog.civilregistry.adoption.model.DocListDto;
import com.gog.civilregistry.adoption.model.DocListRequest;
import com.gog.civilregistry.adoption.model.DocListResponse;
import com.gog.civilregistry.adoption.model.FatherInformation;
import com.gog.civilregistry.adoption.model.GeneralInformation;
import com.gog.civilregistry.adoption.model.GenerateAdoptionCertificateRequest;
import com.gog.civilregistry.adoption.model.MotherInformation;
import com.gog.civilregistry.adoption.model.NewFileUploadResponse;
import com.gog.civilregistry.adoption.model.ProcessApplicationModel;
import com.gog.civilregistry.adoption.model.SaveARDraftRequest;
import com.gog.civilregistry.adoption.model.SaveARDraftResponse;
import com.gog.civilregistry.adoption.model.SaveAdoptionCertRequest;
import com.gog.civilregistry.adoption.model.SaveAdoptionCertResponse;
import com.gog.civilregistry.adoption.model.SearchApplicationACDto;
import com.gog.civilregistry.adoption.model.SearchApplicationACRequest;
import com.gog.civilregistry.adoption.model.SearchApplicationACResponse;
import com.gog.civilregistry.adoption.model.SearchApplicationARDto;
import com.gog.civilregistry.adoption.model.SearchApplicationARRequest;
import com.gog.civilregistry.adoption.model.SearchApplicationARResponse;
import com.gog.civilregistry.adoption.model.TownCodeProjection;
import com.gog.civilregistry.adoption.model.TownProjection;
import com.gog.civilregistry.adoption.model.TrackAppUserDto;
import com.gog.civilregistry.adoption.model.TrackAppUserRequest;
import com.gog.civilregistry.adoption.model.TrackAppUserResponse;
import com.gog.civilregistry.adoption.model.UploadFileData;
import com.gog.civilregistry.adoption.model.VaultRequest;
import com.gog.civilregistry.adoption.model.VaultResponse;
import com.gog.civilregistry.adoption.model.WorkflowInformation;
import com.gog.civilregistry.adoption.model.WorkflowUpdateModel;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;
import com.gog.civilregistry.adoption.repository.AdoptionApplicationDocumentRepository;
import com.gog.civilregistry.adoption.repository.ApplicationAdoptionCertificateDetailRepository;
import com.gog.civilregistry.adoption.repository.ApplicationAdoptionDetailRepository;
import com.gog.civilregistry.adoption.repository.ApplicationRegisterRepository;
import com.gog.civilregistry.adoption.repository.custom.AdoptionRepositoryCustom;
import com.gog.civilregistry.adoption.repository.custom.ApplicationRepositoryCustom;
import com.gog.civilregistry.adoption.service.AdoptionService;
import com.gog.civilregistry.adoption.service.DMSService;
import com.gog.civilregistry.adoption.service.IntegrationApiService;
import com.gog.civilregistry.adoption.util.CommonConstants;
import com.gog.civilregistry.adoption.model.UpdateCertificateFileRequest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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
	ApplicationAdoptionDetailRepository applicationAdoptionDetailRepository;

	@Autowired
	ApplicationRepositoryCustom applicationRepositoryCustom;

	@Autowired
	ApplicationAdoptionCertificateDetailRepository applicationAdoptionCertRepository;
	
	@Autowired
	AdoptionRepositoryCustom adoptionRepositoryCustom;

	@Autowired
	AdoptionApplicationDocumentRepository documentRepository;

	@Autowired
	ApplicationRegisterRepository applicationRegisterRepository;

	@Autowired
	ApplicationAdoptionDetailRepository adoptionDetailRepository;

	@Override
	public ServiceResponse trackApplicationStatus(ApplicationTrackStatus request) {
		logger.info("Entry Method: trackApplicationStatus");
		ServiceResponse response = new ServiceResponse();
		ModelMapper modelMapper = new ModelMapper();

		try {
			Long applicationId = request.getApplicationId();
			if (applicationId == null) {
				logger.error("Application ID is null.");
				response.setStatus(CommonConstants.ERROR_STATUS);
				response.setMessage("Application ID cannot be null.");
				return response;
			}

			List<Object[]> trackApplicationStatusList = applicationAdoptionDetailRepository
					.trackApplicationStatus(applicationId);

			if (trackApplicationStatusList == null || trackApplicationStatusList.isEmpty()) {
				logger.warn("No data found for application ID: " + applicationId);
				response.setStatus(CommonConstants.ERROR_STATUS);
				response.setMessage("No application status found for the given ID.");
				return response;
			}

			List<ApplicationTrackStatusResponse> responseList = trackApplicationStatusList.stream().map(row -> {
				ApplicationTrackStatusResponse trackStatusResponse = new ApplicationTrackStatusResponse();

				trackStatusResponse.setWorkflowId((Long) row[0]);
				trackStatusResponse.setApplicationId((Long) row[1]);
				trackStatusResponse.setSubmittedBy((String) row[2]);
				trackStatusResponse.setSubmittedTo((String) row[3]);
				trackStatusResponse.setDateOfSubmission((String) row[4]);
				trackStatusResponse.setClaimedBy((String) row[5]);
				trackStatusResponse.setClaimedDate((String) row[6]);
				trackStatusResponse.setReleasedDate((String) row[7]);
				trackStatusResponse.setActionStage((String) row[8]);

				return trackStatusResponse;
			}).collect(Collectors.toList());

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS);
			response.setResponseObject(responseList);

		} catch (Exception e) {
			logger.error("An error occurred in trackApplicationStatus: ", e);
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}

		return response;
	}

	

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
				processAr.setParishId(request.getMotherInformation().getMotherParish());
				

				processAr.setCitizenId(request.getChildInformation().getChildCitizenId());
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
				processAr.setStatusId(request.getGeneralInformation().getCurrentStatus());
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

			// normal application save process for parent and child

			if (request.getGeneralInformation().getApplicationAdoptionId() == null
					|| request.getGeneralInformation().getApplicationAdoptionId() == 0L) {

				Date deliveryDate = null;
//				SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
//				SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//				Date date = inputFormat.parse(request.getGeneralInformation().getCod());
//				String formattedCODate = outputFormat.format(date);
//				request.getGeneralInformation().setCourtOrderDate(formattedCODate);

				applicationEntity = modelMapper.map(request.getFatherInformation(),
						ApplicationAdoptionDetailEntity.class);
				modelMapper.map(request.getMotherInformation(), applicationEntity);
				modelMapper.map(request.getGeneralInformation(), applicationEntity);
				if (request.getChildInformation().getDob() != null)
					applicationEntity.setChildDateOfBirth(formatter.parse(request.getChildInformation().getDob()));

				if (request.getGeneralInformation().getCod() != null)
					applicationEntity.setCourtOrderDate(formatter.parse(request.getGeneralInformation().getCod()));

				if (request.getFatherInformation().getFatherDOB() != null)
					applicationEntity
							.setFatherDateOfBirth(formatter.parse(request.getFatherInformation().getFatherDOB()));

				if (request.getMotherInformation().getMotherDOB() != null)
					applicationEntity
							.setMotherDateOfBirth(formatter.parse(request.getMotherInformation().getMotherDOB()));

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
				if (request.getChildInformation().getDob() != null)
					applicationEntity.setChildDateOfBirth(formatter.parse(request.getChildInformation().getDob()));

				if (request.getGeneralInformation().getCod() != null)
					applicationEntity.setCourtOrderDate(formatter.parse(request.getGeneralInformation().getCod()));
				modelMapper.map(request.getChildInformation(), applicationEntity);

				if (request.getFatherInformation().getFatherDOB() != null)
					applicationEntity
							.setFatherDateOfBirth(formatter.parse(request.getFatherInformation().getFatherDOB()));

				if (request.getMotherInformation().getMotherDOB() != null)
					applicationEntity
							.setMotherDateOfBirth(formatter.parse(request.getMotherInformation().getMotherDOB()));

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

			// delete files
			if (request.getDeletedFiles() != null) {
				List<Integer> deletedFileIds = new ArrayList<Integer>();
				for (DeletedFileListModel item : request.getDeletedFiles()) {
					deletedFileIds.add(item.getFileId());
				}
				documentRepository.deleteByFileId(deletedFileIds);
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

	@Override
	public ServiceResponse getAR(GeneralInformation request) {
		logger.info("Entry Method " + " getAR");
		ServiceResponse response = new ServiceResponse();

		ChildInformation childInformation = new ChildInformation();
		List<ChildInformation> childResp = new ArrayList<ChildInformation>();
		FatherInformation fatherInformation = new FatherInformation();

		MotherInformation motherInformation = new MotherInformation();

		GeneralInformation generalInformation = new GeneralInformation();
		List<UploadFileData> fileDataList = new ArrayList<UploadFileData>();
		SaveARDraftResponse res = new SaveARDraftResponse();
		ApplicationRegisterEntity applicationRegisterEntity = new ApplicationRegisterEntity();
		ApplicationAdoptionDetailEntity applicationAR = new ApplicationAdoptionDetailEntity();

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//		String formattedDate = formatter.format(currentDate);

		try {
			applicationRegisterEntity = applicationRegisterRepository
					.findByApplicationRegisterId(request.getApplicationRegisterId());
			applicationAR = adoptionDetailRepository
					.findByApplicationRegisterId(applicationRegisterEntity.getApplicationRegisterId());
			List<AdoptionApplicationDocumentEntity> docEntityList = documentRepository
					.findByApplicationRegisterIdAndIsActive(applicationRegisterEntity.getApplicationRegisterId(), true);
			// process document entity list to model - upload file data
			for (AdoptionApplicationDocumentEntity docEntity : docEntityList) {
				UploadFileData fileData = new UploadFileData();
				fileData.setFileName(docEntity.getApplicationDocName());
				fileData.setApplicationRegisterId(docEntity.getApplicationRegisterId());
				fileData.setApplicationDocDmsId(docEntity.getApplicationDocDmsId());
				fileData.setApplicationDocId(docEntity.getApplicationDocId());
				fileData.setFileSubject(docEntity.getApplicationDocSubject());
				fileData.setDocTypeId(docEntity.getDocumentTypeId());
				fileData.setReferenceId(docEntity.getApplicationDocDmsId());
				fileData.setDocTypeCode(docEntity.getDocumentTypeCode());
				fileDataList.add(fileData);
			}

			fatherInformation = modelMapper.map(applicationAR, FatherInformation.class);

			motherInformation = modelMapper.map(applicationAR, MotherInformation.class);

			generalInformation = modelMapper.map(applicationAR, GeneralInformation.class);

			childInformation = modelMapper.map(applicationAR, ChildInformation.class);

			List<Integer> citizenIds = Arrays.asList(
					fatherInformation.getFatherCitizenId() != null ? fatherInformation.getFatherCitizenId() : 0,
					motherInformation.getMotherCitizenId() != null ? motherInformation.getMotherCitizenId() : 0,
					childInformation.getChildCitizenId() != null ? childInformation.getChildCitizenId() : 0,
					childInformation.getFatherCitizenIdOld() != null ? childInformation.getFatherCitizenIdOld() : 0,
					childInformation.getMotherCitizenIdOld() != null ? childInformation.getMotherCitizenIdOld() : 0

			);

//			List<Integer> citizenIds = Arrays.asList(1,2,3,4,5
//
//			);

			List<GetCivilRegistryNumberDTO> civilRegNoDetailsList = applicationRegisterRepository
					.getCivilRegNoFromCitizenId(citizenIds);

			for (GetCivilRegistryNumberDTO citizenDetails : civilRegNoDetailsList) {

				if (citizenDetails.getCitizen_id().equals(fatherInformation.getFatherCitizenId())) {
					fatherInformation.setFatherCivilRegistryNumber(citizenDetails.getCivilRegistryNumber());
				}

				if (citizenDetails.getCitizen_id().equals(motherInformation.getMotherCitizenId())) {
					motherInformation.setMotherCivilRegistryNumber(citizenDetails.getCivilRegistryNumber());
				}
				if (citizenDetails.getCitizen_id().equals(childInformation.getChildCitizenId())) {
					childInformation.setChildCivilRegistryNumber(citizenDetails.getCivilRegistryNumber());
				}

				if (citizenDetails.getCitizen_id().equals(childInformation.getFatherCitizenIdOld())) {
					childInformation.setFatherCivilRegistryNumberOld(citizenDetails.getCivilRegistryNumber());
				}

				if (citizenDetails.getCitizen_id().equals(childInformation.getMotherCitizenIdOld())) {
					childInformation.setMotherCivilRegistryNumberOld(citizenDetails.getCivilRegistryNumber());
				}

			}

//			InstituteProjection proj = birthRegisterRepository.findInstituteById(generalInformation.getInstituteId());
//			generalInformation.setInstituteName(proj.getInstituteName());

			if (applicationAR.getCourtOrderDate() != null)
				generalInformation.setCod(formatter.format(applicationAR.getCourtOrderDate()));
			if (applicationAR.getChildDateOfBirth() != null)
				childInformation.setDob(formatter.format(applicationAR.getChildDateOfBirth()));
			if (applicationAR.getFatherDateOfBirth() != null)
				fatherInformation.setFatherDOB(formatter.format(applicationAR.getFatherDateOfBirth()));
			if (applicationAR.getMotherDateOfBirth() != null)
				motherInformation.setMotherDOB(formatter.format(applicationAR.getMotherDateOfBirth()));

			generalInformation.setCurrentStageId(applicationRegisterEntity.getCurrentStatusId());
			generalInformation.setApplicationNo(request.getApplicationNo());
			generalInformation.setApplicationTypeId(applicationRegisterEntity.getApplicationTypeId());

			res.setFatherInformation(fatherInformation);
			res.setChildInformation(childInformation);
			res.setMotherInformation(motherInformation);
			res.setGeneralInformation(generalInformation);
			res.setUploadFileData(fileDataList);

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(res);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getAR");
		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ServiceResponse submitAdoptionRegistration(MultipartFile[] files, String requeststr) {
		// TODO Auto-generated method stub
		logger.info("Entry Method " + "submitAdoptionRegistration");
		ServiceResponse response = new ServiceResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SaveARDraftResponse responseObj = new SaveARDraftResponse();
		List<UploadFileData> docEntityResponseList = new ArrayList<UploadFileData>();
		ApplicationAdoptionDetailEntity applicationEntity = new ApplicationAdoptionDetailEntity();
		ApplicationAdoptionDetailEntity savedApplicationEntity = new ApplicationAdoptionDetailEntity();
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

			// first save application in register entity
			// check if application_no of generalInformation is not 0 or null - then only
			// insert

			WorkflowInformation workflowInfoRequest = request.getWorkflowInformation();

			applicationNumber = request.getGeneralInformation().getApplicationNo();
			applicationRegisterId = request.getGeneralInformation().getApplicationRegisterId();

			if (request.getGeneralInformation().getApplicationNo() == null) {

				ProcessApplicationModel processNod = new ProcessApplicationModel();
				// processNod.setFlag("D");
				// processNod.setInstituteId(request.getGeneralInformation().getInstituteId());
				// processNod.setRoleId(2);
				processNod.setUserId(request.getLoginUserId());
				processNod.setApplicationTypeId(request.getGeneralInformation().getApplicationTypeId());
				processNod.setParishId(request.getMotherInformation().getMotherParish());
				
				processNod.setStatusId(workflowInfoRequest.getNextStatusId());
				processNod.setCitizenId(request.getChildInformation().getChildCitizenId());
				// processNod.setApplicationRegisterId(applicationRegisterEntity.getApplicationRegisterId());

				Map<String, Object> resultMap = adoptionRepositoryCustom.createNewApplication(processNod);

				applicationNumber = (String) resultMap.get("applicationNumber");
				applicationRegisterId = (Long) resultMap.get("applicationRegisterId");
				// entry no from Sanjay- set in entity before save

				if (applicationNumber == null || applicationNumber.equals("")) {
					throw new RuntimeException("Unable to crate Application Number");
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

			if (request.getGeneralInformation().getApplicationAdoptionId() == null
					|| request.getGeneralInformation().getApplicationAdoptionId() == 0L) {

				Date deliveryDate = null;

				applicationEntity = modelMapper.map(request.getFatherInformation(),
						ApplicationAdoptionDetailEntity.class);
				modelMapper.map(request.getMotherInformation(), applicationEntity);
				modelMapper.map(request.getGeneralInformation(), applicationEntity);
				if (request.getChildInformation().getDob() != null)
					applicationEntity.setChildDateOfBirth(formatter.parse(request.getChildInformation().getDob()));

				if (request.getGeneralInformation().getCod() != null)
					applicationEntity.setCourtOrderDate(formatter.parse(request.getGeneralInformation().getCod()));

				if (request.getFatherInformation().getFatherDOB() != null)
					applicationEntity
							.setFatherDateOfBirth(formatter.parse(request.getFatherInformation().getFatherDOB()));

				if (request.getMotherInformation().getMotherDOB() != null)
					applicationEntity
							.setMotherDateOfBirth(formatter.parse(request.getMotherInformation().getMotherDOB()));

				modelMapper.map(request.getChildInformation(), applicationEntity);

				applicationEntity.setCreatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedOn(LocalDateTime.now());
				applicationEntity.setCreatedOn(LocalDateTime.now());
				applicationEntity.setIsActive(true);
				// set default status

				applicationEntity.setApplicationRegisterId(applicationRegisterId);

				savedApplicationEntity = adoptionDetailRepository.save(applicationEntity);

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
				if (request.getChildInformation().getDob() != null)
					applicationEntity.setChildDateOfBirth(formatter.parse(request.getChildInformation().getDob()));

				if (request.getGeneralInformation().getCod() != null)
					applicationEntity.setCourtOrderDate(formatter.parse(request.getGeneralInformation().getCod()));
				modelMapper.map(request.getChildInformation(), applicationEntity);

				if (request.getFatherInformation().getFatherDOB() != null)
					applicationEntity
							.setFatherDateOfBirth(formatter.parse(request.getFatherInformation().getFatherDOB()));

				if (request.getMotherInformation().getMotherDOB() != null)
					applicationEntity
							.setMotherDateOfBirth(formatter.parse(request.getMotherInformation().getMotherDOB()));

				applicationEntity.setUpdatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedOn(LocalDateTime.now());
				applicationEntity.setIsActive(true);
				// set default status
//							applicationEntity.setNodStatus(1);
				applicationEntity.setApplicationRegisterId(applicationRegisterId);

				applicationEntity.setCreatedBy(createdBy);
				applicationEntity.setCreatedOn(createdOn);

				// modelMapper.map(applicationEntity, nodDetailsEntity);

				// nodDetailsEntity.setCreatedBy(createdBy);
				// nodDetailsEntity.setCreatedOn(createdOn);

				savedApplicationEntity = adoptionDetailRepository.save(applicationEntity);

			}

			WorkflowUpdateModel workflowUpdateModel = new WorkflowUpdateModel();
			modelMapper.map(workflowInfoRequest, workflowUpdateModel);
			workflowUpdateModel.setApplicationRegisterId(applicationRegisterId);
			workflowUpdateModel.setApplicationTypeId(request.getGeneralInformation().getApplicationTypeId());
			workflowUpdateModel.setIsDraft(0);

			Map<String, Object> resultMap = adoptionRepositoryCustom.updateWorkflowDetails(workflowUpdateModel);

			// End: Added by Sayan

			// setting all values in response

			// update files
			if (request.getDeletedFiles() != null) {
				List<Integer> deletedFileIds = new ArrayList<Integer>();
				for (DeletedFileListModel item : request.getDeletedFiles()) {
					deletedFileIds.add(item.getFileId());
				}
				documentRepository.updateFileIdIsActive(deletedFileIds);
			}

			responseObj.setChildInformation(request.getChildInformation());
			responseObj.setFatherInformation(request.getFatherInformation());

			responseObj.setMotherInformation(request.getMotherInformation());
			responseObj.setUploadFileData(docEntityResponseList);
			request.getGeneralInformation().setApplicationRegisterId(applicationRegisterId);
			responseObj.setGeneralInformation(request.getGeneralInformation());
			responseObj.setLoginUserId(request.getLoginUserId());

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(responseObj);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR);
			throw new RuntimeException("Error during submitting marriage registration, rolling back transaction", e);
		}
		logger.info("Exit Method " + "submitAdoptionRegistration");
		return response;
	}

	@Override
	public ServiceResponse getDocList(DocListRequest request) {
		logger.info("Entry Method: getDocList");
		ServiceResponse response = new ServiceResponse();

		try {
			Integer applicationTypeId = request.getApplicationTypeId();
			// List<DocListDto> docList =
			// deathRepositoryCustom.getDocumentList(applicationTypeId);

			List<Object[]> resultList = adoptionDetailRepository.getDocList(request.getApplicationRegisterId(),
					request.getApplicationTypeId(), request.getLoggedInRoleCode(), request.getCitizenId());

			List<DocListDto> docList = resultList.stream().map(result -> {
				Object[] row = (Object[]) result;
				DocListDto dto = new DocListDto();
				dto.setDocTypeId(row[0] != null ? (Integer) row[0] : null);
				dto.setDataValue(row[1] != null ? (String) row[1] : "");
				dto.setDataDescription(row[2] != null ? (String) row[2] : "");
				dto.setMasterDataTypeId(row[3] != null ? (Integer) row[3] : null);
				dto.setCitizenId(row[4] != null ? (Integer) row[4] : null);
				dto.setDocName(row[5] != null ? (String) row[5] : null);
				dto.setDocSubject(row[6] != null ? (String) row[6] : null);
				dto.setApplicationDocDmsId(row[7] != null ? (String) row[7] : null);
				dto.setIsGeneratedBySystem(row[8] != null ? (Short) row[8] : null);
				return dto;
			}).collect(Collectors.toList());

			DocListResponse docListResponse = new DocListResponse();
			docListResponse.setDocListResponse(docList);

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS);
			response.setResponseObject(docListResponse);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getDocList");
		return response;

	}

	@Override
	public ServiceResponse searchApplicationAR(SearchApplicationARRequest request) {
		logger.info("Entry Method: searchApplicationAR");
		ServiceResponse response = new ServiceResponse();

		try {
			Long applicationRegisterId = null;
			String applicationNumber = request.getApplicationNumber();
			String dateOfBirthStr = request.getDateOfBirthStr();

			Date dateOfBirth = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			if (dateOfBirthStr != null && !dateOfBirthStr.equals("")) {
				try {
					dateOfBirth = dateFormat.parse(dateOfBirthStr);
				} catch (Exception e) {
					System.out.println("Error parsing the date: " + e.getMessage());
				}
			}

			String childName = request.getChildName();
			String motherName = request.getMotherName();
			String fatherName = request.getFatherName();
			Integer parishId = request.getParishId();
			Integer genderId = request.getGenderId();

			if (applicationNumber != null && !applicationNumber.equals("")) {
				applicationRegisterId = applicationAdoptionDetailRepository.getIdFromApplicationNo(applicationNumber);
				if (applicationRegisterId == null || applicationRegisterId == 0l) {
					response.setStatus(CommonConstants.SUCCESS_STATUS);
					response.setMessage(CommonConstants.SUCCESS);
					return response;
				}
			}

			List<SearchApplicationARDto> searchApplicationARList = adoptionRepositoryCustom.searchApplicationAR(
					childName, motherName, fatherName, dateOfBirth, applicationNumber, parishId, genderId);

			SearchApplicationARResponse searchApplicationARResponse = new SearchApplicationARResponse();
			searchApplicationARResponse.setSearchApplicationARResponse(searchApplicationARList);

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS);
			response.setResponseObject(searchApplicationARResponse);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}

		return response;
	}

	@Override
	public ServiceResponse searchApplicationAC(SearchApplicationACRequest request) {
		logger.info("Entry Method: searchApplicationAC");
		ServiceResponse response = new ServiceResponse();

		try {
			Long applicationRegisterId = null;
			String applicationNumber = request.getApplicationNumber();
			String dateOfBirthStr = request.getDateOfBirthStr();

			Date dateOfBirth = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			if (dateOfBirthStr != null && !dateOfBirthStr.equals("")) {
				try {
					dateOfBirth = dateFormat.parse(dateOfBirthStr);
				} catch (Exception e) {
					System.out.println("Error parsing the date: " + e.getMessage());
				}
			}

			String childName = request.getChildName();
			String motherName = request.getMotherName();
			String fatherName = request.getFatherName();
			Integer parishId = request.getParishId();
			Integer genderId = request.getGenderId();

			if (applicationNumber != null && !applicationNumber.equals("")) {
				applicationRegisterId = applicationAdoptionDetailRepository.getIdFromApplicationNo(applicationNumber);
				if (applicationRegisterId == null || applicationRegisterId == 0l) {
					response.setStatus(CommonConstants.SUCCESS_STATUS);
					response.setMessage(CommonConstants.SUCCESS);
					return response;
				}
			}

			List<SearchApplicationACDto> searchApplicationACList = adoptionRepositoryCustom.searchApplicationAC(
					childName, motherName, fatherName, dateOfBirth, applicationNumber, parishId, genderId);

			SearchApplicationACResponse searchApplicationACResponse = new SearchApplicationACResponse();
			searchApplicationACResponse.setSearchApplicationACResponse(searchApplicationACList);

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS);
			response.setResponseObject(searchApplicationACResponse);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}

		return response;
	}

	@Override
	public ServiceResponse trackAppUser(TrackAppUserRequest request) {
		logger.info("Entry Method: trackAppUser");
		ServiceResponse response = new ServiceResponse();

		try {
			Integer loggedInUserId = request.getLoggedInUserId();
			List<TrackAppUserDto> trackAppUserList = adoptionRepositoryCustom.trackApplicationUserList(loggedInUserId);

			TrackAppUserResponse trackAppUserResponse = new TrackAppUserResponse();
			trackAppUserResponse.setTrackAppUserResponse(trackAppUserList);

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS);
			response.setResponseObject(trackAppUserResponse);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " trackAppUser");
		return response;

	}

	@Override
	public ServiceResponse getVault(VaultRequest request) {
		logger.info("Entry Method " + " getVault");
		ServiceResponse response = new ServiceResponse();
		try {
			List<VaultResponse> results = applicationRepositoryCustom.executeVault(request.getApplicationTypeCode(),
					request.getState(), request.getUserId(), request.getInstituteId(), request.getRoleId(),
					request.getPageNumber(), request.getPerPageCount(), request.getSearchText());
			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(results);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getVault");
		return response;
	}

	@Override
	public ServiceResponse saveAndSubmitByDepartmentUsers(MultipartFile[] files, String requestStr) {

		logger.info("Entry Method " + " saveAndSubmitByDepartmentUsers");
		ServiceResponse response = new ServiceResponse();
		List<Integer> list = new ArrayList<Integer>();
		ObjectMapper mapper = new ObjectMapper();
		String dmsReferenceId = null;
		List<UploadFileData> docEntityResponseList = new ArrayList<UploadFileData>();
		Integer yearOfDeath = null;

		try {

			SaveARDraftRequest request = null;
			requestStr = requestStr.replaceAll("\\n", "").replaceAll("\\t", "");
			request = mapper.readValue(requestStr, SaveARDraftRequest.class);

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

			for (UploadFileData documentAttachment : request.getUploadFileData()) {
				// save only if file id is null or 0
				AdoptionApplicationDocumentEntity docEntity = new AdoptionApplicationDocumentEntity();
				docEntity.setApplicationDocDmsId(documentAttachment.getReferenceId());
				docEntity.setApplicationDocName(documentAttachment.getFileName());
				docEntity.setApplicationRegisterId(request.getGeneralInformation().getApplicationRegisterId());
				docEntity.setDocumentTypeId(documentAttachment.getDocTypeId());
				docEntity.setDocumentTypeCode(documentAttachment.getDocTypeCode());
				docEntity.setApplicationDocSubject(documentAttachment.getFileSubject());
				docEntity.setCreatedBy(request.getLoginUserId());
				docEntity.setUpdatedBy(request.getLoginUserId());
				docEntity.setUpdatedOn(LocalDateTime.now());
				docEntity.setCreatedOn(LocalDateTime.now());
				docEntity.setIsActive(true);
				// save or update based on applicationDocId
				if (documentAttachment.getApplicationDocId() != 0L
						|| documentAttachment.getApplicationDocId() != null) {
					docEntity.setApplicationDocId(documentAttachment.getApplicationDocId());

				}
				docEntity = documentRepository.save(docEntity);
				// setting details in uploadFileData
				documentAttachment.setApplicationRegisterId(request.getGeneralInformation().getApplicationRegisterId());
				documentAttachment.setApplicationDocDmsId(documentAttachment.getReferenceId());
				documentAttachment.setApplicationDocId(docEntity.getApplicationDocId());
				docEntityResponseList.add(documentAttachment);
			}

			WorkflowInformation workflowInformation = request.getWorkflowInformation();

			if (request.getGeneralInformation().getApplicationTypeCode() != null && request.getGeneralInformation()
					.getApplicationTypeCode().equalsIgnoreCase(CommonConstants.ADOPTION_REGISTRATION)) {

				String entryNo = request.getGeneralInformation().getEntryNo();

				ApplicationAdoptionDetailEntity applicationEntity = applicationAdoptionDetailRepository
						.findByApplicationRegisterId(request.getGeneralInformation().getApplicationRegisterId());

				if (applicationEntity != null) {
					applicationEntity.setEntryNo(entryNo);
					ApplicationAdoptionDetailEntity savedApplicationEntity = applicationAdoptionDetailRepository
							.save(applicationEntity);
				}
			}

			WorkflowUpdateModel workflowUpdateModel = new WorkflowUpdateModel();
			modelMapper.map(workflowInformation, workflowUpdateModel);

			workflowUpdateModel.setApplicationRegisterId(request.getGeneralInformation().getApplicationRegisterId());
			workflowUpdateModel.setApplicationTypeId(request.getGeneralInformation().getApplicationTypeId());
			workflowUpdateModel.setIsDraft(request.getIsDraft());

			Map<String, Object> resultMap = adoptionRepositoryCustom.updateWorkflowDetails(workflowUpdateModel);

			if (workflowUpdateModel.getNextStatusId() != null
					&& workflowUpdateModel.getNextStatusId() == Integer.valueOf(CommonConstants.APP_APPROVED_STATUS)) {
				if (request.getGeneralInformation().getApplicationTypeCode() != null && request.getGeneralInformation()
						.getApplicationTypeCode().equalsIgnoreCase(CommonConstants.ADOPTION_REGISTRATION)) {

					List<ApprovedAdoptionRegistrationDetails> approveAdoptionRegistration = adoptionRepositoryCustom
							.approveAdoptionRegistration(workflowUpdateModel);
					response.setResponseObject(approveAdoptionRegistration);
				}
				
				if (request.getGeneralInformation().getApplicationTypeCode() != null && request.getGeneralInformation()
						.getApplicationTypeCode().equalsIgnoreCase(CommonConstants.ADOPTION_CERTIFICATE)) {
					
					List<ApprovedAdoptionCertDetails> approveAdoptionCertificate = adoptionRepositoryCustom.approveAdoptionCertificate(workflowUpdateModel);
					
					List<GenerateAdoptionCertificateDTO> certificateDetailsList = applicationAdoptionCertRepository.fetchAdoptionCertificateDetails(request.getGeneralInformation().getApplicationRegisterId());

					GenerateAdoptionCertificateDTO certificateDetails = certificateDetailsList.get(0);
					
					GenerateAdoptionCertificateRequest generateAdoptionCertificateRequest = new GenerateAdoptionCertificateRequest();
					generateAdoptionCertificateRequest.setApplicationRegisterId(request.getGeneralInformation().getApplicationRegisterId());
					
					MultipartFile adoptionCertToUpload = generateAdoptionCertificate(certificateDetails, "CREATE");
				    
					 MultipartFile[] fileToUpload = new MultipartFile[1];
					 fileToUpload[0] = adoptionCertToUpload;
					 NewFileUploadResponse fileUploadResponse = dmsService.uploadFileToAlfresco(fileToUpload);
					 dmsReferenceId = fileUploadResponse.getFileUploadResponse().get(0);
					 
					 UpdateCertificateFileRequest updateCertificateFileRequest = new UpdateCertificateFileRequest();
					 updateCertificateFileRequest.setCitizenId(request.getChildInformation().getChildCitizenId());
					 updateCertificateFileRequest.setCurrentUserId(request.getWorkflowInformation().getAssignedByUserId());
					 updateCertificateFileRequest.setApplicationTypeCode(request.getGeneralInformation().getApplicationTypeCode());
					 updateCertificateFileRequest.setDmsRefId(dmsReferenceId);
				     
					 Map<String, Object> result = adoptionRepositoryCustom.updateCertificateFile(updateCertificateFileRequest);
                     String retcode = (String) result.get("re_code");
                     String retmsg = (String) result.get("re_msg");

						if (retcode != null && !retcode.equals("")) {
							if (!retcode.equals("0")) {
								throw new RuntimeException("Unable to update Marriage License");
							}
						}	
					
					response.setResponseObject(approveAdoptionCertificate);
				}
			}

			SaveARDraftResponse responseObj = new SaveARDraftResponse();

			responseObj.setGeneralInformation(request.getGeneralInformation());
			responseObj.setWorkflowInformation(request.getWorkflowInformation());
			responseObj.setUploadFileData(docEntityResponseList);

			responseObj.setLoginUserId(request.getLoginUserId());
			responseObj.setIsDraft(request.getIsDraft());

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR);
			throw new RuntimeException("Error while submitting the form", e);
		}
		logger.info("Exit Method " + " saveAndSubmitByDepartmentUsers");
		return response;
	}

	@Override
	public ServiceResponse getChildDetailsForAdoption(ChildInformation request) {
		logger.info("Entry Method: getChildDetailsForAdoption");
		ServiceResponse response = new ServiceResponse();
		try {
			ChildAdoptionDetailsProjection childInfoFromCitizenRegister = applicationAdoptionDetailRepository
					.getChildInfoFromCitizenRegister(request.getChildCivilRegistryNumber());

			if (childInfoFromCitizenRegister == null) {
				childInfoFromCitizenRegister = applicationAdoptionDetailRepository
						.getChildInfoFromManageCitizen(request.getChildCivilRegistryNumber());
			}
			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS);
			response.setResponseObject(childInfoFromCitizenRegister);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getChildDetailsForAdoption");
		return response;
	}

	@Override
	public ServiceResponse searchACDownload(ACDownloadRequest request) {
		logger.info("Entry Method: searchACDownload");
		ServiceResponse response = new ServiceResponse();

		try {

			String formattedDateOfBirth = null;
			if (request.getDateOfBirthStr() != null && !request.getDateOfBirthStr().trim().isEmpty()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date parsedDate = dateFormat.parse(request.getDateOfBirthStr());
				formattedDateOfBirth = dateFormat.format(parsedDate);
			}

			logger.info("Formatted Date of adoption: {}", formattedDateOfBirth);

			List<ACDownloadResponseDTO> dtoList = applicationRegisterRepository.searchACDownloadList(
					request.getChildCivilRegistryNumber(), request.getChildName(), request.getMotherName(),
					request.getFatherName(), formattedDateOfBirth, request.getParishId(), request.getGenderId());

			if (dtoList == null || dtoList.isEmpty()) {
				logger.warn("No records found for the given input: {}", request);
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage("No records found.");
				response.setResponseObject(Collections.emptyList());
				return response;
			}

			List<ACDownloadResponse> responseList = new ArrayList<>();
			for (ACDownloadResponseDTO dto : dtoList) {
				ACDownloadResponse responseObj = new ACDownloadResponse();
				responseObj.setChildCitizenId(dto.getChildCitizenId());
				responseObj.setChildCivilRegistryNumber(dto.getChildCivilRegistryNumber());
				responseObj.setMotherCitizenId(dto.getMotherCitizenId());
				responseObj.setFatherCitizenId(dto.getFatherCitizenId());
				responseObj.setChildName(dto.getChildName());
				responseObj.setMotherName(dto.getMotherName());
				responseObj.setFatherName(dto.getFatherName());
				responseObj.setDateOfBirth(dto.getDateOfBirth());
				responseObj.setParishOfBirth(dto.getParishOfBirth());
				responseObj.setAdoptionCertificateNumber(dto.getAdoptionCertificateNumber());
				responseObj.setAdoptionCertificateDmsId(dto.getAdoptionCertificateDmsId());

				responseList.add(responseObj);
			}

			if (responseList.isEmpty()) {
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage("No records found with an approved birth certificate.");
				response.setResponseObject(Collections.emptyList());
			} else {
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage(CommonConstants.SUCCESS);
				response.setResponseObject(responseList);
			}

		} catch (Exception e) {
			logger.error("Error occurred in searchACDownload: {}", e.getMessage(), e);
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}

		logger.info("Exit Method: searchACDownload");
		return response;
	}

	@Override
	public ServiceResponse submitAdoptionCertificate(MultipartFile[] files, String requeststr) {
		// TODO Auto-generated method stub
		logger.info("Entry Method " + "submitAdoptionCertificate");
		ServiceResponse response = new ServiceResponse();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SaveAdoptionCertResponse responseObj = new SaveAdoptionCertResponse();
		List<UploadFileData> docEntityResponseList = new ArrayList<UploadFileData>();
		ApplicationAdoptionCertificateDetailEntity applicationEntity = new ApplicationAdoptionCertificateDetailEntity();
		ApplicationAdoptionCertificateDetailEntity savedApplicationEntity = new ApplicationAdoptionCertificateDetailEntity();
		Long applicationRegisterId = null;
		String applicationNumber = null;
		TownProjection fatherTown = null;
		TownProjection motherTown = null;

		try {
			// generating model request from string request

			ObjectMapper mapper = new ObjectMapper();
			SaveAdoptionCertRequest request = null;
			requeststr = requeststr.replaceAll("\\n", "").replaceAll("\\t", "");
			request = mapper.readValue(requeststr, SaveAdoptionCertRequest.class);

			WorkflowInformation workflowInfoRequest = request.getWorkflowInformation();

			applicationNumber = request.getAdoptionCertInformation().getApplicationNo();
			applicationRegisterId = request.getAdoptionCertInformation().getApplicationRegisterId();

			if (request.getAdoptionCertInformation().getApplicationNo() == null) {

				ProcessApplicationModel processNod = new ProcessApplicationModel();
				processNod.setUserId(request.getLoginUserId());
				processNod.setApplicationTypeId(request.getAdoptionCertInformation().getApplicationTypeId());
				processNod.setParishId(request.getAdoptionCertInformation().getMotherParish());
				processNod.setStatusId(workflowInfoRequest.getNextStatusId());
				processNod.setCitizenId(request.getAdoptionCertInformation().getChildCitizenId());
				// processNod.setApplicationRegisterId(applicationRegisterEntity.getApplicationRegisterId());

				Map<String, Object> resultMap = adoptionRepositoryCustom.createNewApplication(processNod);

				applicationNumber = (String) resultMap.get("applicationNumber");
				applicationRegisterId = (Long) resultMap.get("applicationRegisterId");
				// entry no from Sanjay- set in entity before save

				if (applicationNumber == null || applicationNumber.equals("")) {
					throw new RuntimeException("Unable to crate Application Number");
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

			if (request.getAdoptionCertInformation().getApplicationAdoptionCertificateId() == null
					|| request.getAdoptionCertInformation().getApplicationAdoptionCertificateId() == 0L) {

				Date deliveryDate = null;

				applicationEntity = modelMapper.map(request.getAdoptionCertInformation(),
						ApplicationAdoptionCertificateDetailEntity.class);

				applicationEntity.setCreatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedBy(request.getLoginUserId());
				applicationEntity.setIsActive(true);
				// set default status

				applicationEntity.setApplicationRegisterId(applicationRegisterId);

				savedApplicationEntity = applicationAdoptionCertRepository.save(applicationEntity);

				request.getAdoptionCertInformation().setApplicationAdoptionCertificateId(
						savedApplicationEntity.getApplicationAdoptionCertificateId());
				request.getAdoptionCertInformation().setApplicationNo(applicationNumber);
				request.getAdoptionCertInformation().setApplicationRegisterId(applicationRegisterId);

			} else {

				ApplicationAdoptionCertificateDetailEntity appCertEntity = applicationAdoptionCertRepository
						.findById(request.getAdoptionCertInformation().getApplicationAdoptionCertificateId())
						.orElseThrow(() -> new IllegalArgumentException("Adoption Certificate Details not found"));

				Integer createdBy = appCertEntity.getCreatedBy();
				LocalDateTime createdOn = appCertEntity.getCreatedOn();

				applicationEntity = modelMapper.map(request.getAdoptionCertInformation(),
						ApplicationAdoptionCertificateDetailEntity.class);

				applicationEntity.setUpdatedBy(request.getLoginUserId());
				applicationEntity.setUpdatedOn(LocalDateTime.now());
				applicationEntity.setIsActive(true);
				applicationEntity.setApplicationRegisterId(applicationRegisterId);
				applicationEntity.setCreatedBy(createdBy);
				applicationEntity.setCreatedOn(createdOn);

				savedApplicationEntity = applicationAdoptionCertRepository.save(applicationEntity);

			}

			WorkflowUpdateModel workflowUpdateModel = new WorkflowUpdateModel();
			modelMapper.map(workflowInfoRequest, workflowUpdateModel);
			workflowUpdateModel.setApplicationRegisterId(applicationRegisterId);
			workflowUpdateModel.setApplicationTypeId(request.getAdoptionCertInformation().getApplicationTypeId());
			workflowUpdateModel.setIsDraft(0);

			Map<String, Object> resultMap = adoptionRepositoryCustom.updateWorkflowDetails(workflowUpdateModel);

			// End: Added by Sayan

			// setting all values in response

			// update files
			if (request.getDeletedFiles() != null) {
				List<Integer> deletedFileIds = new ArrayList<Integer>();
				for (DeletedFileListModel item : request.getDeletedFiles()) {
					deletedFileIds.add(item.getFileId());
				}
				documentRepository.updateFileIdIsActive(deletedFileIds);
			}

			request.getAdoptionCertInformation().setApplicationRegisterId(applicationRegisterId);
			responseObj.setAdoptionCertInformation(request.getAdoptionCertInformation());
			responseObj.setUploadFileData(docEntityResponseList);
			responseObj.setLoginUserId(request.getLoginUserId());

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(responseObj);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage(CommonConstants.ERROR);
			throw new RuntimeException("Error during submitting Adoption , rolling back transaction", e);
		}
		logger.info("Exit Method " + "submitAdoptionCertificate");
		return response;
	}

	@Override
	public ServiceResponse getApplyBirthCertificateList(ApplyBirthCertificateRequest request) {
		logger.info("Entry Method " + "getApplyBirthCertificateList");
		ServiceResponse response = new ServiceResponse();
		try {

			List<BirthCertificateApplicationResponseProjection> dtoList = applicationRegisterRepository
					.getListForBirthCertificateApply(request.getCitizenId());

			if (dtoList == null || dtoList.isEmpty()) {
				logger.warn("No records found for the given input: {}", request);
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage("No records found.");
				response.setResponseObject(Collections.emptyList());
				return response;
			}

			List<BirthCertificateApplyListResponse> responseList = new ArrayList<>();
			for (BirthCertificateApplicationResponseProjection dto : dtoList) {
				BirthCertificateApplyListResponse responseObj = new BirthCertificateApplyListResponse();
				responseObj.setApplicationRegisterId(dto.getApplicationRegisterId());
				responseObj.setApplicationNo(dto.getApplicationNo());
				responseObj.setAdoptionRegistrationNumber(dto.getAdoptionRegistrationNumber());
				responseObj.setChildCitizenId(dto.getChildCitizenId());
				responseObj.setChildCivilRegistryNumber(dto.getChildCivilRegistryNumber());
				responseObj.setChildName(dto.getChildName());
				responseObj.setMotherName(dto.getMotherName());
				responseObj.setFatherName(dto.getFatherName());
				responseObj.setDateOfBirth(dto.getDateOfBirth());
				responseObj.setParishOfChild(dto.getParishOfChild());
				responseObj.setIsAdoptionCertificateApprove(dto.getIsAdoptionCertificateApprove());

				responseList.add(responseObj);
			}

			if (responseList.isEmpty()) {
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage("No records found for this.");
				response.setResponseObject(Collections.emptyList());
			} else {
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage(CommonConstants.SUCCESS);
				response.setResponseObject(responseList);
			}

		} catch (Exception e) {
			logger.error("Error occurred in getApplyBirthCertificateList: {}", e.getMessage(), e);
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}

		logger.info("Exit Method: getApplyBirthCertificateList");
		return response;
	}

	@Override
	public ServiceResponse getAdoptionCertificateDetails(GeneralInformation request) {
		logger.info("Entry Method " + " getAdoptionCertificateDetails");
		ServiceResponse response = new ServiceResponse();
		ServiceResponse respadoptionRegistration = new ServiceResponse();

		List<UploadFileData> fileDataList = new ArrayList<UploadFileData>();
		SaveAdoptionCertResponse res = new SaveAdoptionCertResponse();
		ApplicationRegisterEntity applicationRegisterEntity = new ApplicationRegisterEntity();
		ApplicationAdoptionCertificateDetailEntity application = new ApplicationAdoptionCertificateDetailEntity();
		AdoptionCertInformation adoptionCertInformation = new AdoptionCertInformation();

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		SaveARDraftResponse resp = new SaveARDraftResponse();
//		String formattedDate = formatter.format(currentDate);

		try {
			applicationRegisterEntity = applicationRegisterRepository
					.findByApplicationRegisterId(request.getApplicationRegisterId());
			application = applicationAdoptionCertRepository
					.findByApplicationRegisterId(applicationRegisterEntity.getApplicationRegisterId());
			List<AdoptionApplicationDocumentEntity> docEntityList = documentRepository
					.findByApplicationRegisterIdAndIsActive(applicationRegisterEntity.getApplicationRegisterId(), true);
			// process document entity list to model - upload file data
			for (AdoptionApplicationDocumentEntity docEntity : docEntityList) {
				UploadFileData fileData = new UploadFileData();
				fileData.setFileName(docEntity.getApplicationDocName());
				fileData.setApplicationRegisterId(docEntity.getApplicationRegisterId());
				fileData.setApplicationDocDmsId(docEntity.getApplicationDocDmsId());
				fileData.setApplicationDocId(docEntity.getApplicationDocId());
				fileData.setFileSubject(docEntity.getApplicationDocSubject());
				fileData.setDocTypeId(docEntity.getDocumentTypeId());
				fileData.setReferenceId(docEntity.getApplicationDocDmsId());
				fileData.setDocTypeCode(docEntity.getDocumentTypeCode());
				fileDataList.add(fileData);
			}

			adoptionCertInformation = modelMapper.map(application, AdoptionCertInformation.class);

//			InstituteProjection proj = birthRegisterRepository.findInstituteById(generalInformation.getInstituteId());
//			generalInformation.setInstituteName(proj.getInstituteName());

			res.setAdoptionCertInformation(adoptionCertInformation);
			res.setUploadFileData(fileDataList);

			// fetch adoption registration details from adoption certificate
			Long adoptionRegistrationId = application.getApplicationAdoptionId();
			Optional<ApplicationAdoptionDetailEntity> adoptionRegistrationOptentity = adoptionDetailRepository
					.findById(adoptionRegistrationId);

			if (adoptionRegistrationOptentity.isPresent()) {

				GeneralInformation req = new GeneralInformation();
				req.setApplicationRegisterId(adoptionRegistrationOptentity.get().getApplicationRegisterId());

				respadoptionRegistration = getAR(req);

			}

			// set marriage ;license details in marriage registration
			res.getAdoptionCertInformation().setApplicationNo(request.getApplicationNo());
			res.setAdoptionRegistrationInformation(respadoptionRegistration.getResponseObject());

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(res);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getAdoptionCertificateDetails");
		return response;
	}
	

	public MultipartFile generateAdoptionCertificate(GenerateAdoptionCertificateDTO certificateDetails, String flag) {
		logger.info("Entry Method " + "downloadMarriageCertificate");
		String dmsReferenceId = null;
		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		MultipartFile adoptionCertToUpload = null;
		try {
			
			//String flag = "CREATE";
			
			//List<GenerateAdoptionCertificateDTO> certificateDetailsList = applicationAdoptionCertRepository.fetchAdoptionCertificateDetails(request.getApplicationRegisterId());


			//GenerateAdoptionCertificateDTO certificateDetails = certificateDetailsList.get(0);

			 //System.out.println("birthCertificateDetails.getBirthRegNumber()->"+birthCertificateDetails.getBirthRegNumber());

			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			Rectangle rect = new Rectangle(577, 825, 18, 15); // you can resize rectangle
			rect.enableBorderSide(1);
			rect.enableBorderSide(2);
			rect.enableBorderSide(4);
			rect.enableBorderSide(8);
			rect.setBorderColor(BaseColor.BLACK);
			rect.setBorderWidth(1);
			document.add(rect);

			PdfContentByte cb = writer.getDirectContent();
			// Add Text to PDF file ->
			Font font = FontFactory.getFont("Arial", 14, Font.BOLD, BaseColor.BLACK);
			Font font1 = FontFactory.getFont("Arial", 10, Font.BOLD | Font.UNDERLINE | Font.ITALIC, BaseColor.BLACK);
			Font font2 = FontFactory.getFont("Arial", 10, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
			Font font3 = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
			Font font4 = FontFactory.getFont("Arial", 8, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
			Font font5 = FontFactory.getFont("Arial", 10, Font.BOLDITALIC, BaseColor.BLACK);
			Font font6 = FontFactory.getFont("Arial", 9, BaseColor.BLACK);
			Font font7 = FontFactory.getFont("Arial", 9, Font.BOLD, BaseColor.BLACK);
			Font font8 = FontFactory.getFont("Arial", 10, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);

			Font font9 = FontFactory.getFont("Arial", 12, Font.BOLD | Font.UNDERLINE, BaseColor.BLACK);
			
			Font font11 = FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.BLACK);

			Font font10 = FontFactory.getFont("Arial", 9, Font.BOLD, BaseColor.RED);

			String imageFile = "/opt/graneda-logo.png";
			String image_header = "/opt/birth-certi-header.png";
			String image_sig = "/opt/sample_sign.png";

			Phrase phrase = new Phrase();
			PdfPCell cell = new PdfPCell();
			PdfPTable table = new PdfPTable(new float[] { 70, 30 });
			table.setWidthPercentage(100);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			// cell = new PdfPCell();
			// cell.setBorder(1);
			// table.addCell(cell);

			// Write QR code here

			String barcode_str = "Country Name: Grenada" + "\n" + "Certificate Type: Adoption Certificate" + "\n"
					+ "Name: " + certificateDetails.getChild_name() + "\n" + "Adoption Certificate Number: "
					+ certificateDetails.getAdoption_certificate_number();

			if (flag != null && flag != "PREVIEW") {
				BarcodeQRCode qrcode = new BarcodeQRCode(barcode_str.trim(), 1, 1, null);
				Image qrcodeImage = qrcode.getImage();
				qrcodeImage.setAbsolutePosition(50f, 145f);
				qrcodeImage.scaleToFit(70f, 85f);
				document.add(qrcodeImage);
				// qrcodeImage.scalePercent(200);

				/*
				 * cell = new PdfPCell(); qrcodeImage.scaleToFit(100f, 115f);
				 * cell.addElement(qrcodeImage); cell.setBorder(0); table.addCell(cell);
				 */
			}

			// End QR code

			cell = new PdfPCell();
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			try {
				Image img = Image.getInstance(imageFile);
				img.scaleToFit(75f, 50f);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Vertically center the image
				cell.setPaddingLeft(220);// Add some padding to the left to move the image slightly to the right
				cell.addElement(img);
			} catch (Exception e) {
				e.printStackTrace();
			}
			table.addCell(cell);

			// Barcode39 barcode39 = new Barcode39();
			// barcode39.setCode(CommonUtils.generateRandomPassword(9));
			// Image code39Image = barcode39.createImageWithBarcode(cb, null, null);

			cell = new PdfPCell();
			cell.setBorder(0);

			if (flag != null && flag != "PREVIEW") {
				cell.setPhrase(new Phrase(
						"Adoption Certificate Number: " + certificateDetails.getAdoption_certificate_number(), font10));
			}

			table.addCell(cell);

			document.add(table);

			Paragraph para = new Paragraph("GOVERNMENT OF GRENADA", font);
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);

			para = new Paragraph("ADOPTION ORDINANCE, 1955", font9);
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);

			/*
			 * para = new Paragraph("BIRTH CERTIFICATE", font);
			 * para.setAlignment(Element.ALIGN_CENTER); document.add(para);
			 */

			// Commented: header for birth certificate
			/*
			 * try { // Image img = Image.getInstance(imageFile); Image img_header =
			 * Image.getInstance(image_header); // img.setAbsolutePosition(0, 0); //
			 * img.scaleToFit(100f, 125f); img_header.setAbsolutePosition(150, 620);
			 * img_header.scaleToFit(300f, 125f); // cell.addElement(qrcodeImage);
			 * document.add(img_header); } catch (Exception e) { e.printStackTrace(); }
			 */
			// end header

			// document.add(Chunk.NEWLINE);

			BaseColor myColor = WebColors.getRGBColor("#e3e1e1");

			//document.add(new Paragraph("\n"));
			//document.add(new Paragraph("\n"));
			
			//document.add(new Paragraph("Death Registered in the District of "+deathCertificateDetails.getDeath_registration_parish()+" in Grenada", font11));
            
			document.add(new Paragraph("\n"));
			
			PdfPTable table7 = new PdfPTable(new float[] { 45, 55 });
			table7.setWidthPercentage(100);

			// Set the border width for the top and bottom of the table
			table7.getDefaultCell().setBorderWidthTop(2f); // 2f for top border thickness
			table7.getDefaultCell().setBorderWidthBottom(2f); // 2f for bottom border thickness

			cell = new PdfPCell();
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setPhrase(new Phrase("Entry Number", font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// cell.setBackgroundColor(myColor);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setPhrase(new Phrase(": " + certificateDetails.getEntry_no(), font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// cell.setBackgroundColor(myColor);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setPhrase(new Phrase("Name of Adopted Child as stated in Adoption Order", font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setPhrase(new Phrase(": " + certificateDetails.getChild_name(), font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// .setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Gender of Adopted Child", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getChild_gender(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Name of Adopter (Father)", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getFather_name(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Address of Adopter (Father)", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getFather_address(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Occupation of Adopter (Father)", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getFather_occupation(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			//////////////
			
			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Name of Adopter (Mother)", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getMother_name(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Address of Adopter (Mother)", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getMother_address(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Occupation of Adopter (Mother)", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getMother_occupation(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);
			
			
			
			////////////////////////
			
			
			
			
			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Date of Birth", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getChild_date_of_birth(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Place of Birth", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getPlace_of_birth(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Date of Adoption Order", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getCourt_order_date(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);
			
			///////////////////////////////////////////////////////////
			
			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Entry Number in Birth Registration", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getNod_entry_no(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);
			
			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Birth Registration Number", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + "", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			
			
			
			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Registration Number", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + certificateDetails.getAdoption_registration_number(), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("Date of Issuance of Certificate", font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase(": " + (certificateDetails.getAdoption_certificate_approval_date() == null ? ""
					: certificateDetails.getAdoption_certificate_approval_date()), font3));
			cell.setBorder(0);
			// cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM | PdfPCell.LEFT |
			// PdfPCell.RIGHT);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			// cell.setPaddingLeft(5);
			cell.setFixedHeight(25);
			// cell.setBackgroundColor(myColor);
			table7.addCell(cell);

			document.add(table7);

			// document.add(new Paragraph("\n"));
			
			/*PdfPTable table9 = new PdfPTable(new float[] { 100 });
			table9.setWidthPercentage(100);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("", font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			table9.addCell(cell);

			cell = new PdfPCell();
			cell.setPhrase(new Phrase("", font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			table9.addCell(cell);
			
			cell = new PdfPCell();
			
			cell.setPhrase(new Phrase("Married at "+marriageCertificateDetails.getName_of_marriage_place()+
					  " by (or before) me "+marriageCertificateDetails.getRegistrar_name()+" a Marriage Officer at the Parish of "+marriageCertificateDetails.getParish_of_marriage(), font3));

			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			table9.addCell(cell);
			
            cell = new PdfPCell();
			
			cell.setPhrase(new Phrase("This marriage was celebrated between us "+marriageCertificateDetails.getBride_name()+
					  " and "+marriageCertificateDetails.getGroom_name()+" in the presenece of "+marriageCertificateDetails.getFirst_witness_name()+
					  " and "+marriageCertificateDetails.getSecond_witness_name(), font3));

			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			table9.addCell(cell);

			document.add(new Paragraph("\n"));
			
			document.add(table9);*/
			

			PdfPTable table8 = new PdfPTable(new float[] { 100 });
			table8.setWidthPercentage(100);

			/*cell = new PdfPCell();
			cell.setPhrase(new Phrase("", font3));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			table8.addCell(cell);*/


			cell = new PdfPCell();

			Integer approvedYear = LocalDate.now().getYear();

			if (flag != null && flag != "PREVIEW") {
				cell.setPhrase(new Phrase("I, " + certificateDetails.getApproved_by_user() + ", "
						+ certificateDetails.getApproved_by_role()
						+ " of General Births and Deaths for the states of Grenada certify that the above "
						+ "is true extract from the Adopted Children Register in the said state for the year "+approvedYear, font3));

				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorder(0);
				table8.addCell(cell);

				document.add(new Paragraph("\n"));

				document.add(table8);

				document.add(new Paragraph("\n"));
				
				  //document.add(new Paragraph("\n")); 
				  //document.add(new Paragraph("\n"));
				  document.add(new Paragraph("\n"));
				  
				  
				  if (flag != null && flag != "PREVIEW") { 

				PdfPTable table6 = new PdfPTable(new float[] { 70, 50 });
				table6.setWidthPercentage(100);

				cell = new PdfPCell();
				cell.setPhrase(new Phrase("", font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorder(0);
				table6.addCell(cell);

				cell = new PdfPCell();
				cell.setBorder(1);
				phrase = new Phrase();
				phrase.add(new Chunk("ISSUING AUTHORITY", font3));
				cell.addElement(phrase);
				phrase = new Phrase();
				phrase.add(new Chunk((certificateDetails.getApproved_by_user() == null ? ""
						: certificateDetails.getApproved_by_user()), font3));
				cell.addElement(phrase);
				phrase = new Phrase();
				phrase.add(new Chunk(certificateDetails.getApproved_by_role() == null ? ""
						: certificateDetails.getApproved_by_role(), font5));
				cell.addElement(phrase);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBorder(0);
				table6.addCell(cell);

				document.add(table6);
				  }
			}

			/*
			 * try { // Image img = Image.getInstance(imageFile); Image img_sign =
			 * Image.getInstance(image_sig); // img.setAbsolutePosition(0, 0); //
			 * img.scaleToFit(100f, 125f); img_sign.setAbsolutePosition(300, 100);
			 * img_sign.scaleToFit(300f, 125f); // cell.addElement(qrcodeImage);
			 * document.add(img_sign); } catch (Exception e) { e.printStackTrace(); }
			 */

			document.close();

			byte[] byteArray = out.toByteArray();

			adoptionCertToUpload = new ByteArrayMultipartFile(byteArray,
					"Adoption_Certificate_" + ".pdf",
					"application/pdf");
			/*
			 * MultipartFile[] fileToUpload = new MultipartFile[1]; fileToUpload[0] =
			 * birthCertToUpload; NewFileUploadResponse fileUploadResponse =
			 * dmsService.uploadFileToAlfresco(fileToUpload); dmsReferenceId =
			 * fileUploadResponse.getFileUploadResponse().get(0);
			 */
			// Convert the byte array to a Base64 encoded string
			// Base64.getEncoder().encodeToString(byteArray);

			System.out.println("dmsReferenceId-->" + dmsReferenceId);

		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		logger.info("Exit Method " + "generateMarriageCertificate");
		return adoptionCertToUpload;
		//return new ByteArrayInputStream(out.toByteArray());
	}


}
