
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CompanyRepository;
import domain.Company;

@Service
@Transactional
public class CompanyService {

	// Managed repository
	@Autowired
	private CompanyRepository	companyRepository;


	// Supporting services

	// Simple CRUD methods
	public Company create() {

		Company result;

		result = new Company();

		return result;
	}

	public Collection<Company> findAll() {
		Collection<Company> result;

		result = this.companyRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Company findOne(final int companyId) {
		Assert.isTrue(companyId != 0);
		Company result;

		result = this.companyRepository.findOne(companyId);
		Assert.notNull(result);

		return result;
	}

	public Company save(final Company company) {
		Assert.notNull(company);

		Company result;

		if (company.getId() == 0)
			result = this.companyRepository.save(company);
		else
			result = this.companyRepository.save(company);

		return result;
	}

	public void delete(final Company company) {
		Assert.notNull(company);
		Assert.isTrue(company.getId() != 0);
		Assert.isTrue(this.companyRepository.exists(company.getId()));

		this.companyRepository.delete(company);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Company reconstruct(final Company company, final BindingResult binding) {
		Company result;

		if (company.getId() == 0)
			result = company;
		else {
			result = this.companyRepository.findOne(company.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.companyRepository.flush();
	}

}
