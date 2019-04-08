
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository
	@Autowired
	private SocialProfileRepository	socialProfileRepository;


	// Supporting services

	// Simple CRUD methods
	public SocialProfile create() {

		SocialProfile result;

		result = new SocialProfile();

		return result;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> result;

		result = this.socialProfileRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SocialProfile findOne(final int socialProfileId) {
		Assert.isTrue(socialProfileId != 0);
		SocialProfile result;

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}

	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);

		SocialProfile result;

		if (socialProfile.getId() == 0)
			result = this.socialProfileRepository.save(socialProfile);
		else
			result = this.socialProfileRepository.save(socialProfile);

		return result;
	}

	public void delete(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		Assert.isTrue(this.socialProfileRepository.exists(socialProfile.getId()));

		this.socialProfileRepository.delete(socialProfile);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SocialProfile reconstruct(final SocialProfile socialProfile, final BindingResult binding) {
		SocialProfile result;

		if (socialProfile.getId() == 0)
			result = socialProfile;
		else {
			result = this.socialProfileRepository.findOne(socialProfile.getId());
			Assert.notNull(result, "This entity does not exist");

		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.socialProfileRepository.flush();
	}

}
