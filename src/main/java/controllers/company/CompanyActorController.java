/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.CompanyService;
import services.UserAccountService;
import controllers.AbstractController;
import domain.Actor;
import domain.Company;
import forms.CompanyForm;

@Controller
@RequestMapping("/actor/company")
public class CompanyActorController extends AbstractController {

	@Autowired
	ActorService		actorService;

	@Autowired
	CompanyService		companyService;

	@Autowired
	UserAccountService	userAccountService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Company actor;

		actor = (Company) this.actorService.findActorLogged();
		final CompanyForm actorForm = new CompanyForm(actor);

		result = new ModelAndView("actor/edit");

		result.addObject("authority", Authority.COMPANY);
		result.addObject("actionURL", "actor/company/edit.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("actorForm") CompanyForm actorForm, final BindingResult binding) {
		ModelAndView result;

		try {
			actorForm = this.companyService.reconstruct(actorForm, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(actorForm.getActor());
			else {
				this.companyService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(actorForm.getActor(), "hacking.logged.error");
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
			result = new ModelAndView("actor/edit");

		if (actor instanceof Company)
			result.addObject("authority", Authority.COMPANY);
		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
