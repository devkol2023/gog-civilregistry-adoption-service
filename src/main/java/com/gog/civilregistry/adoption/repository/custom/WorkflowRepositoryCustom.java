package com.gog.civilregistry.adoption.repository.custom;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gog.civilregistry.adoption.model.ClaimApplicationWfRequest;
import com.gog.civilregistry.adoption.model.ReleaseApplicationWfRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Component
public class WorkflowRepositoryCustom {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public Map<String, Object> claimApplicationWorkflow(ClaimApplicationWfRequest request) {
        // Create stored procedure query for `get_user_count_and_details`
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("applications.sp_application_claim");

        storedProcedure.registerStoredProcedureParameter("p_application_register_id", Long.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_application_register_id", request.getApplicationRegisterId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_role_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_role_id", request.getCurrentRoleId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_user_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_user_id", request.getCurrentUserId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_institute_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_institute_id", request.getCurrentInstituteId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_status_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_status_id", request.getCurrentStageId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_next_status_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_next_status_id", request.getNextStageId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_action_id", Short.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_action_id", request.getActionId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_workflow_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_workflow_id", request.getWorkflowId());
    	
    	storedProcedure.registerStoredProcedureParameter("re_code", Integer.class, ParameterMode.INOUT);
    	storedProcedure.setParameter("re_code", 0);
    	
    	storedProcedure.registerStoredProcedureParameter("re_msg", String.class, ParameterMode.INOUT);
    	storedProcedure.setParameter("re_msg", "resmessage");
        
        // Execute the stored procedure
    	storedProcedure.execute();

        // Get the output parameters
    	Integer reCode = (Integer) storedProcedure.getOutputParameterValue("re_code");
        String reMsg = (String) storedProcedure.getOutputParameterValue("re_msg");

        
        Map<String, Object> response = new HashMap<>();
        response.put("re_code", reCode);
        response.put("re_msg", reMsg);

        return response;

    }

	public Map<String, Object> releaseApplicationWorkflow(ReleaseApplicationWfRequest request) {
        // Create stored procedure query for `get_user_count_and_details`
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("applications.sp_application_release");

        storedProcedure.registerStoredProcedureParameter("p_application_register_id", Long.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_application_register_id", request.getApplicationRegisterId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_role_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_role_id", request.getCurrentRoleId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_user_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_user_id", request.getCurrentUserId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_institute_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_institute_id", request.getCurrentInstituteId());
    	
    	storedProcedure.registerStoredProcedureParameter("p_current_status_id", Integer.class, ParameterMode.IN);
    	storedProcedure.setParameter("p_current_status_id", request.getCurrentStageId());
    	
    	storedProcedure.registerStoredProcedureParameter("re_code", Integer.class, ParameterMode.INOUT);
    	storedProcedure.setParameter("re_code", 0);
    	
    	storedProcedure.registerStoredProcedureParameter("re_msg", String.class, ParameterMode.INOUT);
    	storedProcedure.setParameter("re_msg", "resmessage");
        
        // Execute the stored procedure
    	storedProcedure.execute();

        // Get the output parameters
    	Integer reCode = (Integer) storedProcedure.getOutputParameterValue("re_code");
        String reMsg = (String) storedProcedure.getOutputParameterValue("re_msg");

        
        Map<String, Object> response = new HashMap<>();
        response.put("re_code", reCode);
        response.put("re_msg", reMsg);

        return response;

    }

}
