/*
 * ProblemRepository.java
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

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query("select p from Company c join c.problems p where p.isFinalMode = 1 and c.id = ?1")
	Collection<Problem> findProblemsFinalModeByCompanyId(int companyId);

	@Query("select p from Company c join c.problems p where c.id = ?1")
	Collection<Problem> findProblemsByCompanyId(int companyId);

}
