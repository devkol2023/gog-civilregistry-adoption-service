package com.gog.civilregistry.adoption.repository.custom;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gog.civilregistry.adoption.model.VaultResponse;
import com.gog.civilregistry.adoption.util.CommonConstants;
import com.gog.civilregistry.adoption.util.SqlConstants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Component
public class ApplicationRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public List<VaultResponse> executeVault(String applicationTypeCode, String state, Integer userId,
			Integer instituteId, Integer roleId, Integer pageNumber, Integer perPageCount, String searchText) {

		Query query = entityManager.createNativeQuery(SqlConstants.APPLICATION_VAULT_QUERY);
		query.setParameter("pApplicationTypeCode", applicationTypeCode);
		query.setParameter("pState", state);
		query.setParameter("pUserId", userId);
		query.setParameter("pInstituteId", instituteId);
		query.setParameter("pRoleId", roleId);
		query.setParameter("pPageNumber", pageNumber);
		query.setParameter("pPerPageCount", perPageCount);
		query.setParameter("pSearchText", searchText);

		List<Object[]> results = query.getResultList();

		return results.stream()
				.map(row -> new VaultResponse((Long) row[0], (String) row[1], (String) row[2], (String) row[3], (String) row[4], (Date) row[5],
						(String) row[6], (String) row[7],
						row[8] != null ? ((Timestamp) row[8]).toLocalDateTime().toLocalDate() : null, (Integer) row[9], (Integer) row[10], 
						(String) row[11], (Integer) row[12], (Integer) row[13], 
						(Integer) row[14], (String) row[15], (Integer) row[16], (String) row[17], (Integer) row[18], (Integer) row[19], (String) row[20], (Integer) row[21]))
				.collect(Collectors.toList());

	}
	
	
}
