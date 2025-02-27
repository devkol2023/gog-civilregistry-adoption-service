package com.gog.civilregistry.adoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gog.civilregistry.adoption.dto.GetCivilRegistryNumberDTO;
import com.gog.civilregistry.adoption.entity.ApplicationRegisterEntity;
import com.gog.civilregistry.adoption.model.ACDownloadResponseDTO;
import com.gog.civilregistry.adoption.model.BirthCertificateApplicationResponseProjection;

@Repository
public interface ApplicationRegisterRepository extends JpaRepository<ApplicationRegisterEntity, Long> {

	@Query(value = "SELECT MAX(application_register_id) FROM applications.t_application_register", nativeQuery = true)
	Integer findLastApplicationRegisterId();

	ApplicationRegisterEntity findByApplicationNo(String applicationNo);

	@Query(value = "SELECT tmc.citizen_id, tmc.first_name as firstName , tmc.civil_registry_number as civilRegistryNumber "
			+ "FROM citizen.t_manage_citizen tmc " + "WHERE tmc.citizen_id IN (:citizenIds)", nativeQuery = true)
	List<GetCivilRegistryNumberDTO> getCivilRegNoFromCitizenId(@Param("citizenIds") List<Integer> citizenIds);

	@Query(value = """
			SELECT
			    tad.child_citizen_id AS childCitizenId,
			    tmc_child.civil_registry_number AS childCivilRegistryNumber,
			    tad.mother_citizen_id AS motherCitizenId,
			    tad.father_citizen_id AS fatherCitizenId,
			    concat_ws(' ', tad.child_first_name, tad.child_middle_name, tad.child_surname) AS childName,
			    concat_ws(' ', tad.mother_first_name, tad.mother_middle_name, tad.mother_surname) AS motherName,
			    concat_ws(' ', tad.father_first_name, tad.father_middle_name, tad.father_surname) AS fatherName,
			    TO_CHAR(tad.child_date_of_birth, 'DD/MM/YYYY') AS dateOfBirth,
			    mg.geography_name AS parishOfBirth,
			    tar.adoption_certificate_number AS adoptionCertificateNumber,
			    tar.adoption_certifcate_dms_id AS adoptionCertificateDmsId
			FROM adoption.t_application_adoption_details tad
			LEFT JOIN citizen.t_manage_citizen tmc_child ON tmc_child.citizen_id = tad.child_citizen_id
			LEFT JOIN masters.m_geography mg ON mg.geography_id = tad.child_parish
			LEFT JOIN adoption.t_adoption_register tar ON tar.application_adoption_id = tad.application_adoption_id
			WHERE (:childCivilRegistryNumber IS NULL OR tmc_child.civil_registry_number = :childCivilRegistryNumber)
			    AND (:childName IS NULL OR LOWER(tad.child_first_name) = LOWER(:childName))
			    AND (:motherName IS NULL OR LOWER(tad.mother_first_name) = LOWER(:motherName))
			    AND (:fatherName IS NULL OR LOWER(tad.father_first_name) = LOWER(:fatherName))
			    AND (:dateOfBirth IS NULL OR tad.child_date_of_birth = TO_DATE(:dateOfBirth, 'DD/MM/YYYY'))
			    AND (:parishId IS NULL OR mg.geography_id = CAST(:parishId AS INTEGER))
			    AND (:genderId IS NULL OR tad.child_gender = :genderId)
			    AND tar.adoption_certificate_number IS NOT NULL
			    AND tar.adoption_certifcate_dms_id IS NOT NULL
			""", nativeQuery = true)
	List<ACDownloadResponseDTO> searchACDownloadList(@Param("childCivilRegistryNumber") String childCivilRegistryNumber,
			@Param("childName") String childName, @Param("motherName") String motherName,
			@Param("fatherName") String fatherName, @Param("dateOfBirth") String dateOfBirth,
			@Param("parishId") Integer parishId, @Param("genderId") Integer genderId);

	ApplicationRegisterEntity findByApplicationRegisterId(Long applicationRegisterId);
	
	@Query(value = " SELECT \r\n"
			+ "		        tar.application_register_id AS applicationRegisterId,\r\n"
			+ "		        tar.application_no AS applicationNo,\r\n"
			+ "		        tbr.adoption_registration_number AS adoptionRegistrationNumber,\r\n"
			+ "		        tmc.citizen_id AS childCitizenId,\r\n"
			+ "		        tmc.civil_registry_number AS childCivilRegistryNumber,\r\n"
			+ "		        masters.fn_get_full_name(tand.child_first_name, tand.child_middle_name, tand.child_surname) AS childName,\r\n"
			+ "		        masters.fn_get_full_name(tand.mother_first_name, tand.mother_middle_name, tand.mother_surname) AS motherName,\r\n"
			+ "		        masters.fn_get_full_name(tand.father_first_name, tand.father_middle_name, tand.father_surname) AS fatherName,\r\n"
			+ "		        TO_CHAR(CAST(tand.child_date_of_birth AS DATE), 'DD/MM/YYYY') AS dateOfBirth,\r\n"
			+ "		        mg.geography_name AS parishOfChild,\r\n"
			+ "		        0 AS isAdoptionCertificateApprove \r\n"
			+ "		    FROM \r\n"
			+ "		        adoption.t_adoption_register tbr\r\n"
			+ "		    INNER JOIN \r\n"
			+ "		        adoption.t_application_adoption_details tand \r\n"
			+ "		        ON tbr.application_adoption_id = tand.application_adoption_id\r\n"
			+ "		    INNER JOIN \r\n"
			+ "		        applications.t_application_register tar \r\n"
			+ "		        ON tar.application_register_id = tand.application_register_id\r\n"
			+ "		    LEFT JOIN \r\n"
			+ "		        citizen.t_manage_citizen tmc \r\n"
			+ "		        ON tmc.citizen_id = tand.child_citizen_id\r\n"
			+ "		    LEFT JOIN \r\n"
			+ "		        masters.m_geography mg \r\n"
			+ "		        ON mg.geography_id = tand.child_parish\r\n"
			+ "		    LEFT JOIN \r\n"
			+ "		        citizen.t_citizen_register tcr \r\n"
			+ "		        ON tcr.citizen_id = tmc.citizen_id\r\n"
			+ "		    WHERE \r\n"
			+ "		        (tand.father_citizen_id = :pCitizenId OR tand.mother_citizen_id = :pCitizenId)\r\n"
			+ "		        AND tbr.citizen_id NOT IN (\r\n"
			+ "		            SELECT tcam.citizen_id\r\n"
			+ "		            FROM applications.t_application_register tar\r\n"
			+ "		            INNER JOIN citizen.t_citizen_application_map tcam \r\n"
			+ "		                ON tar.application_register_id = tcam.application_register_id\r\n"
			+ "		            WHERE tar.current_status_id <> 13 \r\n"
			+ "		                AND tcam.citizen_id IN (\r\n"
			+ "		                    SELECT tand.child_citizen_id \r\n"
			+ "		                    FROM adoption.t_application_adoption_details tand\r\n"
			+ "		                    WHERE tand.father_citizen_id = :pCitizenId OR tand.mother_citizen_id = :pCitizenId\r\n"
			+ "		                )\r\n"
			+ "		        )", nativeQuery = true)
		List<BirthCertificateApplicationResponseProjection> getListForBirthCertificateApply(
		        @Param("pCitizenId") Integer pCitizenId
		);

	

}
