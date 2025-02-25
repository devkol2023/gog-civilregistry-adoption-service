package com.gog.civilregistry.adoption.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gog.civilregistry.adoption.dto.GetWfNextStageDTO;
import com.gog.civilregistry.adoption.dto.GetWfNextUserDTO;
import com.gog.civilregistry.adoption.model.ClaimApplicationWfRequest;
import com.gog.civilregistry.adoption.model.GetWfNextStageRequest;
import com.gog.civilregistry.adoption.model.GetWfNextUserRequest;
import com.gog.civilregistry.adoption.model.ReleaseApplicationWfRequest;
import com.gog.civilregistry.adoption.model.common.ServiceResponse;
import com.gog.civilregistry.adoption.repository.ApplicationAdoptionDetailRepository;
import com.gog.civilregistry.adoption.repository.custom.WorkflowRepositoryCustom;
import com.gog.civilregistry.adoption.service.WorkflowService;
import com.gog.civilregistry.adoption.util.CommonConstants;

@Service
public class WorkflowServiceImpl implements WorkflowService{
	
	private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);
	
	
	@Autowired
	private WorkflowRepositoryCustom workflowRepositoryCustom;
	
	@Autowired
	private ApplicationAdoptionDetailRepository applicationDeathRegRepository;

	@Override
	public ServiceResponse getWfNextStage(GetWfNextStageRequest request) {
		logger.info("Entry Method " + " getWfNextStage");
		ServiceResponse response = new ServiceResponse();
		try {

			List<GetWfNextStageDTO> getWfNextStageDTOList = new ArrayList<GetWfNextStageDTO>();
			
			List<Object[]> result = applicationDeathRegRepository.callWorkflowFunction(request.getApplicationRegisterId(), request.getApplicationTypeId(), request.getCurrentStageId(), request.getCurrentRoleCode(), request.getNextInstituteTypeId(), request.getNextRoleCode());
            
			for (Object[] row : result) {
				
				GetWfNextStageDTO getWfNextStageDTO = new GetWfNextStageDTO();
				
				getWfNextStageDTO.setNextInstituteTypeId((Integer) row[0]);
				getWfNextStageDTO.setNextInstituteTypeCode((String) row[1]);
				getWfNextStageDTO.setWorkflowId((Integer) row[2]);
				getWfNextStageDTO.setStageId((Short) row[3]);
				getWfNextStageDTO.setInternalStageName((String) row[4]);
				getWfNextStageDTO.setActionId((Short) row[5]);
				getWfNextStageDTO.setActionName((String) row[6]);
				getWfNextStageDTO.setNextRoleCode((String) row[7]);
				getWfNextStageDTO.setFlowType((String) row[8]);
				getWfNextStageDTO.setIsUserVisible((Short) row[9]);
				getWfNextStageDTO.setNextRoleId((Integer) row[10]);
				getWfNextStageDTO.setNextRoleName((String) row[11]);
				
				getWfNextStageDTOList.add(getWfNextStageDTO);

			}

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(getWfNextStageDTOList);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getWfNextStage");
		return response;
	}

	@Override
	public ServiceResponse getWfNextUser(GetWfNextUserRequest request) {
		logger.info("Entry Method " + " getWfNextUser");
		ServiceResponse response = new ServiceResponse();
		try {

			List<GetWfNextUserDTO> getWfNextUserDTOList = new ArrayList<GetWfNextUserDTO>();
			
			Long applicationRegisterId = request.getApplicationRegisterId();
			Integer currentInstituteId = request.getCurrentInstituteId();
			Integer currentRoleId = request.getCurrentRoleId();
			Integer currentUserId = request.getCurrentUserId();
			Integer currentParishId = request.getCurrentParishId();
			Integer nextRoleId = request.getNextRoleId();
			Integer nextWorkflowId = request.getNextWorkflowId();
			Short isUserVisible = request.getIsUserVisible();
			Integer nextInstituteTypeId = request.getNextInstituteTypeId();
			
			
			List<Object[]> result = applicationDeathRegRepository.getWfNextUser(applicationRegisterId,currentInstituteId,currentRoleId,currentUserId,currentParishId,nextRoleId,nextWorkflowId,isUserVisible,nextInstituteTypeId);
            
			for (Object[] row : result) {
				
				GetWfNextUserDTO getWfNextUserDTO = new GetWfNextUserDTO();
				
				getWfNextUserDTO.setNextUserName((String) row[0]);
				getWfNextUserDTO.setNextUserId((Long) row[1]);
				getWfNextUserDTO.setNextInstituteId((Integer) row[2]);
			
				
				getWfNextUserDTOList.add(getWfNextUserDTO);

			}

			response.setStatus(CommonConstants.SUCCESS_STATUS);
			response.setMessage(CommonConstants.SUCCESS_MSG);
			response.setResponseObject(getWfNextUserDTOList);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
		}
		logger.info("Exit Method " + " getWfNextUser");
		return response;
	}
	
	@Override
	@Transactional
	public ServiceResponse claimApplicationWorkflow(ClaimApplicationWfRequest request) {
		logger.info("Entry Method " + " claimApplicationWorkflow");
		ServiceResponse response = new ServiceResponse();
		Integer nextStageId = null;
		Short actionId = null;
		Integer workflowId = null;
		try {
			
			GetWfNextStageRequest getWfNextStageRequest = new GetWfNextStageRequest();
			getWfNextStageRequest.setApplicationRegisterId(request.getApplicationRegisterId());
			getWfNextStageRequest.setApplicationTypeId(null);
			getWfNextStageRequest.setCurrentRoleCode(request.getCurrentRoleCode());
			getWfNextStageRequest.setCurrentStageId(request.getCurrentStageId());
			
			
			ServiceResponse wfNextStageResponse = getWfNextStage(getWfNextStageRequest);
			List<GetWfNextStageDTO> getWfNextStageDTOList = (List<GetWfNextStageDTO>) wfNextStageResponse.getResponseObject();
            
			if(getWfNextStageDTOList != null && getWfNextStageDTOList.size()>0) {
				
				nextStageId = Integer.valueOf(getWfNextStageDTOList.get(0).getStageId().intValue());
				actionId = getWfNextStageDTOList.get(0).getActionId();
				workflowId = getWfNextStageDTOList.get(0).getWorkflowId();
			}
			request.setActionId(actionId);
			request.setWorkflowId(workflowId);
			request.setNextStageId(nextStageId);
			
			Map<String, Object> resultMap = workflowRepositoryCustom.claimApplicationWorkflow(request);

			Integer retcode = (Integer) resultMap.get("re_code");
			String retmsg = (String) resultMap.get("re_msg");

			if (retcode != null && retcode!=0) {
				response.setStatus(CommonConstants.ERROR_MSG);
				response.setMessage(CommonConstants.ERROR_STATUS);
				response.setResponseObject(retmsg);
			} else {
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage(CommonConstants.SUCCESS_MSG);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
			throw new RuntimeException("Error during saving NOD draft, rolling back transaction", e);
		}
		logger.info("Exit Method " + " claimApplicationWorkflow");
		return response;
	}

	@Override
	public ServiceResponse releaseApplicationWorkflow(ReleaseApplicationWfRequest request) {
		logger.info("Entry Method " + " releaseApplicationWorkflow");
		ServiceResponse response = new ServiceResponse();
		try {

			Map<String, Object> resultMap = workflowRepositoryCustom.releaseApplicationWorkflow(request);

			Integer retcode = (Integer) resultMap.get("re_code");
			String retmsg = (String) resultMap.get("re_msg");

			if (retcode != null && retcode!=0) {
				response.setStatus(CommonConstants.ERROR_MSG);
				response.setMessage(CommonConstants.ERROR_STATUS);
				response.setResponseObject(retmsg);
			} else {
				response.setStatus(CommonConstants.SUCCESS_STATUS);
				response.setMessage(CommonConstants.SUCCESS_MSG);
			}


		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(CommonConstants.ERROR_STATUS);
			response.setMessage("An error occurred while processing the request.");
			throw new RuntimeException("Error during saving NOD draft, rolling back transaction", e);
		}
		logger.info("Exit Method " + " releaseApplicationWorkflow");
		return response;
	}

}
