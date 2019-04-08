
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionDataRepository;
import domain.PositionData;

@Service
@Transactional
public class PositionDataService {

	// Managed repository
	@Autowired
	private PositionDataRepository	positionDataRepository;


	// Supporting services

	// Simple CRUD methods
	public PositionData create() {

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

	public PositionData save(final PositionData positionData) {
		Assert.notNull(positionData);

		PositionData result;

		if (positionData.getId() == 0)
			result = this.positionDataRepository.save(positionData);
		else
			result = this.positionDataRepository.save(positionData);

		return result;
	}

	public void delete(final PositionData positionData) {
		Assert.notNull(positionData);
		Assert.isTrue(positionData.getId() != 0);
		Assert.isTrue(this.positionDataRepository.exists(positionData.getId()));

		this.positionDataRepository.delete(positionData);
	}


	// Other business methods

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

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.positionDataRepository.flush();
	}

}
