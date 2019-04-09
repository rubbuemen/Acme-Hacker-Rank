/*
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.AdministratorService;
import services.CompanyService;
import services.HackerService;
import services.UserAccountService;
import domain.Actor;
import domain.Company;
import domain.Hacker;
import forms.CompanyForm;
import forms.HackerForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	ActorService			actorService;

	@Autowired
	CompanyService			companyService;

	@Autowired
	HackerService			hackerService;

	@Autowired
	AdministratorService	administratorService;

	@Autowired
	UserAccountService		userAccountService;


	@RequestMapping(value = "/register-company", method = RequestMethod.GET)
	public ModelAndView registerCompany() {
		ModelAndView result;
		Company actor;

		actor = this.companyService.create();

		final CompanyForm actorForm = new CompanyForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("authority", Authority.COMPANY);
		result.addObject("actionURL", "actor/register-company.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-hacker", method = RequestMethod.GET)
	public ModelAndView registerHacker() {
		ModelAndView result;
		Hacker actor;

		actor = this.hackerService.create();

		final HackerForm actorForm = new HackerForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-hacker.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-company", method = RequestMethod.POST, params = "save")
	public ModelAndView registerCompany(@ModelAttribute("actorForm") CompanyForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.companyService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.companyService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("Invalid credit card"))
					result = this.createEditModelAndView(actorForm.getActor(), "creditCard.error.invalid");
				else if (oops.getMessage().equals("Expired credit card"))
					result = this.createEditModelAndView(actorForm.getActor(), "creditCard.error.expired");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/register-hacker", method = RequestMethod.POST, params = "save")
	public ModelAndView registerHacker(@ModelAttribute("actorForm") HackerForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.hackerService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.hackerService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("Invalid credit card"))
					result = this.createEditModelAndView(actorForm.getActor(), "creditCard.error.invalid");
				else if (oops.getMessage().equals("Expired credit card"))
					result = this.createEditModelAndView(actorForm.getActor(), "creditCard.error.expired");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		if (actor == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("actor/register");

		if (actor instanceof Company)
			result.addObject("authority", Authority.COMPANY);
		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
