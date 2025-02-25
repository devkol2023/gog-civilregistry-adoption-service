package com.gog.civilregistry.adoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gog.civilregistry.adoption.dto.ApplicationRegisterTypeDTO;
import com.gog.civilregistry.adoption.dto.FindByAppNumberDTO;
import com.gog.civilregistry.adoption.dto.GetCivilRegistryNumberDTO;
import com.gog.civilregistry.adoption.entity.ApplicationAdoptionDetailEntity;
import com.gog.civilregistry.adoption.model.CitizenDetailsProjectionNew;

@Repository
public interface ApplicationAdoptionDetailRepository
		extends JpaRepository<ApplicationAdoptionDetailEntity, Long>,
		JpaSpecificationExecutor<ApplicationAdoptionDetailEntity> {

	@Query(value = "select * from death.fn_get_workflow_next_stage_death(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
	List<Object[]> callWorkflowFunction(Long applicationId, Integer applicationTypeId, Integer currentStageId,
			String currentRoleCode, Integer nextInstituteTypeId, String nextRoleCode);

	@Query(value = "SELECT * from applications.fn_get_workflow_next_users(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
	List<Object[]> getWfNextUser(Long applicationRegisterId, Integer currentInstituteId, Integer currentRoleId,
			Integer currentUserId, Integer currentParishId, Integer nextRoleId, Integer nextWorkflowId,
			Short isUserVisible, Integer nextInstituteTypeId);
	

   @Query(value = "select tar.application_register_id from applications.t_application_register tar where tar.application_no = ?1", nativeQuery = true)
    Long getIdFromApplicationNo(String applicationNumber);
   
   ApplicationAdoptionDetailEntity findByApplicationRegisterId(Long applicationRegisterId);
   
   @Query(value = "SELECT tmc.citizen_id, tmc.first_name as firstName , tmc.civil_registry_number as civilRegistryNumber "
	        + "FROM citizen.t_manage_citizen tmc "
	        + "WHERE tmc.citizen_id IN (:citizenIds)", nativeQuery = true)
	List<GetCivilRegistryNumberDTO> getCivilRegNoFromCitizenId(@Param("citizenIds") List<Integer> citizenIds);
   
   
   @Query(value = "select application_workflow_id,application_register_id,submitted_by, "
			+ "submitted_to, date_of_submission, claimed_by, claimed_date, released_date, stage  "
			+ " from applications.view_application_tracker"
			+ " where application_register_id = :applicationId"
			+ " order by application_register_id,application_workflow_id",
	        nativeQuery = true)
	List<Object[]> trackApplicationStatus(Long applicationId);
   
   
   
}
