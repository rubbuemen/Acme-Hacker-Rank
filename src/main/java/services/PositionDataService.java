
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionDataRepository;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PositionData;

@Service
@Transactional
public class PositionDataService {

	// Managed repository
	@Autowired
	private PositionDataRepository	positionDataRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private CurriculaService		curriculaService;


	// Simple CRUD methods
	// R17.1
	public PositionData create() {

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		PositionData result;

		result = new PositionData();

		return result;
	}

	public Collection<PositionData> findAll() {
		Collection<PositionData> result;

		result = this.positionDataRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public PositionData findOne(final int positionDataId) {
		Assert.isTrue(positionDataId != 0);
		PositionData result;

		result = this.positionDataRepository.findOne(positionDataId);
		Assert.notNull(result);

		return result;
	}

	// R17.1
	public PositionData save(final PositionData positionData, final Curricula curricula) {
		Assert.notNull(positionData);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		PositionData result;

		final Date startDate = positionData.getStartDate();
		final Date endDate = positionData.getEndDate();

		if (startDate != null && endDate != null)
			Assert.isTrue(startDate.before(endDate), "Start date must be before end date");

		if (positionData.getId() == 0) {
			result = this.positionDataRepository.save(positionData);
			final Collection<PositionData> positionDatasCurricula = curricula.getPositionDatas();
			positionDatasCurricula.add(result);
			curricula.setPositionDatas(positionDatasCurricula);
			this.curriculaService.saveAuxiliar(curricula);
		} else {
			final Hacker hackerOwner = this.hackerService.findHackerByCurriculaId(curricula.getId());
			Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");
			final Hacker hackerOwner2 = this.hackerService.findHackerByPositionDataId(positionData.getId());
			Assert.isTrue(actorLogged.equals(hackerOwner2), "The logged actor is not the owner of this entity");
			final Curricula cur = this.curriculaService.findCurriculaByPositionDataId(positionData.getId());
			Assert.isTrue(!cur.getIsCopy(), "You can not edit a copy of your curricula");
			result = this.positionDataRepository.save(positionData);
		}

		return result;
	}
	public PositionData saveAuxiliar(final PositionData positionData) {
		Assert.notNull(positionData);
		PositionData result;

		result = this.positionDataRepository.save(positionData);

		return result;
	}

	// R17.1
	public void delete(final PositionData positionData) {
		Assert.notNull(positionData);
		Assert.isTrue(positionData.getId() != 0);
		Assert.isTrue(this.positionDataRepository.exists(positionData.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Curricula curricula = this.curriculaService.findCurriculaByPositionDataId(positionData.getId());
		Assert.isTrue(!curricula.getIsCopy(), "You can not edit a copy of your curricula");

		final Collection<PositionData> positionDatasCurricula = curricula.getPositionDatas();
		positionDatasCurricula.remove(positionData);
		this.curriculaService.saveAuxiliar(curricula);

		this.positionDataRepository.delete(positionData);
	}

	// Other business methods
	public PositionData findPositionDataHackerLogged(final int positionDataId) {
		Assert.isTrue(positionDataId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hackerOwner = this.hackerService.findHackerByPositionDataId(positionDataId);
		Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");

		PositionData result;

		result = this.positionDataRepository.findOne(positionDataId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public PositionData reconstruct(final PositionData positionData, final BindingResult binding) {
		PositionData result;

		if (positionData.getId() == 0)
			result = positionData;
		else {
			result = this.positionDataRepository.findOne(positionData.getId());
			Assert.notNull(result, "This entity does not exist");
			result.setTitle(positionData.getTitle());
			result.setDescription(positionData.getDescription());
			result.setStartDate(positionData.getStartDate());
			result.setEndDate(positionData.getEndDate());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.positionDataRepository.flush();
	}

}
