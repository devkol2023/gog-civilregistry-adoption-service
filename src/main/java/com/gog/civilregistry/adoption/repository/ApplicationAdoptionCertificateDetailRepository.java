package com.gog.civilregistry.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gog.civilregistry.adoption.entity.ApplicationAdoptionCertificateDetailEntity;

public interface ApplicationAdoptionCertificateDetailRepository extends JpaRepository<ApplicationAdoptionCertificateDetailEntity, Long>,
JpaSpecificationExecutor<ApplicationAdoptionCertificateDetailEntity> {
	
	

}
