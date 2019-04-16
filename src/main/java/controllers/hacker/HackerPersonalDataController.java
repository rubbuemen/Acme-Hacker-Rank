/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.hacker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculaService;
import services.PersonalDataService;
import controllers.AbstractController;
import domain.Curricula;
import domain.PersonalData;

@Controller
@RequestMapping("/personalData/hacker")
public class HackerPersonalDataController extends AbstractController {

	@Autowired
	PersonalDataService	personalDataService;

	@Autowired
	CurriculaService	curriculaService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalDataId) {
		ModelAndView result;
		PersonalData personalData = null;
		Curricula curricula = null;

		try {
			personalData = this.personalDataService.findPersonalDataHackerLogged(personalDataId);
			curricula = this.curriculaService.findCurriculaByPersonalDataId(personalDataId);
			result = this.createEditModelAndView(personalData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(personalData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(personalData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(PersonalData personalData, final BindingResult binding) {
		ModelAndView result;
		Curricula curricula = null;

		try {
			curricula = this.curriculaService.findCurriculaByPersonalDataId(personalData.getId());
			personalData = this.personalDataService.reconstruct(personalData, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(personalData, curricula);
			else {
				this.personalDataService.save(personalData);
				result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(personalData, curricula, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, curricula, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(personalData, curricula, "commit.error");
		}

		return result;
	}
	// Ancillary methods

	protected ModelAndView createEditModelAndView(final PersonalData personalData, final Curricula curricula) {
		ModelAndView result;
		result = this.createEditModelAndView(personalData, curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PersonalData personalData, final Curricula curricula, final String message) {
		ModelAndView result;

		if (personalData == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (personalData.getId() == 0)
			result = new ModelAndView("personalData/create");
		else
			result = new ModelAndView("personalData/edit");

		result.addObject("personalData", personalData);
		result.addObject("curricula", curricula);
		result.addObject("actionURL", "personalData/hacker/edit.do");
		result.addObject("message", message);

		return result;
	}

}
