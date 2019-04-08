
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CurriculaRepository;
import domain.Curricula;

@Service
@Transactional
public class CurriculaService {

	// Managed repository
	@Autowired
	private CurriculaRepository	curriculaRepository;


	// Supporting services

	// Simple CRUD methods
	public Curricula create() {

		Curricula result;

		result = new Curricula();

		return result;
	}

	public Collection<Curricula> findAll() {
		Collection<Curricula> result;

		result = this.curriculaRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Curricula findOne(final int curriculaId) {
		Assert.isTrue(curriculaId != 0);
		Curricula result;

		result = this.curriculaRepository.findOne(curriculaId);
		Assert.notNull(result);

		return result;
	}

	public Curricula save(final Curricula curricula) {
		Assert.notNull(curricula);

		Curricula result;

		if (curricula.getId() == 0)
			result = this.curriculaRepository.save(curricula);
		else
			result = this.curriculaRepository.save(curricula);

		return result;
	}

	public void delete(final Curricula curricula) {
		Assert.notNull(curricula);
		Assert.isTrue(curricula.getId() != 0);
		Assert.isTrue(this.curriculaRepository.exists(curricula.getId()));

		this.curriculaRepository.delete(curricula);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Curricula reconstruct(final Curricula curricula, final BindingResult binding) {
		Curricula result;

		if (curricula.getId() == 0)
			result = curricula;
		else {
			result = this.curriculaRepository.findOne(curricula.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.curriculaRepository.flush();
	}

}
