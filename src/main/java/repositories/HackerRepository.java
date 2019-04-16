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

import java.util.Collection;

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

	@Query("select h from Curricula c join c.hacker h join c.personalData pd where pd.id = ?1")
	Hacker findHackerByPersonalDataId(int personalDataId);

	@Query("select h from Curricula c join c.hacker h join c.positionDatas pd where pd.id = ?1")
	Hacker findHackerByPositionDataId(int positionDataId);

	@Query("select h from Curricula c join c.hacker h join c.educationDatas ed where ed.id = ?1")
	Hacker findHackerByEducationDataId(int educationDataId);

	@Query("select h from Curricula c join c.hacker h join c.miscellaneousDatas md where md.id = ?1")
	Hacker findHackerByMiscellaneousDataId(int miscellaneousDataId);

	@Query("select distinct h from Hacker h join h.applications a join a.position p where p.id = ?1")
	Collection<Hacker> findHackersByPositionId(int positionId);

	@Query("select distinct h from Hacker h join h.applications a join a.problem p where p.id = ?1")
	Collection<Hacker> findHackersByProblemId(int problemId);

}
