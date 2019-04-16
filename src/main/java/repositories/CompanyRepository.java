/*
 * CompanyRepository.java
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

import domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	@Query("select c from Company c join c.positions p where p.id = ?1")
	Company findCompanyByPositionId(int positionId);

	@Query("select c from Company c join c.problems p where p.id = ?1")
	Company findCompanyByProblemId(int problemId);

	@Query("select c from Company c join c.positions p join p.applications a where a.id = ?1")
	Company findCompanyByApplicationId(int applicationId);

	@Query("select distinct c from Company c join c.positions p join p.applications a join a.curricula cu where cu.id = ?1")
	Collection<Company> findCompaniesByCurriculaId(int curriculaId);

}
