
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PersonalDataRepository;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;

@Service
@Transactional
public class PersonalDataService {

	// Managed repository
	@Autowired
	private PersonalDataRepository	personalDataRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private CurriculaService		curriculaService;


	// Simple CRUD methods
	public PersonalData create() {

		PersonalData result;

		result = new PersonalData();

		return result;
	}

	public Collection<PersonalData> findAll() {
		Collection<PersonalData> result;

		result = this.personalDataRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public PersonalData findOne(final int personalDataId) {
		Assert.isTrue(personalDataId != 0);
		PersonalData result;

		result = this.personalDataRepository.findOne(personalDataId);
		Assert.notNull(result);

		return result;
	}

	// R17.1
	public PersonalData save(final PersonalData personalData) {
		Assert.notNull(personalData);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		PersonalData result;

		if (personalData.getId() != 0) {
			final Hacker hackerOwner = this.hackerService.findHackerByPersonalDataId(personalData.getId());
			Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");
			final Curricula curricula = this.curriculaService.findCurriculaByPersonalDataId(personalData.getId());
			Assert.isTrue(!curricula.getIsCopy(), "You can not edit a copy of your curricula");
			result = this.personalDataRepository.save(personalData);
		}

		result = this.personalDataRepository.save(personalData);

		return result;
	}

	public PersonalData saveAuxiliar(final PersonalData personalData) {
		Assert.notNull(personalData);
		PersonalData result;

		result = this.personalDataRepository.save(personalData);

		return result;
	}

	public void delete(final PersonalData personalData) {
		Assert.notNull(personalData);
		Assert.isTrue(personalData.getId() != 0);
		Assert.isTrue(this.personalDataRepository.exists(personalData.getId()));

		this.personalDataRepository.delete(personalData);
	}

	// Other business methods
	public PersonalData findPersonalDataHackerLogged(final int personalDataId) {
		Assert.isTrue(personalDataId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hackerOwner = this.hackerService.findHackerByPersonalDataId(personalDataId);
		Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");

		PersonalData result;

		result = this.personalDataRepository.findOne(personalDataId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public PersonalData reconstruct(final PersonalData personalData, final BindingResult binding) {
		PersonalData result;

		//No se estará creando desde aquí, unicamente se editará
		result = this.personalDataRepository.findOne(personalData.getId());
		Assert.notNull(result, "This entity does not exist");
		result.setName(personalData.getName());
		result.setStatement(personalData.getStatement());
		result.setPhoneNumber(personalData.getPhoneNumber());
		result.setGitHubProfile(personalData.getGitHubProfile());
		result.setLinkedInProfile(personalData.getLinkedInProfile());

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.personalDataRepository.flush();
	}

}
