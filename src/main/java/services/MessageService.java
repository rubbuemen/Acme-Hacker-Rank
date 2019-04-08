
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed repository
	@Autowired
	private MessageRepository	messageRepository;


	// Supporting services

	// Simple CRUD methods
	public Message create() {

		Message result;

		result = new Message();

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

	public Message save(final Message message) {
		Assert.notNull(message);

		Message result;

		if (message.getId() == 0)
			result = this.messageRepository.save(message);
		else
			result = this.messageRepository.save(message);

		return result;
	}

	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(this.messageRepository.exists(message.getId()));

		this.messageRepository.delete(message);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Message reconstruct(final Message message, final BindingResult binding) {
		Message result;

		if (message.getId() == 0)
			result = message;
		else {
			result = this.messageRepository.findOne(message.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.messageRepository.flush();
	}

}
