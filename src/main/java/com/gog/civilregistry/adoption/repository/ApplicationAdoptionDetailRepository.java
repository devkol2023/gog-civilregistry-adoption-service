package com.gog.civilregistry.adoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gog.civilregistry.adoption.dto.GetCivilRegistryNumberDTO;
import com.gog.civilregistry.adoption.entity.ApplicationAdoptionDetailEntity;
import com.gog.civilregistry.adoption.model.ChildAdoptionDetailsProjection;
import com.gog.civilregistry.adoption.model.TownCodeProjection;
import com.gog.civilregistry.adoption.model.TownProjection;

@Repository
public interface ApplicationAdoptionDetailRepository extends JpaRepository<ApplicationAdoptionDetailEntity, Long>,
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
			+ "FROM citizen.t_manage_citizen tmc " + "WHERE tmc.citizen_id IN (:citizenIds)", nativeQuery = true)
	List<GetCivilRegistryNumberDTO> getCivilRegNoFromCitizenId(@Param("citizenIds") List<Integer> citizenIds);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO masters.m_geography\r\n"
			+ "( geography_name, geography_code, geography_coodrinate, geography_level_name, geography_parent_id, created_by, created_on, updated_by, updated_on, is_active)\r\n"
			+ "VALUES( :name, :code,'', 'CITY', :parentId, 1, now(), 1, now(), true);\r\n" + "", nativeQuery = true)
	Integer saveTown(@Param("name") String name, @Param("code") String code, @Param("parentId") Integer parentId);

	@Query(value = "select geography_id as id, geography_name as name \r\n" + "from masters.m_geography mg \r\n"
			+ "where mg.geography_name = :name\r\n" + "and mg.geography_level_name ='CITY'", nativeQuery = true)
	TownProjection getTown(@Param("name") String name);

	@Query(value = "select count(*) from \r\n" + "masters.m_geography mg \r\n"
			+ "where mg.geography_level_name like '%CITY%' \r\n"
			+ "and mg.geography_parent_id = :parentId", nativeQuery = true)
	TownCodeProjection getCountForTownCode(@Param("parentId") Integer parentId);

	@Query(value = "select application_workflow_id,application_register_id,submitted_by, "
			+ "submitted_to, date_of_submission, claimed_by, claimed_date, released_date, stage  "
			+ " from applications.view_application_tracker" + " where application_register_id = :applicationId"
			+ " order by application_register_id,application_workflow_id", nativeQuery = true)
	List<Object[]> trackApplicationStatus(Long applicationId);

	@Query(value = "SELECT * from adoption.fn_get_document_type_list(?1, ?2, ?3, ?4)", nativeQuery = true)
	List<Object[]> getDocList(Long applicationRegisterId, Integer applicationTypeId, String roleCode,
			Integer citizenId);

	@Query(value = "select \r\n" + "tcr.citizen_id as childCitizenId,\r\n"
			+ "tcr.citizen_civil_registry_number as childCivilRegistryNumber,\r\n"
			+ "tcr.first_name as childFirstName,\r\n" + "tcr.last_name as childSurname,\r\n"
			+ "tcr.middle_name as childMiddleName,\r\n" + "TO_CHAR(tcr.date_of_birth, 'DD/MM/YYYY')as  dateOfBirth,\r\n"
			+ "tcr.parish_of_birth as parishOfBirth,\r\n" + "tcr.gender_id as gender,\r\n"
			+ "tcr.father_citizen_id as fatherCitizenId, \r\n"
			+ "tcr.father_civil_registry_number as fatherCivilRegistryNumber, \r\n"
			+ "tcr.father_first_name as fatherFirstName,\r\n" + "tcr.father_middle_name as fatherMiddleName,\r\n"
			+ " tcr.father_last_name as fatherSurname,\r\n" + " tcr.mother_citizen_id as motherCitizenId,\r\n"
			+ " tcr.mother_civil_registry_number as motherCivilRegistryNumber,\r\n"
			+ " tcr.mother_first_name as motherFirstName,\r\n" + " tcr.mother_middle_name as motherMiddleName,\r\n"
			+ " tcr.mother_last_name as motherSurname,\r\n" + " tcr.mother_maiden_surname as motherMaidenSurname\r\n"
			+ "from citizen.t_citizen_register tcr \r\n"
			+ "where tcr.citizen_civil_registry_number = :civilRegistryNumber", nativeQuery = true)
	ChildAdoptionDetailsProjection getChildInfoFromCitizenRegister(
			@Param("civilRegistryNumber") String civilRegistryNumber);

	@Query(value = "select \r\n" + "tmc.citizen_id as childCitizenId,\r\n"
			+ "tmc.civil_registry_number as childCivilRegistryNumber,\r\n" + "tmc.first_name as childFirstName,\r\n"
			+ "tmc.last_name as childSurname,\r\n" + "tmc.middle_name as childMiddleName,\r\n"
			+ "TO_CHAR(tmc.date_of_birth, 'DD/MM/YYYY')as  dateOfBirth,\r\n" + "tmc.parish_id as parishOfBirth,\r\n"
			+ "tmc.gender_id as gender,\r\n" + "null as fatherCitizenId, \r\n"
			+ "null as fatherCivilRegistryNumber, \r\n" + "null as fatherFirstName,\r\n"
			+ "null as fatherMiddleName,\r\n" + " null as fatherSurname,\r\n"
			+ " tmc.mother_citizen_id as motherCitizenId,\r\n"
			+ " tmcm.civil_registry_number as motherCivilRegistryNumber,\r\n"
			+ " tmc.mother_firstname as motherFirstName,\r\n" + " tmc.mother_middlename as motherMiddleName,\r\n"
			+ " tmc.mother_surname as motherSurname,\r\n" + " tmc.mother_surname as motherMaidenSurname\r\n"
			+ "from citizen.t_manage_citizen tmc\r\n" + "left join citizen.t_manage_citizen tmcm\r\n"
			+ "on tmcm.citizen_id = tmc.mother_citizen_id \r\n"
			+ "where tmc.civil_registry_number = :civilRegistryNumber", nativeQuery = true)
	ChildAdoptionDetailsProjection getChildInfoFromManageCitizen(
			@Param("civilRegistryNumber") String civilRegistryNumber);

}
