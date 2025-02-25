package com.gog.civilregistry.adoption.repository.custom;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gog.civilregistry.adoption.model.ProcessApplicationModel;
import com.gog.civilregistry.adoption.model.UpdateCertificateFileRequest;
import com.gog.civilregistry.adoption.model.WorkflowUpdateModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;

@Component
public class AdoptionRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ObjectMapper objectMapper;

	public Map<String, Object> updateApplicationProcess(ProcessApplicationModel processNod) {
		// Create stored procedure query for `get_user_count_and_details`
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("birth.sp_process_nod_gog");

		storedProcedure.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN);
		storedProcedure.setParameter("p_flag", processNod.getFlag());

		storedProcedure.registerStoredProcedureParameter("p_application_register_id", Long.class, ParameterMode.INOUT);
		storedProcedure.setParameter("p_application_register_id", processNod.getApplicationRegisterId());

		storedProcedure.registerStoredProcedureParameter("p_institute_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_institute_id", processNod.getInstituteId());

		storedProcedure.registerStoredProcedureParameter("p_role_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_role_id", processNod.getRoleId());

		storedProcedure.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_user_id", processNod.getUserId());

		storedProcedure.registerStoredProcedureParameter("p_application_type_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_application_type_id", processNod.getApplicationTypeId());

		storedProcedure.registerStoredProcedureParameter("p_parish_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_parish_id", processNod.getParishId());

		storedProcedure.registerStoredProcedureParameter("p_citizen_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_citizen_id", processNod.getCitizenId());

		storedProcedure.registerStoredProcedureParameter("p_application_no", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("p_application_no", null);

		storedProcedure.registerStoredProcedureParameter("re_code", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_code", 0);

		storedProcedure.registerStoredProcedureParameter("re_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_msg", "resmessage");

		// Register the output parameters (user_count, user_names)
		// storedProcedure.registerStoredProcedureParameter("user_count", Integer.class,
		// ParameterMode.OUT);
		// storedProcedure.registerStoredProcedureParameter("user_names", String.class,
		// ParameterMode.OUT);

		// Execute the stored procedure
		storedProcedure.execute();

		// Get the output parameters
		String reCode = (String) storedProcedure.getOutputParameterValue("re_code");
		String reMsg = (String) storedProcedure.getOutputParameterValue("re_msg");
		String applicationNumber = (String) storedProcedure.getOutputParameterValue("p_application_no");
		Long applicationRegisterId = (Long) storedProcedure.getOutputParameterValue("p_application_register_id");

		Map<String, Object> response = new HashMap<>();
		response.put("re_code", reCode);
		response.put("re_msg", reMsg);
		response.put("applicationNumber", applicationNumber);
		response.put("applicationRegisterId", applicationRegisterId);

		return response;
	}

	public Map<String, Object> createNewApplication(ProcessApplicationModel processNod) {
		// Create stored procedure query for `get_user_count_and_details`
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("applications.sp_create_new_application");

		storedProcedure.registerStoredProcedureParameter("p_application_type_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_application_type_id", processNod.getApplicationTypeId());

		storedProcedure.registerStoredProcedureParameter("p_status_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_status_id", processNod.getStatusId());

		storedProcedure.registerStoredProcedureParameter("p_parish_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_parish_id", processNod.getParishId());

		storedProcedure.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_user_id", processNod.getUserId());

		storedProcedure.registerStoredProcedureParameter("p_citizen_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_citizen_id", processNod.getCitizenId());

		storedProcedure.registerStoredProcedureParameter("p_application_register_id", Long.class, ParameterMode.OUT);
		storedProcedure.setParameter("p_application_register_id", null);

		storedProcedure.registerStoredProcedureParameter("p_application_no", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("p_application_no", null);

		// Execute the stored procedure
		storedProcedure.execute();

		// Get the output parameters

		String applicationNumber = (String) storedProcedure.getOutputParameterValue("p_application_no");
		Long applicationRegisterId = (Long) storedProcedure.getOutputParameterValue("p_application_register_id");

		Map<String, Object> response = new HashMap<>();
		// response.put("re_code", reCode);
		// response.put("re_msg", reMsg);
		response.put("applicationNumber", applicationNumber);
		response.put("applicationRegisterId", applicationRegisterId);

		return response;
	}

	public Map<String, Object> updateWorkflowDetails(WorkflowUpdateModel workflowUpdateModel) {

		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("birth.sp_update_workflow_dtls");

		storedProcedure.registerStoredProcedureParameter("p_application_register_id", Long.class, ParameterMode.IN);
		storedProcedure.setParameter("p_application_register_id", workflowUpdateModel.getApplicationRegisterId());

		storedProcedure.registerStoredProcedureParameter("p_application_type_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_application_type_id", workflowUpdateModel.getApplicationTypeId());

		storedProcedure.registerStoredProcedureParameter("p_is_draft", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_is_draft", workflowUpdateModel.getIsDraft());

		storedProcedure.registerStoredProcedureParameter("p_action_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_action_id", workflowUpdateModel.getActionId());

		storedProcedure.registerStoredProcedureParameter("p_assigned_by_institute_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_assigned_by_institute_id", workflowUpdateModel.getAssignedByInstituteId());

		storedProcedure.registerStoredProcedureParameter("p_assigned_by_role_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_assigned_by_role_id", workflowUpdateModel.getAssignedByRoleId());

		storedProcedure.registerStoredProcedureParameter("p_assigned_by_user_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_assigned_by_user_id", workflowUpdateModel.getAssignedByUserId());

		storedProcedure.registerStoredProcedureParameter("p_assigned_to_institute_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_assigned_to_institute_id", workflowUpdateModel.getAssignedToInstituteId());

		storedProcedure.registerStoredProcedureParameter("p_assigned_to_role_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_assigned_to_role_id", workflowUpdateModel.getAssignedToRoleId());

		storedProcedure.registerStoredProcedureParameter("p_assigned_to_user_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_assigned_to_user_id", workflowUpdateModel.getAssignedToUserId());

		storedProcedure.registerStoredProcedureParameter("p_current_status_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_current_status_id", workflowUpdateModel.getCurrentStatusId());

		storedProcedure.registerStoredProcedureParameter("p_next_status_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_next_status_id", workflowUpdateModel.getNextStatusId());

		storedProcedure.registerStoredProcedureParameter("p_workflow_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_workflow_id", workflowUpdateModel.getWorkflowId());

		storedProcedure.registerStoredProcedureParameter("p_comments", String.class, ParameterMode.IN);
		storedProcedure.setParameter("p_comments", workflowUpdateModel.getComments());

		storedProcedure.registerStoredProcedureParameter("re_code", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_code", "0");

		storedProcedure.registerStoredProcedureParameter("re_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_msg", "resmessage");

		// Execute the stored procedure
		storedProcedure.execute();

		// Get the output parameters
		String reCode = (String) storedProcedure.getOutputParameterValue("re_code");
		String reMsg = (String) storedProcedure.getOutputParameterValue("re_msg");

		Map<String, Object> response = new HashMap<>();
		response.put("re_code", reCode);
		response.put("re_msg", reMsg);

		return response;
	}
	
	public Map<String, Object> updateCertificateFile(UpdateCertificateFileRequest UpdateCertificateFileRequest) {

		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("applications.sp_application_update_certificate_file");

		storedProcedure.registerStoredProcedureParameter("p_citizen_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_citizen_id", UpdateCertificateFileRequest.getCitizenId());

		storedProcedure.registerStoredProcedureParameter("p_current_user_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_current_user_id", UpdateCertificateFileRequest.getCurrentUserId());

		storedProcedure.registerStoredProcedureParameter("p_dms_id", String.class, ParameterMode.IN);
		storedProcedure.setParameter("p_dms_id", UpdateCertificateFileRequest.getDmsRefId());

		storedProcedure.registerStoredProcedureParameter("p_application_type_code", String.class, ParameterMode.IN);
		storedProcedure.setParameter("p_application_type_code", UpdateCertificateFileRequest.getApplicationTypeCode());

		storedProcedure.registerStoredProcedureParameter("re_code", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_code", "0");

		storedProcedure.registerStoredProcedureParameter("re_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_msg", "resmessage");

		// Execute the stored procedure
		storedProcedure.execute();

		// Get the output parameters
		String reCode = (String) storedProcedure.getOutputParameterValue("re_code");
		String reMsg = (String) storedProcedure.getOutputParameterValue("re_msg");

		Map<String, Object> response = new HashMap<>();
		response.put("re_code", reCode);
		response.put("re_msg", reMsg);

		return response;
	}



}