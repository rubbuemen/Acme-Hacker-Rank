
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	// Attributes
	private String	keyWord;
	private Date	deadline;
	private Double	minSalary;
	private Date	maxDeadline;
	private Date	searchMoment;


	// Getters and Setters
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@Min(0)
	@Digits(integer = 6, fraction = 2)
	public Double getMinSalary() {
		return this.minSalary;
	}

	public void setMinSalary(final Double minSalary) {
		this.minSalary = minSalary;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMaxDeadline() {
		return this.maxDeadline;
	}

	public void setMaxDeadline(final Date maxDeadline) {
		this.maxDeadline = maxDeadline;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past
	public Date getSearchMoment() {
		return this.searchMoment;
	}

	public void setSearchMoment(final Date searchMoment) {
		this.searchMoment = searchMoment;
	}


	// Relationships
	private Collection<Position>	positions;


	@Valid
	@EachNotNull
	@ManyToMany
	public Collection<Position> getPositions() {
		return this.positions;
	}

	public void setPositions(final Collection<Position> positions) {
		this.positions = positions;
	}

}
