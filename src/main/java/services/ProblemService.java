
package services;

import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProblemRepository;
import domain.Actor;
import domain.Company;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	// Managed repository
	@Autowired
	private ProblemRepository	problemRepository;

	// Supporting services
	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods
	// R9.2
	public Problem create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		Problem result;

		result = new Problem();
		result.setIsFinalMode(false);

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

	// R9.2
	public Problem save(final Problem problem) {
		Assert.notNull(problem);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyLogged = (Company) actorLogged;

		Assert.isTrue(!problem.getIsFinalMode(), "You can only save problems that are not in final mode");

		Problem result;

		if (problem.getId() == 0) {
			result = this.problemRepository.save(problem);
			final Collection<Problem> problemsCompanyLogged = companyLogged.getProblems();
			problemsCompanyLogged.add(result);
			companyLogged.setProblems(problemsCompanyLogged);
			this.companyService.save(companyLogged);
		} else {
			final Company companyOwner = this.companyService.findCompanyByProblemId(problem.getId());
			Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");
			result = this.problemRepository.save(problem);
		}

		return result;
	}

	// R9.2
	public void delete(final Problem problem) {
		Assert.notNull(problem);
		Assert.isTrue(problem.getId() != 0);
		Assert.isTrue(this.problemRepository.exists(problem.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByProblemId(problem.getId());
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!problem.getIsFinalMode(), "You can only delete problems that are not in final mode");

		final Company companyLogged = (Company) actorLogged;

		final Collection<Problem> problemsActorLogged = companyLogged.getProblems();
		problemsActorLogged.remove(problem);
		companyLogged.setProblems(problemsActorLogged);
		this.companyService.save(companyLogged);

		this.problemRepository.delete(problem);
	}

	// Other business methods

	public Collection<Problem> findProblemsFinalModeByCompanyLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		Collection<Problem> result;

		final Company companyLogged = (Company) actorLogged;

		result = this.problemRepository.findProblemsFinalModeByCompanyId(companyLogged.getId());
		Assert.notNull(result);

		return result;
	}

	// R9.2
	public Collection<Problem> findProblemsByCompanyLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		Collection<Problem> result;

		final Company companyLogged = (Company) actorLogged;

		result = this.problemRepository.findProblemsByCompanyId(companyLogged.getId());
		Assert.notNull(result);

		return result;
	}

	//R9.2
	public Problem changeFinalMode(final Problem problem) {
		Problem result;
		Assert.notNull(problem);
		Assert.isTrue(problem.getId() != 0);
		Assert.isTrue(this.problemRepository.exists(problem.getId()));

		Assert.isTrue(!problem.getIsFinalMode(), "This problem is already in final mode");
		problem.setIsFinalMode(true);

		result = this.problemRepository.save(problem);

		return result;
	}

	public Problem findProblemCompanyLogged(final int problemId) {

		Assert.isTrue(problemId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByProblemId(problemId);
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Problem result;

		result = this.problemRepository.findOne(problemId);
		Assert.notNull(result);

		return result;
	}

	//R10.1
	public Problem getRandomProblemByPosition(final Position position) {

		Problem result;

		final Random r = new Random();
		final Collection<Problem> problems = position.getProblems();

		final int i = r.nextInt(problems.size());
		result = (Problem) problems.toArray()[i];

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Problem reconstruct(final Problem problem, final BindingResult binding) {
		Problem result;

		if (problem.getId() == 0) {
			problem.setIsFinalMode(false);
			result = problem;
		} else {
			result = this.problemRepository.findOne(problem.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(problem.getTitle());
			result.setStatement(problem.getStatement());
			result.setAttachments(problem.getAttachments());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.problemRepository.flush();
	}

}
