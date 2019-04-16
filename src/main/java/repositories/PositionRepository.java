/*
 * PositionRepository.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE")
	Collection<Position> findPositionsFinalModeNotCancelledNotDeadline();

	@Query("select p from Company c join c.positions p where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and c.id = ?1")
	Collection<Position> findPositionsFinalModeNotCancelledNotDeadlineByCompanyId(int companyId);

	@Query("select distinct p from Company c join c.positions p join p.skills s join p.technologies t where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and (p.title like %?1% or p.description like %?1% or p.profile like %?1% or s like %?1% or t like %?1% or c.commercialName like %?1%)")
	Collection<Position> findPositionsFinalModeNotCancelledNotDeadlineBySingleKeyWord(String singleKeyWord);

	@Query("select p from Position p where p.ticker like %?1%")
	Position findPositionByTicker(String ticker);

	@Query("select p from Company c join c.positions p where c.id = ?1")
	Collection<Position> findPositionsByCompanyId(int companyId);

	@Query("select distinct p from Hacker h join h.applications a join a.position p where a.status != 'REJECTED' and p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and h.id = ?1")
	Collection<Position> findPositionsApplicationsNotRejectedByHackerId(int hackerId);

	@Query("select distinct p from Company c join c.positions p join p.skills s join p.technologies t where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and (p.ticker like %?1% or p.title like %?1% or p.description like %?1% or p.profile like %?1% or s like %?1% or t like %?1%)")
	Collection<Position> findPositionsFilterByKeyWord(String keyWord);

	@Query("select p from Company c join c.positions p where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and p.deadline = ?1")
	Collection<Position> findPositionsFilterByDeadline(Date deadline);

	@Query("select p from Company c join c.positions p where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and p.salary >= ?1")
	Collection<Position> findPositionsFilterByMinSalary(Double minSalary);

	@Query("select p from Company c join c.positions p where p.isFinalMode = 1 and p.isCancelled = 0 and p.deadline > CURRENT_DATE and p.deadline <= ?1")
	Collection<Position> findPositionsFilterByMaxDeadline(Date maxDeadline);

}
