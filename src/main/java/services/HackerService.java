
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.HackerRepository;
import domain.Hacker;

@Service
@Transactional
public class HackerService {

	// Managed repository
	@Autowired
	private HackerRepository	hackerRepository;


	// Supporting services

	// Simple CRUD methods
	public Hacker create() {

		Hacker result;

		result = new Hacker();

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

	public Hacker save(final Hacker hacker) {
		Assert.notNull(hacker);

		Hacker result;

		if (hacker.getId() == 0)
			result = this.hackerRepository.save(hacker);
		else
			result = this.hackerRepository.save(hacker);

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


	public Hacker reconstruct(final Hacker hacker, final BindingResult binding) {
		Hacker result;

		if (hacker.getId() == 0)
			result = hacker;
		else {
			result = this.hackerRepository.findOne(hacker.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.hackerRepository.flush();
	}

}
