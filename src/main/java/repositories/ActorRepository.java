/*
 * ActorRepository.java
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

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findActorByUserAccountId(int userAccountId);

	@Query("select a from Actor a where a.name like 'System'")
	Actor getSystemActor();

	@Query("select a from Actor a where a.name like 'User deleted'")
	Actor getDeletedActor();

	@Query("select a from SocialProfile sp join sp.actor a where sp.id = ?1")
	Actor findActorBySocialProfileId(int socialProfileId);

	@Query("select a from Actor a join a.messages m where m.id = ?1")
	Actor findActorByMessageId(int messageId);

	@Query("select a from Actor a where a.userAccount.statusAccount = 1 and a.isSpammer = 1")
	Collection<Actor> findActorsToBan();

	@Query("select a from Actor a where a.userAccount.statusAccount = 0")
	Collection<Actor> findActorsBanned();
}
