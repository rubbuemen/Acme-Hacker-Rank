
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Company;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Managed repository
	@Autowired
	private PositionRepository	positionRepository;

	// Supporting services
	@Autowired
	private CompanyService		companyService;


	// Simple CRUD methods
	public Position create() {

		Position result;

		result = new Position();

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

	public Position save(final Position position) {
		Assert.notNull(position);

		Position result;

		if (position.getId() == 0)
			result = this.positionRepository.save(position);
		else
			result = this.positionRepository.save(position);

		return result;
	}

	public void delete(final Position position) {
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);
		Assert.isTrue(this.positionRepository.exists(position.getId()));

		this.positionRepository.delete(position);
	}

	// Other business methods
	//R7.2
	public Collection<Position> findPositionsFinalModeNotCancelled() {
		Collection<Position> result;

		result = this.positionRepository.findPositionsFinalModeNotCancelled();
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

	public Collection<Position> findPositionsFinalModeNotCancelledByCompanyId(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findPositionsFinalModeNotCancelledByCompanyId(companyId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Position> findPositionsFinalModeNotCancelledBySingleKeyWord(final String singleKeyWord) {
		Collection<Position> result;

		result = this.positionRepository.findPositionsFinalModeNotCancelledBySingleKeyWord(singleKeyWord);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Position reconstruct(final Position position, final BindingResult binding) {
		Position result;

		if (position.getId() == 0)
			result = position;
		else {
			result = this.positionRepository.findOne(position.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.positionRepository.flush();
	}

}
