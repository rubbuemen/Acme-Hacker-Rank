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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.ProblemService;
import controllers.AbstractController;
import domain.Problem;

@Controller
@RequestMapping("/problem/company")
public class CompanyProblemController extends AbstractController {

	@Autowired
	ProblemService	problemService;

	@Autowired
	CompanyService	companyService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Problem> problems;

		problems = this.problemService.findProblemsByCompanyLogged();

		result = new ModelAndView("problem/list");

		result.addObject("problems", problems);
		result.addObject("requestURI", "problem/company/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.create();

		result = this.createEditModelAndView(problem);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int problemId) {
		ModelAndView result;
		Problem problem = null;

		try {
			problem = this.problemService.findProblemCompanyLogged(problemId);
			result = this.createEditModelAndView(problem);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(problem, "hacking.logged.error");
			else
				result = this.createEditModelAndView(problem, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Problem problem, final BindingResult binding) {
		ModelAndView result;

		try {
			problem = this.problemService.reconstruct(problem, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(problem);
			else {
				this.problemService.save(problem);
				result = new ModelAndView("redirect:/problem/company/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only save problems that are not in final mode"))
				result = this.createEditModelAndView(problem, "problem.error.save.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(problem, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(problem, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int problemId) {
		ModelAndView result;

		final Problem problem = this.problemService.findProblemCompanyLogged(problemId);

		try {
			this.problemService.delete(problem);
			result = new ModelAndView("redirect:/problem/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only delete problems that are not in final mode"))
				result = this.createEditModelAndView(problem, "problem.error.delete.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(problem, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(problem, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView changeFinalMode(@RequestParam final int problemId) {
		ModelAndView result;

		final Problem problem = this.problemService.findProblemCompanyLogged(problemId);

		try {
			this.problemService.changeFinalMode(problem);
			result = new ModelAndView("redirect:/problem/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This problem is already in final mode"))
				result = this.createEditModelAndView(problem, "problem.error.change.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(problem, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(problem, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showProblem(@RequestParam final int problemId) {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.findProblemCompanyLogged(problemId);

		result = new ModelAndView("problem/show");

		result.addObject("problem", problem);

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Problem problem) {
		ModelAndView result;
		result = this.createEditModelAndView(problem, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Problem problem, final String message) {
		ModelAndView result;

		if (problem == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (problem.getId() == 0)
			result = new ModelAndView("problem/create");
		else
			result = new ModelAndView("problem/edit");

		result.addObject("problem", problem);
		result.addObject("actionURL", "problem/company/edit.do");
		result.addObject("message", message);

		return result;
	}

}
