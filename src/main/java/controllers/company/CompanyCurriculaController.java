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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculaService;
import controllers.AbstractController;
import domain.Curricula;

@Controller
@RequestMapping("/curricula/company")
public class CompanyCurriculaController extends AbstractController {

	@Autowired
	CurriculaService	curriculaService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula curricula = null;

		try {
			curricula = this.curriculaService.findCurriculaCompanyLogged(curriculaId);
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
		else
			result = new ModelAndView("curricula/show");

		result.addObject("curricula", curricula);
		result.addObject("message", message);

		return result;
	}

}
