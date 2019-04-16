/*
 * MessageRepository.java
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

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select distinct m from Actor a join a.messages m left join m.tags t where a.id = ?1 order by t")
	Collection<Message> findMessagesOrderByTagByActorId(int actorId);

	@Query("select distinct m from Actor a join a.messages m where m.sender.id = ?1")
	Collection<Message> findMessagesSentByActorId(int actorId);

	@Query("select distinct m from Message m where ?1 member of m.recipients")
	Collection<Message> findMessagesByReccipientActorId(int idActor);

	@Query("select distinct m from Message m join m.sender a where a.id = ?1")
	Collection<Message> findMessagesBySenderActorId(int idActor);

}
