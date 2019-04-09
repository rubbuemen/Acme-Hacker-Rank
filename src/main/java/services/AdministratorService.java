
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import forms.AdministratorForm;

@Service
@Transactional
public class AdministratorService {

	// Managed repository
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;


	// Simple CRUD methods
	// R11.1
	public Administrator create() {
		Administrator result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = new Administrator();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.ADMIN);
		userAccount.addAuthority(auth);
		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Administrator findOne(final int administratorId) {
		Assert.isTrue(administratorId != 0);
		Administrator result;

		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);

		return result;
	}

	// R8.2, R11.1
	public Administrator save(final Administrator administrator) {
		Assert.notNull(administrator);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		Administrator result;

		result = (Administrator) this.actorService.save(administrator);
		result = this.administratorRepository.save(result);

		return result;
	}

	public void delete(final Administrator administrator) {
		Assert.notNull(administrator);
		Assert.isTrue(administrator.getId() != 0);
		Assert.isTrue(this.administratorRepository.exists(administrator.getId()));

		this.administratorRepository.delete(administrator);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public AdministratorForm reconstruct(final AdministratorForm administratorForm, final BindingResult binding) {
		AdministratorForm result;
		final Administrator administrator = administratorForm.getActor();

		if (administrator.getId() == 0) {
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.ADMIN);
			userAccount.addAuthority(auth);
			userAccount.setUsername(administratorForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(administratorForm.getActor().getUserAccount().getPassword());
			administrator.setUserAccount(userAccount);
			administratorForm.setActor(administrator);
		} else {
			final Administrator res = this.administratorRepository.findOne(administrator.getId());
			res.setName(administrator.getName());
			res.setSurnames(administrator.getSurnames());
			res.setVATNumber(administrator.getVATNumber());
			res.setCreditCard(administrator.getCreditCard());
			res.setPhoto(administrator.getPhoto());
			res.setEmail(administrator.getEmail());
			res.setPhoneNumber(administrator.getPhoneNumber());
			res.setAddress(administrator.getAddress());
			administratorForm.setActor(res);
		}

		result = administratorForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.administratorRepository.flush();
	}

}
