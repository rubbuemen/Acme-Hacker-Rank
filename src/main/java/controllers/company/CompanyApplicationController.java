/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.company;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import controllers.AbstractController;
import domain.Application;

@Controller
@RequestMapping("/application/company")
public class CompanyApplicationController extends AbstractController {

	@Autowired
	ApplicationService	applicationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Application> applications;

		applications = this.applicationService.findApplicationsOrderByStatusByCompanyLogged();

		result = new ModelAndView("application/list");

		result.addObject("applications", applications);
		result.addObject("requestURI", "application/company/list.do");

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView acceptApplication(@RequestParam final int applicationId) {
		ModelAndView result;

		final Application application = this.applicationService.findApplicationCompanyLogged(applicationId);
		final Collection<Application> applications = this.applicationService.findApplicationsOrderByStatusByCompanyLogged();

		try {
			this.applicationService.acceptApplication(application);
			result = new ModelAndView("redirect:/application/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The status of this application is not 'submitted'"))
				result = this.createEditModelAndView(applications, "application.error.notSubmitted");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(applications, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(applications, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView rejectApplication(@RequestParam final int applicationId) {
		ModelAndView result;

		final Application application = this.applicationService.findApplicationCompanyLogged(applicationId);
		final Collection<Application> applications = this.applicationService.findApplicationsOrderByStatusByCompanyLogged();

		try {
			this.applicationService.rejectApplication(application);
			result = new ModelAndView("redirect:/application/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The status of this application is not 'submitted'"))
				result = this.createEditModelAndView(applications, "application.error.notSubmitted");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(applications, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(applications, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Collection<Application> applications) {
		ModelAndView result;
		result = this.createEditModelAndView(applications, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Application> applications, final String message) {
		ModelAndView result;

		if (applications == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("application/list");

		result.addObject("applications", applications);
		result.addObject("message", message);

		return result;
	}

}
