/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.hacker;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.CurriculaService;
import controllers.AbstractController;
import domain.Curricula;

@Controller
@RequestMapping("/curricula/hacker")
public class HackerCurriculaController extends AbstractController {

	@Autowired
	CurriculaService	curriculaService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Curricula> curriculas;

		curriculas = this.curriculaService.findCurriculasByHackerLogged();

		result = new ModelAndView("curricula/list");

		result.addObject("curriculas", curriculas);
		result.addObject("requestURI", "curricula/hacker/list.do");

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula = null;

		try {
			curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);
			result = this.createEditModelAndView(curricula);
			result.addObject("personalData", curricula.getPersonalData());
			result.addObject("positionDatas", curricula.getPositionDatas());
			result.addObject("educationDatas", curricula.getEducationDatas());
			result.addObject("miscellaneousDatas", curricula.getMiscellaneousDatas());
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Curricula curricula;

		curricula = this.curriculaService.create();

		result = this.createEditModelAndView(curricula);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Curricula curricula, final BindingResult binding) {
		ModelAndView result;

		try {
			curricula = this.curriculaService.reconstruct(curricula, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(curricula);
			else {
				this.curriculaService.save(curricula);
				result = new ModelAndView("redirect:/curricula/hacker/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(curricula, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(curricula, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int curriculaId) {
		ModelAndView result;

		final Curricula curricula = this.curriculaService.findCurriculaHackerLogged(curriculaId);

		try {
			this.curriculaService.delete(curricula);
			result = new ModelAndView("redirect:/curricula/hacker/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not edit a copy of your curricula"))
				result = this.createEditModelAndView(curricula, "curricula.error.copy");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(curricula, "hacking.logged.error");
			else
				result = this.createEditModelAndView(curricula, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Curricula curricula) {
		ModelAndView result;
		result = this.createEditModelAndView(curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Curricula curricula, final String message) {
		ModelAndView result;

		if (curricula == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (curricula.getId() == 0) {
			result = new ModelAndView("curricula/create");
			result.addObject("actionURL", "curricula/hacker/edit.do");
		} else
			result = new ModelAndView("curricula/show");

		if (!curricula.getIsCopy())
			result.addObject("authority", Authority.HACKER);
		result.addObject("curricula", curricula);
		result.addObject("message", message);

		return result;
	}

}
