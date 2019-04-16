
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed repository
	@Autowired
	private MessageRepository			messageRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Simple CRUD methods
	//R23.2
	public Message create() {
		Message result;

		final Actor sender = this.actorService.findActorLogged();
		Assert.notNull(sender);

		result = new Message();
		final Collection<String> tags = new HashSet<>();
		final Collection<Actor> recipients = new HashSet<>();
		final Date moment = new Date(System.currentTimeMillis() - 1);

		result.setRecipients(recipients);
		result.setSender(sender);
		result.setMoment(moment);
		result.setFlagSpam(false);
		result.setTags(tags);

		return result;
	}

	public Message createAuxiliar() {
		Message result;

		result = new Message();
		final Collection<Actor> recipients = new HashSet<>();
		final Date moment = new Date(System.currentTimeMillis() - 1);

		result.setRecipients(recipients);
		result.setMoment(moment);
		result.setFlagSpam(false);

		return result;
	}

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Message findOne(final int messageId) {
		Assert.isTrue(messageId != 0);
		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	//R23.2
	public void save(final Message message, final boolean notification) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0); //Un mensaje no tiene sentido que se edite, por lo que sólo vendrá del create

		Message result;

		// R26
		final Collection<String> spamWords = this.systemConfigurationService.getConfiguration().getSpamWords();
		for (final String sw : spamWords) {
			final Boolean flagSpam = (message.getBody().toLowerCase().contains(sw) || message.getSubject().toLowerCase().contains(sw)) ? true : false;
			if (flagSpam) {
				message.setFlagSpam(flagSpam);
				break;
			}
		}

		final Date moment = new Date(System.currentTimeMillis() - 1);
		message.setMoment(moment);

		if (notification) {
			final String controlTag = "SYSTEM";
			message.getTags().add(controlTag);
		}

		for (final Actor recipient : message.getRecipients()) {
			final Message copyMessage = this.create();
			copyMessage.setMoment(message.getMoment());
			copyMessage.setBody(message.getBody());
			copyMessage.setSubject(message.getSubject());
			copyMessage.setTags(message.getTags());
			copyMessage.setFlagSpam(message.getFlagSpam());
			copyMessage.setSender(message.getSender());
			copyMessage.setRecipients(message.getRecipients());
			result = this.messageRepository.save(copyMessage);
			recipient.getMessages().add(result);
		}
	}

	//R23.2
	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(this.messageRepository.exists(message.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		//R26: If a message is deleted and it doesn't have tag 'DELETED' then it gets tag 'DELETED', but it's not actually deleted from the system; ; if a message with tag 'DELETED' is deleted, then it's actually removed from the system.
		final Collection<Message> messagesActorLogged = actorLogged.getMessages();
		Assert.isTrue(messagesActorLogged.contains(message), "The logged actor is not the owner of this entity");

		if (message.getTags().contains("DELETED")) {
			messagesActorLogged.remove(message);
			actorLogged.setMessages(messagesActorLogged);
			this.actorService.saveAuxiliar(actorLogged);
			this.messageRepository.delete(message);
		} else {
			final String controlTag = "DELETED";
			message.getTags().add(controlTag);
			this.messageRepository.save(message);
		}
	}

	// Other business methods
	//R23.2
	public Collection<Message> findMessagesOrderByTagByActorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		Collection<Message> result;

		result = this.messageRepository.findMessagesOrderByTagByActorId(actorLogged.getId());
		return result;
	}

	public Message findMessageActorLogged(final int messageId) {
		Assert.isTrue(messageId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Actor actorOwner = this.actorService.findActorByMessageId(messageId);
		Assert.isTrue(actorLogged.equals(actorOwner), "The logged actor is not the owner of this entity");

		final Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findMessagesSentByActorId(final int actorId) {
		Assert.isTrue(actorId != 0);

		Collection<Message> result;

		result = this.messageRepository.findMessagesSentByActorId(actorId);
		return result;
	}

	public void deleteActorFromRecipientsMessage() {
		final Actor actor = this.actorService.findActorLogged();
		final Actor deletedActor = this.actorService.getDeletedActor();
		final Collection<Message> messages = this.messageRepository.findMessagesByReccipientActorId(actor.getId());
		for (final Message m : messages) {
			final Collection<Actor> recipients = new HashSet<>(m.getRecipients());
			for (final Actor a : m.getRecipients())
				if (a.equals(actor)) {
					recipients.remove(a);
					recipients.add(deletedActor);
					m.setRecipients(recipients);
					this.messageRepository.save(m);
					break;
				}
		}
	}

	public void deleteActorFromSenderMessage() {
		final Actor actor = this.actorService.findActorLogged();
		final Actor deletedActor = this.actorService.getDeletedActor();
		final Collection<Message> messages = this.messageRepository.findMessagesBySenderActorId(actor.getId());
		for (final Message m : messages) {
			m.setSender(deletedActor);
			this.messageRepository.save(m);
		}
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Message reconstruct(final Message message, final BindingResult binding) {
		Message result;

		if (message.getRecipients() == null || message.getRecipients().contains(null)) {
			final Collection<Actor> recipients = new HashSet<>();
			message.setRecipients(recipients);
		}

		final Actor sender = this.actorService.findActorLogged();
		Assert.notNull(sender);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		message.setSender(sender);
		message.setMoment(moment);
		message.setFlagSpam(false);

		result = message;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.messageRepository.flush();
	}

}
