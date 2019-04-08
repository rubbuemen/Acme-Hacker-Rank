
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PersonalDataRepository;
import domain.PersonalData;

@Service
@Transactional
public class PersonalDataService {

	// Managed repository
	@Autowired
	private PersonalDataRepository	personalDataRepository;


	// Supporting services

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

	public PersonalData save(final PersonalData personalData) {
		Assert.notNull(personalData);

		PersonalData result;

		if (personalData.getId() == 0)
			result = this.personalDataRepository.save(personalData);
		else
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

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public PersonalData reconstruct(final PersonalData personalData, final BindingResult binding) {
		PersonalData result;

		if (personalData.getId() == 0)
			result = personalData;
		else {
			result = this.personalDataRepository.findOne(personalData.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.personalDataRepository.flush();
	}

}
