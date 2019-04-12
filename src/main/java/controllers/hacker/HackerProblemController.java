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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProblemService;
import controllers.AbstractController;
import domain.Problem;

@Controller
@RequestMapping("/problem/hacker")
public class HackerProblemController extends AbstractController {

	@Autowired
	ProblemService	problemService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showProblem(@RequestParam final int problemId) {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.findOne(problemId);

		result = new ModelAndView("problem/show");

		result.addObject("problem", problem);

		return result;
	}
}
