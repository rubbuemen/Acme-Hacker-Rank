/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PositionService;
import domain.Company;
import domain.Position;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

	@Autowired
	PositionService	positionService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listPositions(@RequestParam(required = false) final Integer companyId) {
		ModelAndView result;
		Collection<Position> positions;
		Map<Position, Company> mapPositionCompany = null;

		if (companyId != null)
			positions = this.positionService.findPositionsFinalModeNotCancelledNotDeadlineByCompanyId(companyId);
		else {
			positions = this.positionService.findPositionsFinalModeNotCancelledNotDeadline();
			mapPositionCompany = this.positionService.getMapPositionCompany(positions);
		}

		result = new ModelAndView("position/listGeneric");

		result.addObject("positions", positions);
		result.addObject("mapPositionCompany", mapPositionCompany);
		result.addObject("requestURI", "position/listGeneric.do");

		return result;
	}

	@RequestMapping(value = "/listGeneric", method = RequestMethod.POST, params = "search")
	public ModelAndView searchPosition(final String singleKeyWord) {
		ModelAndView result;
		Collection<Position> positions;
		Map<Position, Company> mapPositionCompany = null;

		positions = this.positionService.findPositionsFinalModeNotCancelledNotDeadlineBySingleKeyWord(singleKeyWord);
		mapPositionCompany = this.positionService.getMapPositionCompany(positions);

		result = new ModelAndView("position/listGeneric");
		result.addObject("positions", positions);
		result.addObject("mapPositionCompany", mapPositionCompany);
		result.addObject("requestURI", "position/listGeneric.do");

		return result;
	}
}
