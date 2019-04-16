/*
 * CurriculaRepository.java
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

import domain.Curricula;

@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer> {

	@Query("select c from Curricula c join c.hacker h where c.isCopy = 0 and h.id = ?1 ")
	Collection<Curricula> findCurriculasByHackerId(int hackerId);

	@Query("select c from Curricula c join c.personalData pd where pd.id = ?1")
	Curricula findCurriculaByPersonalDataId(int personalDataId);

	@Query("select c from Curricula c join c.positionDatas pd where pd.id = ?1")
	Curricula findCurriculaByPositionDataId(int positionDataId);

	@Query("select c from Curricula c join c.educationDatas ed where ed.id = ?1")
	Curricula findCurriculaByEducationDataId(int educationDataId);

	@Query("select c from Curricula c join c.miscellaneousDatas md where md.id = ?1")
	Curricula findCurriculaByMiscellaneousDataId(int miscellaneousDataId);

}
