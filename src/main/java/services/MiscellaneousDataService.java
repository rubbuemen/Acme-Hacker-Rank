
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MiscellaneousDataRepository;
import domain.MiscellaneousData;

@Service
@Transactional
public class MiscellaneousDataService {

	// Managed repository
	@Autowired
	private MiscellaneousDataRepository	miscellaneousDataRepository;


	// Supporting services

	// Simple CRUD methods
	public MiscellaneousData create() {

		MiscellaneousData result;

		result = new MiscellaneousData();

		return result;
	}

	public Collection<MiscellaneousData> findAll() {
		Collection<MiscellaneousData> result;

		result = this.miscellaneousDataRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public MiscellaneousData findOne(final int miscellaneousDataId) {
		Assert.isTrue(miscellaneousDataId != 0);
		MiscellaneousData result;

		result = this.miscellaneousDataRepository.findOne(miscellaneousDataId);
		Assert.notNull(result);

		return result;
	}

	public MiscellaneousData save(final MiscellaneousData miscellaneousData) {
		Assert.notNull(miscellaneousData);

		MiscellaneousData result;

		if (miscellaneousData.getId() == 0)
			result = this.miscellaneousDataRepository.save(miscellaneousData);
		else
			result = this.miscellaneousDataRepository.save(miscellaneousData);

		return result;
	}

	public void delete(final MiscellaneousData miscellaneousData) {
		Assert.notNull(miscellaneousData);
		Assert.isTrue(miscellaneousData.getId() != 0);
		Assert.isTrue(this.miscellaneousDataRepository.exists(miscellaneousData.getId()));

		this.miscellaneousDataRepository.delete(miscellaneousData);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public MiscellaneousData reconstruct(final MiscellaneousData miscellaneousData, final BindingResult binding) {
		MiscellaneousData result;

		if (miscellaneousData.getId() == 0)
			result = miscellaneousData;
		else {
			result = this.miscellaneousDataRepository.findOne(miscellaneousData.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.miscellaneousDataRepository.flush();
	}

}
