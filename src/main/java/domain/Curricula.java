
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Curricula extends DomainEntity {

	// Attributes
	private Boolean	isCopy;


	// Getters and Setters
	@NotNull
	public Boolean getIsCopy() {
		return this.isCopy;
	}

	public void setIsCopy(final Boolean isCopy) {
		this.isCopy = isCopy;
	}


	// Relationships
	private Hacker							hacker;
	private PersonalData					personalData;
	private Collection<PositionData>		positionDatas;
	private Collection<EducationData>		educationDatas;
	private Collection<MiscellaneousData>	miscellaneousDatas;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Hacker getHacker() {
		return this.hacker;
	}

	public void setHacker(final Hacker hacker) {
		this.hacker = hacker;
	}

	@Valid
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	public PersonalData getPersonalData() {
		return this.personalData;
	}

	public void setPersonalData(final PersonalData personalData) {
		this.personalData = personalData;
	}

	@Valid
	@EachNotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<PositionData> getPositionDatas() {
		return this.positionDatas;
	}

	public void setPositionDatas(final Collection<PositionData> positionDatas) {
		this.positionDatas = positionDatas;
	}

	@Valid
	@EachNotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<EducationData> getEducationDatas() {
		return this.educationDatas;
	}

	public void setEducationDatas(final Collection<EducationData> educationDatas) {
		this.educationDatas = educationDatas;
	}

	@Valid
	@EachNotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousData> getMiscellaneousDatas() {
		return this.miscellaneousDatas;
	}

	public void setMiscellaneousDatas(final Collection<MiscellaneousData> miscellaneousDatas) {
		this.miscellaneousDatas = miscellaneousDatas;
	}

}
