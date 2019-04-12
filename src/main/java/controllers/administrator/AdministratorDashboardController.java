/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import controllers.AbstractController;
import domain.Company;
import domain.Hacker;
import domain.Position;

@Controller
@RequestMapping("/dashboard/administrator")
public class AdministratorDashboardController extends AbstractController {

	@Autowired
	AdministratorService	administratorService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView dashboard() {

		final ModelAndView result;

		result = new ModelAndView("dashboard/show");

		//Query C1
		final String[] queryC1 = this.administratorService.dashboardQueryC1().split(",");
		final String avgQueryC1 = queryC1[0];
		final String minQueryC1 = queryC1[1];
		final String maxQueryC1 = queryC1[2];
		final String stddevQueryC1 = queryC1[3];
		result.addObject("avgQueryC1", avgQueryC1);
		result.addObject("minQueryC1", minQueryC1);
		result.addObject("maxQueryC1", maxQueryC1);
		result.addObject("stddevQueryC1", stddevQueryC1);

		//Query C2
		final String[] queryC2 = this.administratorService.dashboardQueryC2().split(",");
		final String avgQueryC2 = queryC2[0];
		final String minQueryC2 = queryC2[1];
		final String maxQueryC2 = queryC2[2];
		final String stddevQueryC2 = queryC2[3];
		result.addObject("avgQueryC2", avgQueryC2);
		result.addObject("minQueryC2", minQueryC2);
		result.addObject("maxQueryC2", maxQueryC2);
		result.addObject("stddevQueryC2", stddevQueryC2);

		//Query C3
		final Collection<Company> queryC3 = this.administratorService.dashboardQueryC3();
		result.addObject("queryC3", queryC3);

		//Query C4
		final Collection<Hacker> queryC4 = this.administratorService.dashboardQueryC4();
		result.addObject("queryC4", queryC4);

		//Query C5
		final String[] queryC5 = this.administratorService.dashboardQueryC5().split(",");
		final String avgQueryC5 = queryC5[0];
		final String minQueryC5 = queryC5[1];
		final String maxQueryC5 = queryC5[2];
		final String stddevQueryC5 = queryC5[3];
		result.addObject("avgQueryC5", avgQueryC5);
		result.addObject("minQueryC5", minQueryC5);
		result.addObject("maxQueryC5", maxQueryC5);
		result.addObject("stddevQueryC5", stddevQueryC5);

		//Query C6
		final Position queryC6_1 = this.administratorService.dashboardQueryC6_1();
		result.addObject("queryC6_1", queryC6_1);
		final Position queryC6_2 = this.administratorService.dashboardQueryC6_2();
		result.addObject("queryC6_2", queryC6_2);

		//Query B1
		final String[] queryB1 = this.administratorService.dashboardQueryB1().split(",");
		final String avgQueryB1 = queryB1[0];
		final String minQueryB1 = queryB1[1];
		final String maxQueryB1 = queryB1[2];
		final String stddevQueryB1 = queryB1[3];
		result.addObject("avgQueryB1", avgQueryB1);
		result.addObject("minQueryB1", minQueryB1);
		result.addObject("maxQueryB1", maxQueryB1);
		result.addObject("stddevQueryB1", stddevQueryB1);

		//Query B2
		final String[] queryB2 = this.administratorService.dashboardQueryB2().split(",");
		final String avgQueryB2 = queryB2[0];
		final String minQueryB2 = queryB2[1];
		final String maxQueryB2 = queryB2[2];
		final String stddevQueryB2 = queryB2[3];
		result.addObject("avgQueryB2", avgQueryB2);
		result.addObject("minQueryB2", minQueryB2);
		result.addObject("maxQueryB2", maxQueryB2);
		result.addObject("stddevQueryB2", stddevQueryB2);

		//Query B3
		final String ratioQueryB3 = this.administratorService.dashboardQueryB3();
		result.addObject("ratioQueryB3", ratioQueryB3);

		return result;
	}

}
