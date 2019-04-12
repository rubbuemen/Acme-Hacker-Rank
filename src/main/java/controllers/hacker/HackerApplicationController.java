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

import services.ApplicationService;
import services.CurriculaService;
import services.PositionService;
import controllers.AbstractController;
import domain.Application;
import domain.Curricula;
import domain.Position;

@Controller
@RequestMapping("/application/hacker")
public class HackerApplicationController extends AbstractController {

	@Autowired
	ApplicationService	applicationService;

	@Autowired
	PositionService		positionService;

	@Autowired
	CurriculaService	curriculaService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.findApplicationsOrderByStatusByHackerLogged();

		result = new ModelAndView("application/list");

		result.addObject("applications", applications);
		result.addObject("requestURI", "application/hacker/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Application application;

		application = this.applicationService.create();

		result = this.createEditModelAndView(application);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application = null;

		try {
			application = this.applicationService.findApplicationHackerLogged(applicationId);
			result = this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(application, "hacking.logged.error");
			else
				result = this.createEditModelAndView(application, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Application application, final BindingResult binding) {
		ModelAndView result;

		try {
			application = this.applicationService.reconstruct(application, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(application);
			else {
				this.applicationService.save(application);
				result = new ModelAndView("redirect:/application/hacker/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have not selected a position really available to you"))
				result = this.createEditModelAndView(application, "application.error.positionNotAvailable");
			else if (oops.getMessage().equals("You are not the owner of this curricula"))
				result = this.createEditModelAndView(application, "application.error.curriculaNotOwner");
			else if (oops.getMessage().equals("You must provide explanations about the solution to the problem"))
				result = this.createEditModelAndView(application, "application.error.explanationsNotBlank");
			else if (oops.getMessage().equals("You must provide a code link about the solution to the problem"))
				result = this.createEditModelAndView(application, "application.error.codeLinkNotBlank");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(application, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(application, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;
		result = this.createEditModelAndView(application, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String message) {
		ModelAndView result;

		if (application == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (application.getId() == 0) {
			result = new ModelAndView("application/create");
			final Collection<Position> positions = this.positionService.findPositionsToSelectApplication();
			final Collection<Curricula> curriculas = this.curriculaService.findCurriculasByHackerLogged();
			result.addObject("positions", positions);
			result.addObject("curriculas", curriculas);
		} else
			result = new ModelAndView("application/edit");

		result.addObject("application", application);
		result.addObject("actionURL", "application/hacker/edit.do");
		result.addObject("message", message);

		return result;
	}

}
