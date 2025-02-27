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
import com.gog.civilregistry.adoption.model.ApprovedAdoptionRegistrationDetails;
import com.gog.civilregistry.adoption.model.ProcessApplicationModel;
import com.gog.civilregistry.adoption.model.SearchApplicationACDto;
import com.gog.civilregistry.adoption.model.SearchApplicationARDto;
import com.gog.civilregistry.adoption.model.TrackAppUserDto;
import com.gog.civilregistry.adoption.model.UpdateCertificateFileRequest;
import com.gog.civilregistry.adoption.model.VaultResponse;
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
	
	public List<SearchApplicationARDto> searchApplicationAR(String childName, String motherName, String fatherName, 
            Date dateOfBirth, String applicationNumber, Integer parishId, Integer genderId) {

		StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT tar.application_register_id AS applicationRegisterId, ")
                .append("tar.application_no AS applicationNumber, ")
                .append("concat_ws(', ', taad.child_surname, concat_ws(' ', taad.child_first_name, taad.child_middle_name)) AS childName, ")
                .append("concat_ws(', ', taad.mother_surname, concat_ws(' ', taad.mother_first_name, taad.mother_middle_name)) AS motherName, ")
                .append("concat_ws(', ', taad.father_surname, concat_ws(' ', taad.father_first_name, taad.father_middle_name)) AS fatherName, ")
                .append("TO_CHAR(taad.child_date_of_birth, 'DD/MM/YYYY') AS dateOfBirth, ")
                .append("mg.geography_name AS parishOfBirth, ")
                .append("mdv.data_description AS gender ")
                .append("FROM applications.t_application_register tar ")
                .append("INNER JOIN adoption.t_application_adoption_details taad ON tar.application_register_id = taad.application_register_id ")
                .append("LEFT JOIN masters.m_geography mg ON mg.geography_id = taad.child_parish ")
                .append("LEFT JOIN masters.m_master_data_value mdv ON mdv.master_data_value_id = taad.child_gender ")
                .append("WHERE 1=1 ");

        
        if (applicationNumber != null && !applicationNumber.trim().isEmpty()) {
            sqlQuery.append(" AND tar.application_no = :applicationNumber ");
        }
        if (childName != null && !childName.trim().isEmpty()) {
            sqlQuery.append(" AND LOWER(taad.child_first_name) = LOWER(:childName) ");
        }
        if (motherName != null && !motherName.trim().isEmpty()) {
            sqlQuery.append(" AND LOWER(taad.mother_first_name) = LOWER(:motherName) ");
        }
        if (fatherName != null && !fatherName.trim().isEmpty()) {
            sqlQuery.append(" AND LOWER(taad.father_first_name) = LOWER(:fatherName) ");
        }
        if (dateOfBirth != null) {
            sqlQuery.append(" AND taad.child_date_of_birth = :dateOfBirth ");
        }
        if (parishId != null && parishId != 0) {
            sqlQuery.append(" AND taad.child_parish = :parishId ");
        }
        if (genderId != null && genderId != 0) {
            sqlQuery.append(" AND taad.child_gender = :genderId ");
        }

        Query query = entityManager.createNativeQuery(sqlQuery.toString());

        // Set query parameters dynamically
        if (applicationNumber != null && !applicationNumber.trim().isEmpty()) {
            query.setParameter("applicationNumber", applicationNumber);
        }
        if (childName != null && !childName.trim().isEmpty()) {
            query.setParameter("childName", childName.trim());
        }
        if (motherName != null && !motherName.trim().isEmpty()) {
            query.setParameter("motherName", motherName.trim());
        }
        if (fatherName != null && !fatherName.trim().isEmpty()) {
            query.setParameter("fatherName", fatherName.trim());
        }
        if (dateOfBirth != null) {
            query.setParameter("dateOfBirth", dateOfBirth);
        }
        if (parishId != null && parishId != 0) {
            query.setParameter("parishId", parishId);
        }
        if (genderId != null && genderId != 0) {
            query.setParameter("genderId", genderId);
        }

        List<Object[]> resultList = query.getResultList();

        // Map results to DTO
        return resultList.stream().map(result -> {
            Object[] row = (Object[]) result;
            SearchApplicationARDto dto = new SearchApplicationARDto();
            dto.setApplicationRegisterId(row[0] != null ? (Long) row[0] : 0l);
            dto.setApplicationNumber(row[1] != null ? (String) row[1] : "");
            dto.setChildName(row[2] != null ? (String) row[2] : "");
            dto.setMotherName(row[3] != null ? (String) row[3] : "");
            dto.setFatherName(row[4] != null ? (String) row[4] : "");
            dto.setDateOfBirth(row[5] != null ? (String) row[5] : "");
            dto.setParishOfBirth(row[6] != null ? (String) row[6] : "");
            dto.setGender(row[7] != null ? (String) row[7] : "");
            return dto;
        }).collect(Collectors.toList());
    }
	
	
	public List<TrackAppUserDto> trackApplicationUserList(Integer loggedInUserId) {
	    StringBuilder sqlQuery = new StringBuilder();

	    sqlQuery.append("SELECT DISTINCT ")
	        .append("tar.application_no AS applicationNo, ")

	        .append("CASE ")
	        .append("    WHEN mat.application_type_code = 'AR' THEN ")
	        .append("        concat_ws(', ', taad.child_surname, concat_ws(' ', taad.child_first_name, taad.child_middle_name)) ")
	        .append("    WHEN mat.application_type_code = 'AC' THEN ")
	        .append("        concat_ws(', ', taad_ac.child_surname, concat_ws(' ', taad_ac.child_first_name, taad_ac.child_middle_name)) ")
	        .append("END AS childName, ")

	        .append("CASE ")
	        .append("    WHEN mat.application_type_code = 'AR' THEN ")
	        .append("        concat_ws(', ', taad.mother_surname, concat_ws(' ', taad.mother_first_name, taad.mother_middle_name)) ")
	        .append("    WHEN mat.application_type_code = 'AC' THEN ")
	        .append("        concat_ws(', ', taad_ac.mother_surname, concat_ws(' ', taad_ac.mother_first_name, taad_ac.mother_middle_name)) ")
	        .append("END AS motherName, ")

	        .append("CASE ")
	        .append("    WHEN mat.application_type_code = 'AR' THEN ")
	        .append("        concat_ws(', ', taad.father_surname, concat_ws(' ', taad.father_first_name, taad.father_middle_name)) ")
	        .append("    WHEN mat.application_type_code = 'AC' THEN ")
	        .append("        concat_ws(', ', taad_ac.father_surname, concat_ws(' ', taad_ac.father_first_name, taad_ac.father_middle_name)) ")
	        .append("END AS fatherName, ")

	        .append("CASE ")
	        .append("    WHEN mat.application_type_code = 'AR' THEN TO_CHAR(taad.child_date_of_birth, 'DD/MM/YYYY') ")
	        .append("    WHEN mat.application_type_code = 'AC' THEN TO_CHAR(taad_ac.child_date_of_birth, 'DD/MM/YYYY') ")
	        .append("END AS dateOfBirth, ")

	        .append("mws.display_stage_name AS status, ")
	        .append("mat.application_type_name AS applicationType ")

	        .append("FROM applications.t_application_workflow taw ")
	        .append("INNER JOIN applications.t_application_register tar ")
	        .append("ON taw.application_register_id = tar.application_register_id ")
	        .append("INNER JOIN masters.m_application_type mat ")
	        .append("ON tar.application_type_id = mat.application_type_id ")
	        .append("INNER JOIN masters.m_workflow_stage mws ")
	        .append("ON taw.stage_id = mws.stage_id ")

	        // Join for application type 'AR'
	        .append("LEFT JOIN adoption.t_application_adoption_details taad ")
	        .append("ON tar.application_register_id = taad.application_register_id ")
	        .append("AND mat.application_type_code = 'AR' ")

	        // Join for application type 'AC'
	        .append("LEFT JOIN adoption.t_application_adoption_certificate_details taac ")
	        .append("ON tar.application_register_id = taac.application_register_id ")
	        .append("AND taac.is_active = true ")
	        .append("AND mat.application_type_code = 'AC' ")

	        .append("LEFT JOIN adoption.t_application_adoption_details taad_ac ")
	        .append("ON taac.application_adoption_id = taad_ac.application_adoption_id ")
	        .append("AND taad_ac.is_active = true ")
	        .append("AND mat.application_type_code = 'AC' ")

	        .append("WHERE (taw.assigned_from_user = :loggedInUserId OR taw.assigned_to_user = :loggedInUserId) ")
	        .append("AND taw.is_active = true ")
	        .append("AND tar.is_active = true ")
	        .append("AND mws.is_active = true ")
	        .append("AND mat.module_code = 'ADOPTION' ")
	        .append("AND mat.application_type_code IN ('AR', 'AC') ")
	        .append("AND taw.stage_id <> 1; ");

	    Query query = entityManager.createNativeQuery(sqlQuery.toString());

	    query.setParameter("loggedInUserId", loggedInUserId);

	    List<Object[]> resultList = query.getResultList();

	    List<TrackAppUserDto> trackApplicationUserList = resultList.stream().map(result -> {
	        Object[] row = (Object[]) result;
	        TrackAppUserDto dto = new TrackAppUserDto();
	        dto.setApplicationNo(row[0] != null ? (String) row[0] : "");
	        dto.setChildName(row[1] != null ? (String) row[1] : "");
	        dto.setMotherName(row[2] != null ? (String) row[2] : "");
	        dto.setFatherName(row[3] != null ? (String) row[3] : "");
	        dto.setDateOfBirth(row[4] != null ? (String) row[4] : "");
	        dto.setStatus(row[5] != null ? (String) row[5] : "");
	        dto.setApplicationType(row[6] != null ? (String) row[6] : "");
	        return dto;
	    }).collect(Collectors.toList());

	    return trackApplicationUserList;
	}


	public List<SearchApplicationACDto> searchApplicationAC(
            String childName, String motherName, String fatherName, Date dateOfBirth, 
            String applicationNumber, Integer parishId, Integer genderId) {

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT tar.application_register_id, tar.application_no, ")
                .append("CONCAT_WS(' ', taad.child_first_name, taad.child_middle_name, taad.child_surname) AS child_full_name, ")
                .append("CONCAT_WS(' ', taad.mother_first_name, taad.mother_middle_name, taad.mother_surname) AS mother_full_name, ")
                .append("CONCAT_WS(' ', taad.father_first_name, taad.father_middle_name, taad.father_surname) AS father_full_name, ")
                .append("mg.geography_name AS parish_of_birth, ")
                .append("TO_CHAR(taad.child_date_of_birth, 'DD/MM/YYYY') AS date_of_birth, ")
                .append("mdv.data_description AS gender ")
                .append("FROM applications.t_application_register tar ")
                .append("INNER JOIN adoption.t_application_adoption_certificate_details taacd ")
                .append("ON tar.application_register_id = taacd.application_register_id ")
                .append("INNER JOIN adoption.t_application_adoption_details taad ")
                .append("ON taacd.application_adoption_id = taad.application_adoption_id ")
                .append("LEFT JOIN masters.m_geography mg ")
                .append("ON taad.child_parish = mg.geography_id ")
                .append("LEFT JOIN masters.m_master_data_value mdv ON mdv.master_data_value_id = taad.child_gender ")
                .append("WHERE 1=1 ");

        // Applying filters dynamically
        if (applicationNumber != null && !applicationNumber.trim().isEmpty()) {
            sqlQuery.append(" AND tar.application_no = :applicationNumber");
        }
        if (childName != null && !childName.trim().isEmpty()) {
            sqlQuery.append(" AND LOWER(taad.child_first_name) = LOWER(:childName)");
        }
        if (motherName != null && !motherName.trim().isEmpty()) {
            sqlQuery.append(" AND LOWER(taad.mother_first_name) = LOWER(:motherName)");
        }
        if (fatherName != null && !fatherName.trim().isEmpty()) {
            sqlQuery.append(" AND LOWER(taad.father_first_name) = LOWER(:fatherName)");
        }
        if (dateOfBirth != null) {
            sqlQuery.append(" AND taad.child_date_of_birth = :dateOfBirth");
        }
        if (parishId != null && parishId != 0) {
            sqlQuery.append(" AND taad.child_parish = :parishId");
        }
        if (genderId != null && genderId != 0) {
            sqlQuery.append(" AND taad.child_gender = :genderId");
        }

        Query query = entityManager.createNativeQuery(sqlQuery.toString());

        // Setting Query Parameters Dynamically
        if (applicationNumber != null && !applicationNumber.trim().isEmpty()) {
            query.setParameter("applicationNumber", applicationNumber);
        }
        if (childName != null && !childName.trim().isEmpty()) {
            query.setParameter("childName", childName.trim());
        }
        if (motherName != null && !motherName.trim().isEmpty()) {
            query.setParameter("motherName", motherName.trim());
        }
        if (fatherName != null && !fatherName.trim().isEmpty()) {
            query.setParameter("fatherName", fatherName.trim());
        }
        if (dateOfBirth != null) {
            query.setParameter("dateOfBirth", dateOfBirth);
        }
        if (parishId != null && parishId != 0) {
            query.setParameter("parishId", parishId);
        }
        if (genderId != null && genderId != 0) {
            query.setParameter("genderId", genderId);
        }

        List<Object[]> resultList = query.getResultList();

        // Mapping results to DTO
        return resultList.stream().map(result -> {
            Object[] row = (Object[]) result;
            SearchApplicationACDto dto = new SearchApplicationACDto();
            dto.setApplicationRegisterId(row[0] != null ? ((Number) row[0]).longValue() : 0L);
            dto.setApplicationNumber(row[1] != null ? (String) row[1] : "");
            dto.setChildName(row[2] != null ? (String) row[2] : "");
            dto.setMotherName(row[3] != null ? (String) row[3] : "");
            dto.setFatherName(row[4] != null ? (String) row[4] : "");
            dto.setParishOfBirth(row[5] != null ? (String) row[5] : "");
            dto.setDateOfBirth(row[6] != null ? (String) row[6] : "");
            dto.setGender(row[7] != null ? (String) row[7] : "");
            return dto;
        }).collect(Collectors.toList());
    
}
	
	public List<ApprovedAdoptionRegistrationDetails> approveAdoptionRegistration(WorkflowUpdateModel workflowUpdateModel) {
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("adoption.sp_application_approve_ar");

		storedProcedure.registerStoredProcedureParameter("p_application_register_id", Long.class, ParameterMode.IN);
		storedProcedure.setParameter("p_application_register_id", workflowUpdateModel.getApplicationRegisterId());

		storedProcedure.registerStoredProcedureParameter("p_current_user_id", Integer.class, ParameterMode.IN);
		storedProcedure.setParameter("p_current_user_id", workflowUpdateModel.getAssignedByUserId());

		storedProcedure.registerStoredProcedureParameter("re_code", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_code", "0");

		storedProcedure.registerStoredProcedureParameter("re_msg", String.class, ParameterMode.OUT);
		storedProcedure.setParameter("re_msg", "resmessage");

		// Execute the stored procedure
		storedProcedure.execute();

		// Get the output parameters
		String reCode = (String) storedProcedure.getOutputParameterValue("re_code");
		String jsonResponse = (String) storedProcedure.getOutputParameterValue("re_msg");

		List<ApprovedAdoptionRegistrationDetails> approvedDetails = null;

		try {
			// Use ObjectMapper to convert the JSON string into a list of
			// ApprovedBCDetails objects
			approvedDetails = objectMapper.readValue(jsonResponse,
					objectMapper.getTypeFactory().constructCollectionType(List.class, ApprovedAdoptionRegistrationDetails.class));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return approvedDetails;

	}


}