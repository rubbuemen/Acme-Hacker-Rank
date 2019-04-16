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
import services.MiscellaneousDataService;
import controllers.AbstractController;
import domain.Curricula;
import domain.MiscellaneousData;

@Controller
@RequestMapping("/miscellaneousData/hacker")
public class HackerMiscellaneousDataController extends AbstractController {

	@Autowired
	MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	CurriculaService			curriculaService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		MiscellaneousData miscellaneousData = null;
		Curricula curricula = null;

		try {
			miscellaneousData = this.miscellaneousDataService.create();
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(miscellaneousData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(miscellaneousData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousDataId, @RequestParam final int curriculaId) {
		ModelAndView result;
		MiscellaneousData miscellaneousData = null;
		Curricula curricula = null;

		try {
			miscellaneousData = this.miscellaneousDataService.findMiscellaneousDataHackerLogged(miscellaneousDataId);
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(miscellaneousData, curricula);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(miscellaneousData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(MiscellaneousData miscellaneousData, final BindingResult binding, @RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula = null;

		try {
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			miscellaneousData = this.miscellaneousDataService.reconstruct(miscellaneousData, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(miscellaneousData, curricula);
			else {
				this.miscellaneousDataService.save(miscellaneousData, curricula);
				result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousData, curricula, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, curricula, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(miscellaneousData, curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int miscellaneousDataId, @RequestParam final int curriculaId) {
		ModelAndView result;

		final MiscellaneousData miscellaneousData = this.miscellaneousDataService.findMiscellaneousDataHackerLogged(miscellaneousDataId);
		final Curricula curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);

		try {
			this.miscellaneousDataService.delete(miscellaneousData);
			result = new ModelAndView("redirect:/curricula/hacker/show.do?curriculaId=" + curricula.getId());

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(miscellaneousData, curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(miscellaneousData, curricula, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final MiscellaneousData miscellaneousData, final Curricula curricula) {
		ModelAndView result;
		result = this.createEditModelAndView(miscellaneousData, curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousData miscellaneousData, final Curricula curricula, final String message) {
		ModelAndView result;

		if (miscellaneousData == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (miscellaneousData.getId() == 0)
				result = new ModelAndView("miscellaneousData/create");
			else
				result = new ModelAndView("miscellaneousData/edit");
			result.addObject("actionURL", "miscellaneousData/hacker/edit.do?curriculaId=" + curricula.getId());
		}

		result.addObject("miscellaneousData", miscellaneousData);
		result.addObject("curricula", curricula);
		result.addObject("message", message);

		return result;
	}

}
