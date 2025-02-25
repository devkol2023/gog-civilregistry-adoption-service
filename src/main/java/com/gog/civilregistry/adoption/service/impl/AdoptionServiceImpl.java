package com.gog.civilregistry.adoption.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gog.civilregistry.adoption.service.DMSService;
import com.gog.civilregistry.adoption.service.IntegrationApiService;
import com.gog.civilregistry.adoption.service.AdoptionService;
import com.gog.civilregistry.adoption.util.CommonConstants;
import com.gog.civilregistry.adoption.model.ApplicationTrackStatus;
import com.gog.civilregistry.adoption.model.ApplicationTrackStatusResponse;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;
import com.gog.civilregistry.adoption.repository.ApplicationAdoptionDetailRepository;
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

	
	
	
}
