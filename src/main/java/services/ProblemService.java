
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProblemRepository;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	// Managed repository
	@Autowired
	private ProblemRepository	problemRepository;


	// Supporting services

	// Simple CRUD methods
	public Problem create() {

		Problem result;

		result = new Problem();

		return result;
	}

	public Collection<Problem> findAll() {
		Collection<Problem> result;

		result = this.problemRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Problem findOne(final int problemId) {
		Assert.isTrue(problemId != 0);
		Problem result;

		result = this.problemRepository.findOne(problemId);
		Assert.notNull(result);

		return result;
	}

	public Problem save(final Problem problem) {
		Assert.notNull(problem);

		Problem result;

		if (problem.getId() == 0)
			result = this.problemRepository.save(problem);
		else
			result = this.problemRepository.save(problem);

		return result;
	}

	public void delete(final Problem problem) {
		Assert.notNull(problem);
		Assert.isTrue(problem.getId() != 0);
		Assert.isTrue(this.problemRepository.exists(problem.getId()));

		this.problemRepository.delete(problem);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Problem reconstruct(final Problem problem, final BindingResult binding) {
		Problem result;

		if (problem.getId() == 0)
			result = problem;
		else {
			result = this.problemRepository.findOne(problem.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.problemRepository.flush();
	}

}
