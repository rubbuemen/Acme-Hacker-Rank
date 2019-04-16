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
import services.EducationDataService;
import controllers.AbstractController;
import domain.Curricula;
import domain.EducationData;

@Controller
@RequestMapping("/educationData/hacker")
public class HackerEducationDataController extends AbstractController {

	@Autowired
	EducationDataService	educationDataService;

	@Autowired
	CurriculaService		curriculaService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		EducationData educationData = null;
		Curricula curricula = null;

		try {
			educationData = this.educationDataService.create();
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(educationData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(educationData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(educationData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int educationDataId, @RequestParam final int curriculaId) {
		ModelAndView result;
		EducationData educationData = null;
		Curricula curricula = null;

		try {
			educationData = this.educationDataService.findEducationDataHackerLogged(educationDataId);
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(educationData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(educationData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(educationData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(EducationData educationData, final BindingResult binding, @RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula = null;

		try {
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			educationData = this.educationDataService.reconstruct(educationData, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(educationData, curricula);
			else {
				this.educationDataService.save(educationData, curricula);
				result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("Start date must be before end date"))
				result = this.createEditModelAndView(educationData, curricula, "educationData.error.dates");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(educationData, curricula, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, curricula, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(educationData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int educationDataId, @RequestParam final int curriculaId) {
		ModelAndView result;

		final EducationData educationData = this.educationDataService.findEducationDataHackerLogged(educationDataId);
		final Curricula curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);

		try {
			this.educationDataService.delete(educationData);
			result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(educationData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(educationData, curricula, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final EducationData educationData, final Curricula curricula) {
		ModelAndView result;
		result = this.createEditModelAndView(educationData, curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationData educationData, final Curricula curricula, final String message) {
		ModelAndView result;

		if (educationData == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (educationData.getId() == 0)
				result = new ModelAndView("educationData/create");
			else
				result = new ModelAndView("educationData/edit");
			result.addObject("actionURL", "educationData/hacker/edit.do?curriculaId=" + curricula.getId());
		}

		result.addObject("educationData", educationData);
		result.addObject("curricula", curricula);
		result.addObject("message", message);

		return result;
	}

}
