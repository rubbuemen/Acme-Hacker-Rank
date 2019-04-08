
package forms;

import javax.validation.Valid;

import domain.Company;

public class CompanyForm {

	// Attributes
	@Valid
	private Company	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public CompanyForm() {
		super();
	}

	public CompanyForm(final Company actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Company getActor() {
		return this.actor;
	}

	public void setActor(final Company actor) {
		this.actor = actor;
	}

	public String getPasswordCheck() {
		return this.passwordCheck;
	}

	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public Boolean getTermsConditions() {
		return this.termsConditions;
	}

	public void setTermsConditions(final Boolean termsConditions) {
		this.termsConditions = termsConditions;
	}

}
