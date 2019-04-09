
package services;

import java.util.Collection;
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
import domain.Application;
import domain.Finder;
import domain.Hacker;
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

		this.hackerRepository.delete(hacker);
	}


	// Other business methods

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
