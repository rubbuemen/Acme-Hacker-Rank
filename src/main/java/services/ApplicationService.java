
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Hacker;
import domain.Message;
import domain.Position;

@Service
@Transactional
public class ApplicationService {

	// Managed repository
	@Autowired
	private ApplicationRepository	applicationRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private ProblemService			problemService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private MessageService			messageService;


	// Simple CRUD methods
	// R10.1
	public Application create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		Application result;

		final Date moment = new Date(System.currentTimeMillis() - 1);

		result = new Application();
		result.setStatus("PENDING");
		result.setMoment(moment);
		result.setHacker((Hacker) actorLogged);

		return result;
	}

	public Collection<Application> findAll() {
		Collection<Application> result;

		result = this.applicationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Application findOne(final int applicationId) {
		Assert.isTrue(applicationId != 0);
		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Application save(final Application application) {
		Assert.notNull(application);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		Application result;

		final Date moment = new Date(System.currentTimeMillis() - 1);

		if (application.getId() == 0) {
			final Position positionApplication = application.getPosition();
			final Hacker hackerApplication = (Hacker) actorLogged;
			Assert.isTrue(this.positionService.findPositionsToSelectApplication().contains(positionApplication), "You have not selected a position really available to you");
			final Hacker hackerOwner = this.hackerService.findHackerByCurriculaId(application.getCurricula().getId());
			Assert.isTrue(actorLogged.equals(hackerOwner), "You are not the owner of this curricula");
			application.setCurricula(this.curriculaService.copyCurricula(application.getCurricula()));
			application.setMoment(moment);
			application.setHacker(hackerApplication);
			result = this.applicationRepository.save(application);
			positionApplication.getApplications().add(result);
			this.positionService.saveAuxiliar(positionApplication);
			hackerApplication.getApplications().add(result);
			this.hackerService.saveAuxiliar(hackerApplication);
		} else {
			final Hacker hackerOwner = this.hackerService.findHackerByApplicationId(application.getId());
			Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");
			application.setMomentSubmit(moment);
			application.setStatus("SUBMITTED");
			Assert.notNull(application.getExplications(), "You must provide explanations about the solution to the problem");
			Assert.isTrue(!application.getExplications().isEmpty(), "You must provide explanations about the solution to the problem");
			Assert.notNull(application.getCodeLink(), "You must provide a code link about the solution to the problem");
			Assert.isTrue(!application.getCodeLink().isEmpty(), "You must provide a code link about the solution to the problem");
			result = this.applicationRepository.save(application);
		}

		// R27
		final Company company = this.companyService.findCompanyByApplicationId(result.getId());
		final Hacker hacker = result.getHacker();
		final Message message = this.messageService.create();

		final Locale locale = LocaleContextHolder.getLocale();
		if (result.getStatus().equals("SUBMITTED")) {
			if (locale.getLanguage().equals("es")) {
				message.setSubject("Una nueva solicitud ha sido enviada");
				message.setBody("El hacker " + hacker.getName() + " " + hacker.getSurnames() + " ha enviado una solución al problema envuelto en la solicitud");
			} else {
				message.setSubject("A new application has been submitted");
				message.setBody("The hacker " + hacker.getName() + " " + hacker.getSurnames() + " has submitted a solution to the problem wrapped in the application");
			}
		} else if (locale.getLanguage().equals("es")) {
			message.setSubject("Una nueva solicitud está pendiente");
			message.setBody("El hacker " + hacker.getName() + " " + hacker.getSurnames() + " ha hecho una solicutud pendiente de resolver");
		} else {
			message.setSubject("A new application is pending");
			message.setBody("The hacker " + hacker.getName() + " " + hacker.getSurnames() + " has made a pending application to resolve");
		}

		final Actor sender = this.actorService.getSystemActor();
		message.setSender(sender);

		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(company);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

		return result;
	}

	public void delete(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		this.applicationRepository.delete(application);
	}

	// Other business methods
	public Collection<Application> findApplicationsPendingOrSubmittedByPositionId(final int positionId) {
		Collection<Application> result;

		result = this.applicationRepository.findApplicationsPendingOrSubmittedByPositionId(positionId);

		return result;
	}

	public Application saveAuxiliar(final Application application) {
		Assert.notNull(application);

		Application result;

		result = this.applicationRepository.save(application);

		return result;
	}

	// R9.3
	public Collection<Application> findApplicationsOrderByStatusByCompanyLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		Collection<Application> result;

		final Company companyLogged = (Company) actorLogged;

		result = this.applicationRepository.findApplicationsOrderByStatusByCompanyId(companyLogged.getId());
		Assert.notNull(result);

		return result;
	}

	// R9.3
	public Application acceptApplication(final Application application) {
		Application result;
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByApplicationId(application.getId());
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(application.getStatus().equals("SUBMITTED"), "The status of this application is not 'submitted'");
		application.setStatus("ACCEPTED");

		result = this.applicationRepository.save(application);

		this.positionService.changeCancelled(result.getPosition());

		// R27
		final Company company = this.companyService.findCompanyByApplicationId(result.getId());
		final Hacker hacker = result.getHacker();
		final Message message = this.messageService.create();

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Una solicitud ha cambiado de estado");
			message.setBody("La empresa " + company.getCommercialName() + " ha cambiado el estado de la solicitud cuyo momento de creación fue " + result.getMoment() + " a " + result.getStatus());
		} else {
			message.setSubject("An application changed status");
			message.setBody("The company " + company.getCommercialName() + " has changed the status of the application whose moment was " + result.getMoment() + " to " + result.getStatus());
		}

		final Actor sender = this.actorService.getSystemActor();
		message.setSender(sender);

		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(hacker);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

		return result;
	}

	// R9.3
	public Application rejectApplication(final Application application) {
		Application result;
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByApplicationId(application.getId());
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(application.getStatus().equals("SUBMITTED"), "The status of this application is not 'submitted'");
		application.setStatus("REJECTED");

		result = this.applicationRepository.save(application);

		// R27
		final Company company = this.companyService.findCompanyByApplicationId(result.getId());
		final Hacker hacker = result.getHacker();
		final Message message = this.messageService.create();

		final Locale locale = LocaleContextHolder.getLocale();
		if (locale.getLanguage().equals("es")) {
			message.setSubject("Una solicitud ha cambiado de estado");
			message.setBody("La empresa " + company.getCommercialName() + " ha cambiado el estado de la solicitud cuyo momento de creación fue " + result.getMoment() + " a " + result.getStatus());
		} else {
			message.setSubject("An application changed status");
			message.setBody("The company " + company.getCommercialName() + " has changed the status of the application whose moment was " + result.getMoment() + " to " + result.getStatus());
		}

		final Actor sender = this.actorService.getSystemActor();
		message.setSender(sender);

		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(hacker);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

		return result;
	}

	public Application findApplicationCompanyLogged(final int applicationId) {
		Assert.isTrue(applicationId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByApplicationId(applicationId);
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		return result;
	}

	public Application findApplicationHackerLogged(final int applicationId) {
		Assert.isTrue(applicationId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hackerOwner = this.hackerService.findHackerByApplicationId(applicationId);
		Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");

		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Collection<Application> findApplicationsOrderByStatusByHackerLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		Collection<Application> result;

		final Hacker hackerLogged = (Hacker) actorLogged;

		result = this.applicationRepository.findApplicationsOrderByStatusByHackerId(hackerLogged.getId());
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Application reconstruct(final Application application, final BindingResult binding) {
		Application result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		if (application.getId() == 0) {
			application.setMoment(moment);
			application.setStatus("PENDING");
			application.setHacker((Hacker) actorLogged);
			if (application.getPosition() != null)
				application.setProblem(this.problemService.getRandomProblemByPosition(application.getPosition()));
			result = application;
		} else {
			result = this.applicationRepository.findOne(application.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setExplications(application.getExplications());
			result.setCodeLink(application.getCodeLink());
		}
		this.validator.validate(result, binding);

		return result;
	}
	public void flush() {
		this.applicationRepository.flush();
	}

}
