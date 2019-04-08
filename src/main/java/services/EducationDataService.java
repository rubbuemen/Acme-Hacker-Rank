
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EducationDataRepository;
import domain.EducationData;

@Service
@Transactional
public class EducationDataService {

	// Managed repository
	@Autowired
	private EducationDataRepository	educationDataRepository;


	// Supporting services

	// Simple CRUD methods
	public EducationData create() {

		EducationData result;

		result = new EducationData();

		return result;
	}

	public Collection<EducationData> findAll() {
		Collection<EducationData> result;

		result = this.educationDataRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public EducationData findOne(final int educationDataId) {
		Assert.isTrue(educationDataId != 0);
		EducationData result;

		result = this.educationDataRepository.findOne(educationDataId);
		Assert.notNull(result);

		return result;
	}

	public EducationData save(final EducationData educationData) {
		Assert.notNull(educationData);

		EducationData result;

		if (educationData.getId() == 0)
			result = this.educationDataRepository.save(educationData);
		else
			result = this.educationDataRepository.save(educationData);

		return result;
	}

	public void delete(final EducationData educationData) {
		Assert.notNull(educationData);
		Assert.isTrue(educationData.getId() != 0);
		Assert.isTrue(this.educationDataRepository.exists(educationData.getId()));

		this.educationDataRepository.delete(educationData);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public EducationData reconstruct(final EducationData educationData, final BindingResult binding) {
		EducationData result;

		if (educationData.getId() == 0)
			result = educationData;
		else {
			result = this.educationDataRepository.findOne(educationData.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.educationDataRepository.flush();
	}

}
