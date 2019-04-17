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
import services.PositionDataService;
import controllers.AbstractController;
import domain.Curricula;
import domain.PositionData;

@Controller
@RequestMapping("/positionData/hacker")
public class HackerPositionDataController extends AbstractController {

	@Autowired
	PositionDataService	positionDataService;

	@Autowired
	CurriculaService	curriculaService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		PositionData positionData = null;
		Curricula curricula = null;

		try {
			positionData = this.positionDataService.create();
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(positionData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(positionData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionDataId, @RequestParam final int curriculaId) {
		ModelAndView result;
		PositionData positionData = null;
		Curricula curricula = null;

		try {
			positionData = this.positionDataService.findPositionDataHackerLogged(positionDataId);
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(positionData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(positionData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(PositionData positionData, final BindingResult binding, @RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula = null;

		try {
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			positionData = this.positionDataService.reconstruct(positionData, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(positionData, curricula);
			else {
				this.positionDataService.save(positionData, curricula);
				result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("Start date must be before end date"))
				result = this.createEditModelAndView(positionData, curricula, "positionData.error.dates");
			else if (oops.getMessage().equals("You can not edit a copy of your curricula"))
				result = this.createEditModelAndView(positionData, curricula, "curricula.error.copy");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionData, curricula, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, curricula, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(positionData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int positionDataId, @RequestParam final int curriculaId) {
		ModelAndView result;

		final PositionData positionData = this.positionDataService.findPositionDataHackerLogged(positionDataId);
		final Curricula curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);

		try {
			this.positionDataService.delete(positionData);
			result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not edit a copy of your curricula"))
				result = this.createEditModelAndView(positionData, curricula, "curricula.error.copy");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(positionData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(positionData, curricula, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final PositionData positionData, final Curricula curricula) {
		ModelAndView result;
		result = this.createEditModelAndView(positionData, curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionData positionData, final Curricula curricula, final String message) {
		ModelAndView result;

		if (positionData == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (positionData.getId() == 0)
				result = new ModelAndView("positionData/create");
			else
				result = new ModelAndView("positionData/edit");
			result.addObject("actionURL", "positionData/hacker/edit.do?curriculaId=" + curricula.getId());
		}

		result.addObject("positionData", positionData);
		result.addObject("curricula", curricula);
		result.addObject("message", message);

		return result;
	}

}
