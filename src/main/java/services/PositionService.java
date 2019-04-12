
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Finder;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class PositionService {

	// Managed repository
	@Autowired
	private PositionRepository	positionRepository;

	// Supporting services
	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private FinderService		finderService;


	// Simple CRUD methods
	//R9.1
	public Position create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		Position result;

		result = new Position();
		final Collection<Problem> problems = new HashSet<>();
		final Collection<Application> applications = new HashSet<>();

		// R4
		final String ticker = ""; //Ticker will be generated in reconstruct method

		result.setProblems(problems);
		result.setApplications(applications);
		result.setTicker(ticker);
		result.setIsFinalMode(false);
		result.setIsCancelled(false);

		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> result;

		result = this.positionRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Position findOne(final int positionId) {
		Assert.isTrue(positionId != 0);
		Position result;

		result = this.positionRepository.findOne(positionId);
		Assert.notNull(result);

		return result;
	}

	//R9.1
	public Position save(final Position position) {
		Assert.notNull(position);

		Position result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyLogged = (Company) actorLogged;

		Assert.isTrue(!position.getIsFinalMode(), "You can only save positions that are not in final mode");

		final Collection<Problem> problemsPosition = position.getProblems();
		for (final Problem pro : problemsPosition) {
			final Company companyOwnerProblem = this.companyService.findCompanyByProblemId(pro.getId());
			Assert.isTrue(actorLogged.equals(companyOwnerProblem), "The logged company is not the owner of this problem");
		}

		if (position.getId() == 0) {
			result = this.positionRepository.save(position);
			final Collection<Position> positionsCompanyLogged = companyLogged.getPositions();
			positionsCompanyLogged.add(result);
			companyLogged.setPositions(positionsCompanyLogged);
			this.companyService.save(companyLogged);

		} else {
			final Company companyOwner = this.companyService.findCompanyByPositionId(position.getId());
			Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");
			result = this.positionRepository.save(position);
		}

		return result;
	}

	//R9.1
	public void delete(final Position position) {
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		Assert.isTrue(this.positionRepository.exists(position.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByPositionId(position.getId());
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!position.getIsFinalMode(), "You can only delete positions that are not in final mode");

		final Company companyLogged = (Company) actorLogged;

		final Collection<Position> positionsActorLogged = companyLogged.getPositions();
		positionsActorLogged.remove(position);
		companyLogged.setPositions(positionsActorLogged);
		this.companyService.save(companyLogged);

		this.positionRepository.delete(position);
	}

	public Position saveAuxiliar(final Position position) {
		Assert.notNull(position);

		Position result;

		result = this.positionRepository.save(position);

		return result;
	}

	// Other business methods
	//R7.2
	public Collection<Position> findPositionsFinalModeNotCancelledNotDeadline() {
		Collection<Position> result;

		result = this.positionRepository.findPositionsFinalModeNotCancelledNotDeadline();
		Assert.notNull(result);

		return result;
	}

	public Map<Position, Company> getMapPositionCompany(final Collection<Position> positions) {
		final Map<Position, Company> result = new HashMap<>();

		if (positions != null)
			for (final Position p : positions) {
				final Company company = this.companyService.findCompanyByPositionId(p.getId());
				result.put(p, company);
			}

		return result;
	}

	public Collection<Position> findPositionsFinalModeNotCancelledNotDeadlineByCompanyId(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findPositionsFinalModeNotCancelledNotDeadlineByCompanyId(companyId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Position> findPositionsFinalModeNotCancelledNotDeadlineBySingleKeyWord(final String singleKeyWord) {
		Collection<Position> result;

		result = this.positionRepository.findPositionsFinalModeNotCancelledNotDeadlineBySingleKeyWord(singleKeyWord);
		Assert.notNull(result);

		return result;
	}

	//R9.1
	public Collection<Position> findPositionsByCompanyLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		Collection<Position> result;

		final Company companyLogged = (Company) actorLogged;

		result = this.positionRepository.findPositionsByCompanyId(companyLogged.getId());
		Assert.notNull(result);

		return result;
	}

	//R9.1
	public Position changeFinalMode(final Position position) {
		Position result;
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		Assert.isTrue(this.positionRepository.exists(position.getId()));

		Assert.isTrue(!position.getIsFinalMode(), "This position is already in final mode");
		Assert.isTrue(position.getProblems().size() >= 2, "A position cannot be saved in final mode unless there are at least two problems associated with it");
		position.setIsFinalMode(true);

		result = this.positionRepository.save(position);

		return result;
	}

	//R9.1
	public Position changeCancelled(final Position position) {
		Position result;
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		Assert.isTrue(this.positionRepository.exists(position.getId()));

		Assert.isTrue(position.getIsFinalMode(), "To cancel a position, it must be in final mode");
		Assert.isTrue(!position.getIsCancelled(), "This position is already cancelled");

		final Collection<Application> applicationsPendingOrSubmitted = this.applicationService.findApplicationsPendingOrSubmittedByPositionId(position.getId());
		position.getApplications().removeAll(applicationsPendingOrSubmitted);
		for (final Application a : applicationsPendingOrSubmitted) {
			a.setStatus("REJECTED");
			final Application application = this.applicationService.saveAuxiliar(a);
			position.getApplications().add(application);
		}
		position.setIsCancelled(true);

		result = this.positionRepository.save(position);

		final Collection<Finder> finders = this.finderService.findFindersByPosition(result);
		for (final Finder f : finders) {
			f.getPositions().remove(result);
			this.finderService.save(f);
		}

		return result;
	}

	// R4
	public String generateTicker() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final String commercialName = ((Company) actorLogged).getCommercialName();
		String result = "";
		while (true) {
			result = "";
			if (commercialName.length() < 4) {
				result = commercialName.toUpperCase();
				final int numberX = 4 - commercialName.length();
				for (int i = 0; i < numberX; i++)
					result = result + "X";
			} else
				result = commercialName.substring(0, 4).toUpperCase();
			result = result + "-" + RandomStringUtils.randomNumeric(4);

			final Position pos = this.positionRepository.findPositionByTicker(result);
			if (pos == null)
				break;
		}

		return result;
	}

	public Position findPositionCompanyLogged(final int positionId) {
		Assert.isTrue(positionId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Company companyOwner = this.companyService.findCompanyByPositionId(positionId);
		Assert.isTrue(actorLogged.equals(companyOwner), "The logged actor is not the owner of this entity");

		Position result;

		result = this.positionRepository.findOne(positionId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Position> findPositionsToSelectApplication() {
		Collection<Position> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		result = this.findPositionsFinalModeNotCancelledNotDeadline();
		final Collection<Position> positionsToRemove = this.positionRepository.findPositionsApplicationsNotRejectedByHackerId(actorLogged.getId());
		result.removeAll(positionsToRemove);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Position reconstruct(final Position position, final BindingResult binding) {
		Position result;

		if (position.getProblems() == null || position.getProblems().contains(null)) {
			final Collection<Problem> problems = new HashSet<>();
			position.setProblems(problems);
		}

		if (position.getId() == 0) {
			final Collection<Application> applications = new HashSet<>();
			position.setApplications(applications);
			position.setTicker(this.generateTicker());
			position.setIsFinalMode(false);
			position.setIsCancelled(false);
			result = position;
		}

		else {
			result = this.positionRepository.findOne(position.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(position.getTitle());
			result.setDescription(position.getDescription());
			result.setDeadline(position.getDeadline());
			result.setSkills(position.getSkills());
			result.setTechnologies(position.getTechnologies());
			result.setSalary(position.getSalary());
			result.setProfile(position.getProfile());
			result.setProblems(position.getProblems());
		}

		this.validator.validate(result, binding);

		this.positionRepository.flush();

		return result;
	}

	public void flush() {
		this.positionRepository.flush();
	}

}
