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

}
