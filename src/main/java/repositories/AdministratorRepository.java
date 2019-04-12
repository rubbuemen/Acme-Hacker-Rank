/*
 * AdministratorRepository.java
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

import domain.Administrator;
import domain.Company;
import domain.Hacker;
import domain.Position;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select avg(c.positions.size), min(c.positions.size), max(c.positions.size), stddev(c.positions.size) from Company c")
	String dashboardQueryC1();

	@Query("select avg(h.applications.size), min(h.applications.size), max(h.applications.size), stddev(h.applications.size) from Hacker h")
	String dashboardQueryC2();

	@Query("select c from Company c join c.positions p group by c order by sum(p) desc")
	Collection<Company> dashboardQueryC3();

	@Query("select h from Hacker h join h.applications a group by h order by sum(a) desc")
	Collection<Hacker> dashboardQueryC4();

	@Query("select avg(p.salary), min(p.salary), max(p.salary), stddev(p.salary) from Position p")
	String dashboardQueryC5();

	@Query("select p1 from Position p1 where p1.salary = (select max(p2.salary) from Position p2)")
	Collection<Position> dashboardQueryC6_1();

	@Query("select p1 from Position p1 where p1.salary = (select min(p2.salary) from Position p2)")
	Collection<Position> dashboardQueryC6_2();

	@Query("select avg(1.0*(select count(c) from Curricula c where c.hacker.id = h.id and c.isCopy = 0)), min(1*(select count(c) from Curricula c where c.hacker.id = h.id and c.isCopy = 0)), max(1*(select count(c) from Curricula c where c.hacker.id = h.id and c.isCopy = 0)), stddev(1.0*(select count(c) from Curricula c where c.hacker.id = h.id and c.isCopy = 0)) from Hacker h")
	String dashboardQueryB1();

	@Query("select avg(f.positions.size), min(f.positions.size), max(f.positions.size), stddev(f.positions.size) from Finder f")
	String dashboardQueryB2();

	@Query("select sum(case when f.positions.size = 0 then 1.0 else 0.0 end)/sum(case when f.positions.size > 0 then 1.0 else 0.0 end) from Finder f")
	String dashboardQueryB3();
}
