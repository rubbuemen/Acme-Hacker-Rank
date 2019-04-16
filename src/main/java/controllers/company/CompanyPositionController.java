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

import services.PositionService;
import services.ProblemService;
import controllers.AbstractController;
import domain.Position;
import domain.Problem;

@Controller
@RequestMapping("/position/company")
public class CompanyPositionController extends AbstractController {

	@Autowired
	PositionService	positionService;

	@Autowired
	ProblemService	problemService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Position> positions;

		positions = this.positionService.findPositionsByCompanyLogged();

		result = new ModelAndView("position/list");

		result.addObject("positions", positions);
		result.addObject("requestURI", "position/company/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Position position;

		position = this.positionService.create();

		result = this.createEditModelAndView(position);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		ModelAndView result;
		Position position = null;

		try {
			position = this.positionService.findPositionCompanyLogged(positionId);
			result = this.createEditModelAndView(position);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(position, "hacking.logged.error");
			else
				result = this.createEditModelAndView(position, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Position position, final BindingResult binding) {
		ModelAndView result;

		try {
			position = this.positionService.reconstruct(position, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(position);
			else {
				this.positionService.save(position);
				result = new ModelAndView("redirect:/position/company/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only save positions that are not in final mode"))
				result = this.createEditModelAndView(position, "position.error.save.finalMode");
			else if (oops.getMessage().equals("The logged company is not the owner of this problem"))
				result = this.createEditModelAndView(position, "position.error.save.problem.hacking");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(position, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(position, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int positionId) {
		ModelAndView result;

		final Position position = this.positionService.findPositionCompanyLogged(positionId);

		try {
			this.positionService.delete(position);
			result = new ModelAndView("redirect:/position/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only delete positions that are not in final mode"))
				result = this.createEditModelAndView(position, "position.error.delete.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(position, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(position, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView changeFinalMode(@RequestParam final int positionId) {
		ModelAndView result;

		final Position position = this.positionService.findPositionCompanyLogged(positionId);

		try {
			this.positionService.changeFinalMode(position);
			result = new ModelAndView("redirect:/position/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This position is already in final mode"))
				result = this.createEditModelAndView(position, "position.error.change.finalMode");
			else if (oops.getMessage().equals("A position cannot be saved in final mode unless there are at least two problems associated with it"))
				result = this.createEditModelAndView(position, "position.error.change.finalMode.notEnoughProblems");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(position, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(position, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int positionId) {
		ModelAndView result;

		final Position position = this.positionService.findPositionCompanyLogged(positionId);

		try {
			this.positionService.changeCancelled(position);
			result = new ModelAndView("redirect:/position/company/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("To cancel a position, it must be in final mode"))
				result = this.createEditModelAndView(position, "position.error.cancel.isNotfinalMode");
			else if (oops.getMessage().equals("This position is already cancelled"))
				result = this.createEditModelAndView(position, "position.error.cancel");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(position, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(position, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showPosition(@RequestParam final int positionId) {
		ModelAndView result;
		Position position;

		position = this.positionService.findPositionCompanyLogged(positionId);
		result = new ModelAndView("position/show");

		result.addObject("position", position);

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Position position) {
		ModelAndView result;
		result = this.createEditModelAndView(position, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Position position, final String message) {
		ModelAndView result;

		if (position == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (position.getId() == 0)
				result = new ModelAndView("position/create");
			else
				result = new ModelAndView("position/edit");
			final Collection<Problem> problemsCompanyLogged = this.problemService.findProblemsFinalModeByCompanyLogged();
			result.addObject("problems", problemsCompanyLogged);
		}

		result.addObject("position", position);
		result.addObject("actionURL", "position/company/edit.do");
		result.addObject("message", message);

		return result;
	}

}
