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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.isFinalMode = 1 and p.isCancelled = 0")
	Collection<Position> findPositionsFinalModeNotCancelled();

	@Query("select p from Company c join c.positions p where p.isFinalMode = 1 and p.isCancelled = 0 and c.id = ?1")
	Collection<Position> findPositionsFinalModeNotCancelledByCompanyId(int companyId);

	@Query("select distinct p from Company c join c.positions p join p.skills s join p.technologies t where (p.title like %?1% or p.description like %?1% or p.profile like %?1% or s like %?1% or t like %?1% or c.commercialName like %?1%) and p.isFinalMode = 1 and p.isCancelled = 0")
	Collection<Position> findPositionsFinalModeNotCancelledBySingleKeyWord(String singleKeyWord);

}
