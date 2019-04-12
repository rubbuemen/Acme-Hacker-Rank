/*
 * ApplicationRepository.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select a from Application a where (a.status = 'PENDING' or a.status = 'SUBMITTED') and a.position.id = ?1")
	Collection<Application> findApplicationsPendingOrSubmittedByPositionId(int positionId);

	@Query("select a from Company c join c.positions p join p.applications a where c.id = ?1 order by a.status")
	Collection<Application> findApplicationsOrderByStatusByCompanyId(int companyId);

	@Query("select a from Hacker h join h.applications a where h.id = ?1 order by a.status")
	Collection<Application> findApplicationsOrderByStatusByHackerId(int hackerId);

}
