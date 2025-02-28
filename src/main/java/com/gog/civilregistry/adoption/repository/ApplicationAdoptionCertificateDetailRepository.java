package com.gog.civilregistry.adoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gog.civilregistry.adoption.dto.GenerateAdoptionCertificateDTO;
import com.gog.civilregistry.adoption.entity.ApplicationAdoptionCertificateDetailEntity;

public interface ApplicationAdoptionCertificateDetailRepository
		extends JpaRepository<ApplicationAdoptionCertificateDetailEntity, Long>{

	ApplicationAdoptionCertificateDetailEntity findByApplicationRegisterId(Long applicationRegisterId);
	
	@Query(value = "select t.entry_no, t.child_gender, t.child_name,\r\n"
			+ "t.father_name, t.father_occupation, t.father_address,\r\n"
			+ "t.mother_name, t.mother_occupation, t.mother_address,\r\n"
			+ "t.child_date_of_birth, t.place_of_birth, t,court_order_date, \r\n"
			+ "t.adoption_registration_number, t.adoption_certificate_number,t.nod_entry_no,t.adoption_certificate_approval_date,\r\n"
			+ "p.approved_by_role,p.approved_by_user\r\n"
			+ "from\r\n"
			+ "(select taad.entry_no,taacd.application_register_id,\r\n"
			+ "(select mmdv.data_description\r\n"
			+ "from masters.m_master_data_value mmdv\r\n"
			+ "where mmdv.master_data_value_id = cast(taad.child_gender as integer) ) child_gender,\r\n"
			+ "masters.fn_get_full_name(tcr.first_name, tcr.middle_name, tcr.last_name) child_name,\r\n"
			+ "(select masters.fn_get_full_name(tmc.first_name, tmc.middle_name, tmc.last_name) \r\n"
			+ "from citizen.t_manage_citizen tmc \r\n"
			+ "where tmc.citizen_id = taad.father_citizen_id) father_name,\r\n"
			+ "taad.father_occupation,\r\n"
			+ "(select mg.geography_name from masters.m_geography mg\r\n"
			+ "where mg.geography_id = cast(taad.father_address as integer)) father_address,\r\n"
			+ "(select masters.fn_get_full_name(tmc.first_name, tmc.middle_name, tmc.last_name) \r\n"
			+ "from citizen.t_manage_citizen tmc \r\n"
			+ "where tmc.citizen_id = taad.mother_citizen_id) mother_name,\r\n"
			+ "taad.mother_occupation,\r\n"
			+ "(select mg.geography_name from masters.m_geography mg\r\n"
			+ "where mg.geography_id = cast(taad.mother_address as integer)) mother_address,\r\n"
			+ "to_char(taad.child_date_of_birth,'dd/mm/yyyy') child_date_of_birth,\r\n"
			+ "tcr.place_of_birth,\r\n"
			+ "to_char(taad.court_order_date,'dd/mm/yyyy') court_order_date,\r\n"
			+ "tar.adoption_registration_number,\r\n"
			+ "tar.adoption_certificate_number,\r\n"
			+ "taad.nod_entry_no,\r\n"
			+ "to_char(tar.adoption_certificate_approval_date,'dd/mm/yyyy') adoption_certificate_approval_date\r\n"
			+ "from adoption.t_application_adoption_certificate_details taacd\r\n"
			+ "inner join adoption.t_application_adoption_details taad \r\n"
			+ "on taacd.application_adoption_id = taad.application_adoption_id \r\n"
			+ "inner join adoption.t_adoption_register tar \r\n"
			+ "on tar.application_adoption_id = taacd.application_adoption_id \r\n"
			+ "inner join citizen.t_citizen_register tcr \r\n"
			+ "on tcr.citizen_id = taad.child_citizen_id \r\n"
			+ "and taacd.application_register_id = :certficateApplicationId)t\r\n"
			+ "left join \r\n"
			+ "(select taw.application_register_id ,\r\n"
			+ "(select mr.role_name from masters.m_role mr where mr.role_id = taw.assigned_from_role) approved_by_role, \r\n"
			+ "(select  concat_ws(', ',tmc.last_name ,concat_ws(' ',tmc.first_name ,tmc.middle_name))\r\n"
			+ "from citizen.t_user tu, citizen.t_manage_citizen tmc  \r\n"
			+ "where tu.citizen_id = tmc.citizen_id  \r\n"
			+ "and tu.user_id = taw.assigned_from_user) approved_by_user\r\n"
			+ "from applications.t_application_workflow taw \r\n"
			+ "where taw.application_register_id = :certficateApplicationId\r\n"
			+ "and taw.stage_id = 13\r\n"
			+ "and taw.is_active = true)p\r\n"
			+ "on t.application_register_id = p.application_register_id", nativeQuery = true)
	public List<GenerateAdoptionCertificateDTO> fetchAdoptionCertificateDetails(Long certficateApplicationId);

}
