
package forms;

import javax.validation.Valid;

import domain.Hacker;

public class HackerForm {

	// Attributes
	@Valid
	private Hacker	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public HackerForm() {
		super();
	}

	public HackerForm(final Hacker actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Hacker getActor() {
		return this.actor;
	}

	public void setActor(final Hacker actor) {
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
