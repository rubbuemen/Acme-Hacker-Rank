
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class EducationData extends DomainEntity {

	// Attributes
	private String	degree;
	private String	institution;
	private Double	mark;
	private Date	startDate;
	private Date	endDate;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDegree() {
		return this.degree;
	}

	public void setDegree(final String degree) {
		this.degree = degree;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getInstitution() {
		return this.institution;
	}

	public void setInstitution(final String institution) {
		this.institution = institution;
	}

	@NotNull
	@Range(min = 0, max = 10)
	@Digits(integer = 2, fraction = 2)
	public Double getMark() {
		return this.mark;
	}

	public void setMark(final Double mark) {
		this.mark = mark;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

}
