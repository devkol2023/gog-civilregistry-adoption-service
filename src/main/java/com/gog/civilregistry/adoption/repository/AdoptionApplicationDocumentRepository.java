package com.gog.civilregistry.adoption.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gog.civilregistry.adoption.entity.AdoptionApplicationDocumentEntity;

@Repository
public interface AdoptionApplicationDocumentRepository extends JpaRepository<AdoptionApplicationDocumentEntity, Long> {

	List<AdoptionApplicationDocumentEntity> findByApplicationRegisterId(Long applicationRegisterId);

	@Modifying
	@Transactional
	@Query(value = " update marriage.t_marriage_application_documents \r\n" + "	set is_active = false \r\n"
			+ "			where marriage_application_doc_id in (:applicationDocId)", nativeQuery = true)
	void updateFileIdIsActive(List<Integer> applicationDocId);

	List<AdoptionApplicationDocumentEntity> findByApplicationRegisterIdAndIsActive(Long applicationRegisterId,
			boolean b);

}
