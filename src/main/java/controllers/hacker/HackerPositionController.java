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

import services.PositionService;
import controllers.AbstractController;
import domain.Position;

@Controller
@RequestMapping("/position/hacker")
public class HackerPositionController extends AbstractController {

	@Autowired
	PositionService	positionService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showPosition(@RequestParam final int positionId) {
		ModelAndView result;
		Position position;

		position = this.positionService.findPositionHackerLogged(positionId);

		result = new ModelAndView("position/show");

		result.addObject("position", position);

		return result;
	}
}
