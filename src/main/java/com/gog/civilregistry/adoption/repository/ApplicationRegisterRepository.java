package com.gog.civilregistry.adoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gog.civilregistry.adoption.dto.GetCivilRegistryNumberDTO;
import com.gog.civilregistry.adoption.entity.ApplicationRegisterEntity;

@Repository
public interface ApplicationRegisterRepository extends JpaRepository<ApplicationRegisterEntity, Long> {

	@Query(value = "SELECT MAX(application_register_id) FROM applications.t_application_register", nativeQuery = true)
	Integer findLastApplicationRegisterId();

	ApplicationRegisterEntity findByApplicationNo(String applicationNo);

	@Query(value = "SELECT tmc.citizen_id, tmc.first_name as firstName , tmc.civil_registry_number as civilRegistryNumber "
			+ "FROM citizen.t_manage_citizen tmc " + "WHERE tmc.citizen_id IN (:citizenIds)", nativeQuery = true)
	List<GetCivilRegistryNumberDTO> getCivilRegNoFromCitizenId(@Param("citizenIds") List<Integer> citizenIds);
}
