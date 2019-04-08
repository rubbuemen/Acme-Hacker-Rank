
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed repository
	@Autowired
	private AdministratorRepository	administratorRepository;


	// Supporting services

	// Simple CRUD methods
	public Administrator create() {

		Administrator result;

		result = new Administrator();

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

	public Administrator save(final Administrator administrator) {
		Assert.notNull(administrator);

		Administrator result;

		if (administrator.getId() == 0)
			result = this.administratorRepository.save(administrator);
		else
			result = this.administratorRepository.save(administrator);

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


	public Administrator reconstruct(final Administrator administrator, final BindingResult binding) {
		Administrator result;

		if (administrator.getId() == 0)
			result = administrator;
		else {
			result = this.administratorRepository.findOne(administrator.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.administratorRepository.flush();
	}

}
