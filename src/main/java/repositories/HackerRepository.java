/*
 * HackerRepository.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Hacker;

@Repository
public interface HackerRepository extends JpaRepository<Hacker, Integer> {

	@Query("select h from Hacker h join h.applications a where a.id = ?1")
	Hacker findHackerByApplicationId(int applicationId);

	@Query("select h from Curricula c join c.hacker h where c.id = ?1")
	Hacker findHackerByCurriculaId(int curriculaId);

}
