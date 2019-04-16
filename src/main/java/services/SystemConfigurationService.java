
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.Actor;
import domain.Message;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	// Managed repository
	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	// Supporting services
	@Autowired
	private ActorService					actorService;

	@Autowired
	private MessageService					messageService;


	// Simple CRUD methods
	public SystemConfiguration create() {
		SystemConfiguration result;

		result = new SystemConfiguration();

		return result;
	}

	public Collection<SystemConfiguration> findAll() {
		Collection<SystemConfiguration> result;

		result = this.systemConfigurationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SystemConfiguration findOne(final int systemConfigurationId) {
		Assert.isTrue(systemConfigurationId != 0);

		SystemConfiguration result;

		result = this.systemConfigurationRepository.findOne(systemConfigurationId);
		Assert.notNull(result);

		return result;
	}

	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		SystemConfiguration result;

		result = this.systemConfigurationRepository.save(systemConfiguration);

		return result;
	}

	public void delete(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);
		Assert.isTrue(systemConfiguration.getId() != 0);
		Assert.isTrue(this.systemConfigurationRepository.exists(systemConfiguration.getId()));

		this.systemConfigurationRepository.delete(systemConfiguration);
	}

	// Other business methods
	public SystemConfiguration getConfiguration() {
		SystemConfiguration result;

		result = this.systemConfigurationRepository.getConfiguration();
		Assert.notNull(result);

		return result;
	}

	private Pattern patternSpamWords() {
		final Collection<String> spamWords = this.getConfiguration().getSpamWords();
		String pattern = "";
		for (final String sw : spamWords)
			pattern = pattern + (sw + "|");
		pattern = pattern.substring(0, pattern.length() - 1);
		pattern = pattern + "$";
		final Pattern result = Pattern.compile(pattern);
		return result;
	}

	// R24.2
	public Map<Actor, Boolean> mapActorSpammer() {
		final Map<Actor, Boolean> result = new HashMap<>();

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		Matcher spamWordsSubject, spamWordsBody;
		Integer hasSpamCount = 0;

		final Collection<Actor> actors = this.actorService.findAll();
		final Actor system = this.actorService.getSystemActor();
		final Actor deletedUser = this.actorService.getDeletedActor();
		actors.remove(system);
		actors.remove(deletedUser);

		for (final Actor a : actors) {
			boolean isSpammer = false;
			final Collection<Message> messagesActor = this.messageService.findMessagesSentByActorId(a.getId());
			for (final Message m : messagesActor) {
				spamWordsSubject = this.patternSpamWords().matcher(m.getSubject().toLowerCase());
				spamWordsBody = this.patternSpamWords().matcher(m.getBody().toLowerCase());
				if (spamWordsSubject.find() || spamWordsBody.find())
					hasSpamCount++;
			}
			final Integer sizeMessages = messagesActor.size();
			Double spamPorcentMessages = 0.0;
			if (sizeMessages != 0)
				spamPorcentMessages = (double) (hasSpamCount / sizeMessages) * 100;
			if (spamPorcentMessages >= 10)
				isSpammer = true;
			result.put(a, isSpammer);
		}
		return result;
	}

	// R24.2
	public void computeSpammers() {
		Boolean res = null;

		final Map<Actor, Boolean> spammerMap = this.mapActorSpammer();

		final Collection<Actor> actors = this.actorService.findAll();
		final Actor system = this.actorService.getSystemActor();
		final Actor deletedUser = this.actorService.getDeletedActor();
		actors.remove(system);
		actors.remove(deletedUser);

		for (final Actor a : actors) {
			res = spammerMap.get(a);
			a.setIsSpammer(res);
			this.actorService.saveAuxiliar(a);
		}
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SystemConfiguration reconstruct(final SystemConfiguration systemConfiguration, final BindingResult binding) {
		SystemConfiguration result;

		result = this.systemConfigurationRepository.findOne(systemConfiguration.getId());
		Assert.notNull(result, "This entity does not exist");
		result.setNameSystem(systemConfiguration.getNameSystem());
		result.setBannerUrl(systemConfiguration.getBannerUrl());
		result.setWelcomeMessageEnglish(systemConfiguration.getWelcomeMessageEnglish());
		result.setWelcomeMessageSpanish(systemConfiguration.getWelcomeMessageSpanish());
		result.setPhoneCountryCode(systemConfiguration.getPhoneCountryCode());
		result.setPeriodFinder(systemConfiguration.getPeriodFinder());
		result.setMaxResultsFinder(systemConfiguration.getMaxResultsFinder());
		result.setSpamWords(systemConfiguration.getSpamWords());

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.systemConfigurationRepository.flush();
	}

}
