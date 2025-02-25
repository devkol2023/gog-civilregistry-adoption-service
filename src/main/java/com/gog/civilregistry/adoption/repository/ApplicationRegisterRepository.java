package com.gog.civilregistry.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gog.civilregistry.adoption.entity.ApplicationRegisterEntity;

@Repository
public interface ApplicationRegisterRepository extends JpaRepository<ApplicationRegisterEntity, Long> {

	@Query(value = "SELECT MAX(application_register_id) FROM applications.t_application_register", nativeQuery = true)
	Integer findLastApplicationRegisterId();

	ApplicationRegisterEntity findByApplicationNo(String applicationNo);
}
