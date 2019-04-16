
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Company;
import domain.CreditCard;

@Service
@Transactional
public class ActorService {

	// Managed repository
	@Autowired
	private ActorRepository				actorRepository;

	// Supporting services
	@Autowired
	private UserAccountService			userAccountService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private MessageService				messageService;

	@Autowired
	private SocialProfileService		socialProfileService;


	// Simple CRUD methods
	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);
		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	// R7.1, R8.2
	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		Assert.isTrue(actor.getUserAccount().getStatusAccount());

		if (!actor.getCreditCard().getNumber().isEmpty())
			Assert.isTrue(this.isNumeric(actor.getCreditCard().getNumber()), "Invalid credit card");
		if (actor.getCreditCard().getExpirationYear() != null && actor.getCreditCard().getExpirationMonth() != null && actor.getCreditCard().getExpirationYear() >= 0)
			Assert.isTrue(this.checkCreditCard(actor.getCreditCard()), "Expired credit card");

		if (actor.getId() == 0) {
			final UserAccount userAccount = this.userAccountService.save(actor.getUserAccount());
			actor.setUserAccount(userAccount);
		} else {
			final Actor actorLogged = this.findActorLogged();
			Assert.notNull(actorLogged);
			Assert.isTrue(actor.equals(actorLogged));
		}

		if (actor.getPhoneNumber() != null) {
			String phoneNumber = actor.getPhoneNumber();
			final String phoneCountryCode = this.systemConfigurationService.getConfiguration().getPhoneCountryCode();
			if (!actor.getPhoneNumber().isEmpty() && !actor.getPhoneNumber().startsWith("+"))
				phoneNumber = phoneCountryCode + " " + phoneNumber;
			actor.setPhoneNumber(phoneNumber);
		}

		result = this.actorRepository.save(actor);

		return result;
	}

	public Actor saveAuxiliar(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		result = this.actorRepository.save(actor);

		return result;
	}

	public void deleteEntities(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);

		this.messageService.deleteActorFromRecipientsMessage();
		this.messageService.deleteActorFromSenderMessage();
		this.socialProfileService.deleteSocialProfiles();

	}

	// Other business methods
	public Actor findActorLogged() {
		Actor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		result = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Actor getSystemActor() {
		Actor result;

		result = this.actorRepository.getSystemActor();
		Assert.notNull(result);

		return result;
	}

	public Actor getDeletedActor() {
		Actor result;

		result = this.actorRepository.getDeletedActor();
		Assert.notNull(result);

		return result;
	}

	public void checkUserLoginCompany(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.COMPANY);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a company");
	}

	public void checkUserLoginHacker(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.HACKER);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a hacker");
	}

	public void checkUserLoginAdministrator(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a administrator");
	}

	public boolean isNumeric(final String cadena) {

		boolean resultado;

		try {
			Long.parseLong(cadena);
			resultado = true;
		} catch (final NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	public boolean checkCreditCard(final CreditCard creditCard) {
		boolean result;
		Calendar calendar;
		int actualYear, actualMonth;

		result = false;
		calendar = new GregorianCalendar();
		actualYear = calendar.get(Calendar.YEAR);
		actualMonth = calendar.get(Calendar.MONTH) + 1;
		actualYear = actualYear % 100;
		if (creditCard.getExpirationYear() > actualYear)
			result = true;
		else if (creditCard.getExpirationYear() == actualYear && creditCard.getExpirationMonth() >= actualMonth)
			result = true;
		return result;
	}

	public Actor findActorBySocialProfileId(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);

		Actor result;

		result = this.actorRepository.findActorBySocialProfileId(socialProfileId);
		return result;
	}

	public Actor findActorByMessageId(final int messageId) {
		Assert.isTrue(messageId != 0);

		Actor result;

		result = this.actorRepository.findActorByMessageId(messageId);
		return result;
	}

	public Collection<Actor> findAllActorsExceptLogged() {
		Collection<Actor> result;

		final Actor actorLogged = this.findActorLogged();
		final Actor systemActor = this.actorRepository.getSystemActor();
		final Actor deletedActor = this.actorRepository.getDeletedActor();
		result = this.actorRepository.findAll();
		result.remove(actorLogged);
		result.remove(systemActor);
		result.remove(deletedActor);

		return result;
	}

	public Collection<Actor> findActorsToBan() {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		Collection<Actor> result;

		result = this.actorRepository.findActorsToBan();
		return result;
	}

	public Collection<Actor> findActorsBanned() {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		Collection<Actor> result;

		result = this.actorRepository.findActorsBanned();
		return result;
	}

	public void banActor(final Actor actor) {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		final UserAccount userAccount = actor.getUserAccount();
		userAccount.setStatusAccount(false);
		this.userAccountService.save(userAccount);
	}

	public void unbanActor(final Actor actor) {
		final Actor actorLogged = this.findActorLogged();
		Assert.notNull(actorLogged);
		this.checkUserLoginAdministrator(actorLogged);

		final UserAccount userAccount = actor.getUserAccount();
		userAccount.setStatusAccount(true);
		this.userAccountService.save(userAccount);
	}

	public StringBuilder exportData() {
		final Actor actorLogged = this.findActorLogged();

		final StringBuilder sb = new StringBuilder();
		sb.append(actorLogged.getName());
		sb.append(';');
		sb.append(actorLogged.getSurnames());
		sb.append(';');
		sb.append(actorLogged.getVATNumber());
		sb.append(';');
		sb.append(actorLogged.getCreditCard().getHolder());
		sb.append(';');
		sb.append(actorLogged.getCreditCard().getMake());
		sb.append(';');
		sb.append(actorLogged.getCreditCard().getNumber());
		sb.append(';');
		sb.append(actorLogged.getCreditCard().getExpirationMonth());
		sb.append(';');
		sb.append(actorLogged.getCreditCard().getExpirationYear());
		sb.append(';');
		sb.append(actorLogged.getCreditCard().getCvv());
		sb.append(';');
		sb.append(actorLogged.getEmail());
		sb.append(';');
		sb.append(actorLogged.getPhoneNumber());
		sb.append(';');
		sb.append(actorLogged.getAddress());
		sb.append(';');
		sb.append(actorLogged.getPhoto());
		if (actorLogged instanceof Company) {
			sb.append(';');
			sb.append(((Company) actorLogged).getCommercialName());
		}
		sb.append('\n');
		return sb;
	}
}
