
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Finder;
import domain.Hacker;
import domain.Position;

@Service
@Transactional
public class FinderService {

	// Managed repository
	@Autowired
	private FinderRepository			finderRepository;

	// Supporting services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private PositionService				positionService;


	// Simple CRUD methods
	public Finder create() {
		Finder result;

		result = new Finder();

		final Collection<Position> positions = new HashSet<>();

		result.setPositions(positions);

		return result;
	}

	public Collection<Finder> findAll() {
		Collection<Finder> result;

		result = this.finderRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Finder findOne(final int finderId) {
		Assert.isTrue(finderId != 0);

		Finder result;

		result = this.finderRepository.findOne(finderId);
		Assert.notNull(result);

		return result;
	}

	public Finder save(final Finder finder) {
		Assert.notNull(finder);

		Finder result;

		if (finder.getId() != 0) {
			final Date searchMoment = new Date(System.currentTimeMillis() - 1);
			finder.setSearchMoment(searchMoment);
		}

		result = this.finderRepository.save(finder);

		return result;
	}

	public void delete(final Finder finder) {
		Assert.notNull(finder);
		Assert.isTrue(finder.getId() != 0);
		Assert.isTrue(this.finderRepository.exists(finder.getId()));

		this.finderRepository.delete(finder);
	}

	// Other business methods
	public Collection<Finder> findFindersByPosition(final Position position) {
		Collection<Finder> result;

		result = this.finderRepository.findFindersByPosition(position);
		Assert.notNull(result);

		return result;
	}

	public Finder findFinderHackerLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hackerLogged = (Hacker) actorLogged;

		Finder result;

		result = hackerLogged.getFinder();

		return result;
	}

	// R17.1
	public Finder cleanFinder(final Finder finder) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		Finder result;

		Collection<Position> positions = this.positionService.findPositionsFinalModeNotCancelledNotDeadline();
		final Date searchMoment = new Date(System.currentTimeMillis() - 1);
		finder.setKeyWord("");
		finder.setDeadline(null);
		finder.setMinSalary(null);
		finder.setMaxDeadline(null);

		final Integer maxResults = this.systemConfigurationService.getConfiguration().getMaxResultsFinder();

		// R20
		if (positions.size() > maxResults) {
			final List<Position> resultList = new ArrayList<>(positions);
			positions = new HashSet<>(resultList.subList(0, maxResults));
		}

		finder.setPositions(positions);
		finder.setSearchMoment(searchMoment);
		result = this.finderRepository.save(finder);

		return result;
	}

	// R19
	public boolean cleanCache(final Finder finder) {
		Assert.notNull(finder);

		Long searchMoment, nowMomment;
		Boolean result = false;
		final Date dateMoment = finder.getSearchMoment();
		if (dateMoment != null) {
			searchMoment = finder.getSearchMoment().getTime();
			nowMomment = new Date(System.currentTimeMillis()).getTime();
			Long passedTime = (nowMomment - searchMoment) / 1000; //De milisegundos a segundos
			passedTime = passedTime / 3600; //De segundos a horas
			final Integer periodFinder = this.systemConfigurationService.getConfiguration().getPeriodFinder();
			result = passedTime >= periodFinder;
		}

		return result;
	}

	// R17.2
	public void updateCriteria(String keyWord, final Date deadline, Double minSalary, Date maxDeadline) {
		Finder result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hacker = (Hacker) actorLogged;

		result = hacker.getFinder();

		final Calendar cal = Calendar.getInstance();

		if (keyWord.isEmpty() && deadline == null && minSalary == null && maxDeadline == null)
			result = this.cleanFinder(result);
		else {
			keyWord = keyWord.toLowerCase();

			if (keyWord != "")
				result.setKeyWord(keyWord);
			if (deadline == null)
				result.setDeadline(deadline);
			if (minSalary == null) {
				minSalary = 0.0;
				result.setMinSalary(0.0);
			}
			if (maxDeadline == null) {
				cal.set(3000, 0, 1);
				maxDeadline = cal.getTime();
				result.setMaxDeadline(maxDeadline);
			}

			Collection<Position> positionsFinder = this.positionService.findPositionsFromFinder(keyWord, deadline, minSalary, maxDeadline);

			final Integer maxResults = this.systemConfigurationService.getConfiguration().getMaxResultsFinder();

			// R20
			if (positionsFinder.size() > maxResults) {
				final List<Position> resultList = new ArrayList<>(positionsFinder);
				positionsFinder = new HashSet<>(resultList.subList(0, maxResults));
			}

			result.setPositions(positionsFinder);

			cal.setTime(result.getMaxDeadline());
			if (cal.get(Calendar.YEAR) == 3000)
				result.setMaxDeadline(null);
			if (result.getMinSalary() == 0.0)
				result.setMinSalary(null);
		}

		final Date searchMoment = new Date(System.currentTimeMillis() - 1);
		result.setSearchMoment(searchMoment);

		result = this.finderRepository.save(result);
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Finder reconstruct(final Finder finder, final BindingResult binding) {
		Finder result;

		if (finder.getId() == 0)
			result = finder;
		else {
			result = this.finderRepository.findOne(finder.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setKeyWord(finder.getKeyWord());
			result.setDeadline(finder.getDeadline());
			result.setMinSalary(finder.getMinSalary());
			result.setMaxDeadline(finder.getMaxDeadline());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.finderRepository.flush();
	}

}
