
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.HackerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Application;
import domain.Curricula;
import domain.Finder;
import domain.Hacker;
import domain.Position;
import forms.HackerForm;

@Service
@Transactional
public class HackerService {

	// Managed repository
	@Autowired
	private HackerRepository	hackerRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CurriculaService	curriculaService;


	// Simple CRUD methods
	// R7.1
	public Hacker create() {

		Hacker result;

		result = new Hacker();
		final Collection<Application> applications = new HashSet<>();
		final Finder finder = this.finderService.create();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.HACKER);
		userAccount.addAuthority(auth);
		result.setApplications(applications);
		result.setFinder(finder);
		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Hacker> findAll() {
		Collection<Hacker> result;

		result = this.hackerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Hacker findOne(final int hackerId) {
		Assert.isTrue(hackerId != 0);
		Hacker result;

		result = this.hackerRepository.findOne(hackerId);
		Assert.notNull(result);

		return result;
	}

	// R7.1, R8.2
	public Hacker save(final Hacker hacker) {
		Assert.notNull(hacker);

		Hacker result;

		if (hacker.getId() == 0) {
			final Finder finder = this.finderService.save(hacker.getFinder());
			hacker.setFinder(finder);
		}

		result = (Hacker) this.actorService.save(hacker);
		result = this.hackerRepository.save(result);

		return result;
	}

	public void delete(final Hacker hacker) {
		Assert.notNull(hacker);
		Assert.isTrue(hacker.getId() != 0);
		Assert.isTrue(this.hackerRepository.exists(hacker.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hackerLogged = (Hacker) actorLogged;

		this.actorService.deleteEntities(hackerLogged);

		final Collection<Application> applications = new HashSet<>(hackerLogged.getApplications());
		for (final Application a : applications) {
			final Position p = a.getPosition();
			final Curricula c = a.getCurricula();
			p.getApplications().remove(a);
			hackerLogged.getApplications().remove(a);
			this.applicationService.delete(a);
			this.curriculaService.deleteAuxiliar(c);
		}

		final Collection<Curricula> curriculasHacker = this.curriculaService.findCurriculasByHackerLogged();
		for (final Curricula c : curriculasHacker)
			this.curriculaService.deleteAuxiliar(c);

		final Finder finder = hacker.getFinder();

		this.hackerRepository.flush();
		this.hackerRepository.delete(hacker);
		this.finderService.delete(finder);
	}

	// Other business methods
	public Hacker saveAuxiliar(final Hacker hacker) {
		Assert.notNull(hacker);

		Hacker result;

		result = this.hackerRepository.save(hacker);

		return result;
	}

	public Collection<Hacker> findHackersByPositionId(final int positionId) {
		Collection<Hacker> result;

		result = this.hackerRepository.findHackersByPositionId(positionId);

		return result;
	}

	public Collection<Hacker> findHackersByProblemId(final int problemId) {
		Collection<Hacker> result;

		result = this.hackerRepository.findHackersByProblemId(problemId);

		return result;
	}

	public Hacker findHackerByApplicationId(final int applicationId) {
		Hacker result;

		result = this.hackerRepository.findHackerByApplicationId(applicationId);

		return result;
	}

	public Hacker findHackerByCurriculaId(final int curriculaId) {
		Hacker result;

		result = this.hackerRepository.findHackerByCurriculaId(curriculaId);

		return result;
	}

	public Hacker findHackerByPersonalDataId(final int personalDataId) {
		Hacker result;

		result = this.hackerRepository.findHackerByPersonalDataId(personalDataId);

		return result;
	}

	public Hacker findHackerByPositionDataId(final int positionDataId) {
		Hacker result;

		result = this.hackerRepository.findHackerByPositionDataId(positionDataId);

		return result;
	}

	public Hacker findHackerByEducationDataId(final int educationDataId) {
		Hacker result;

		result = this.hackerRepository.findHackerByEducationDataId(educationDataId);

		return result;
	}

	public Hacker findHackerByMiscellaneousDataId(final int miscellaneousDataId) {
		Hacker result;

		result = this.hackerRepository.findHackerByMiscellaneousDataId(miscellaneousDataId);

		return result;
	}

	public Collection<Hacker> findHackersByFinderCriteria(final int positionId) {
		final Position positionCheck = this.positionService.findOne(positionId);
		final Collection<Hacker> result = new HashSet<>();
		final Collection<Hacker> hackers = this.hackerRepository.findAll();

		for (final Hacker h : hackers) {
			final Finder f = h.getFinder();
			final Calendar cal = Calendar.getInstance();
			Collection<Position> positionsFinder = new HashSet<>();
			if (f.getKeyWord() != null)
				if (!(f.getKeyWord().isEmpty() && f.getDeadline() == null && f.getMinSalary() == null && f.getMaxDeadline() == null)) {
					final String keyWord = f.getKeyWord().toLowerCase();
					final Date deadline = f.getDeadline();
					Double minSalary = f.getMinSalary();
					Date maxDeadline = f.getMaxDeadline();

					if (f.getMinSalary() == null)
						minSalary = 0.0;
					if (maxDeadline == null) {
						cal.set(3000, 0, 1);
						maxDeadline = cal.getTime();
					}
					positionsFinder = this.positionService.findPositionsFromFinder(keyWord, deadline, minSalary, maxDeadline);
				}

			if (positionsFinder.contains(positionCheck))
				result.add(h);
		}

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public HackerForm reconstruct(final HackerForm hackerForm, final BindingResult binding) {
		HackerForm result;
		final Hacker hacker = hackerForm.getActor();

		if (hacker.getId() == 0) {
			final Collection<Application> applications = new HashSet<>();
			final Finder finder = this.finderService.create();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.HACKER);
			userAccount.addAuthority(auth);
			userAccount.setUsername(hackerForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(hackerForm.getActor().getUserAccount().getPassword());
			hacker.setApplications(applications);
			hacker.setFinder(finder);
			hacker.setUserAccount(userAccount);
			hackerForm.setActor(hacker);
		} else {
			final Hacker res = this.hackerRepository.findOne(hacker.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(hacker.getName());
			res.setSurnames(hacker.getSurnames());
			res.setVATNumber(hacker.getVATNumber());
			res.setCreditCard(hacker.getCreditCard());
			res.setPhoto(hacker.getPhoto());
			res.setEmail(hacker.getEmail());
			res.setPhoneNumber(hacker.getPhoneNumber());
			res.setAddress(hacker.getAddress());
			hackerForm.setActor(res);
		}

		result = hackerForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.hackerRepository.flush();
	}

}
