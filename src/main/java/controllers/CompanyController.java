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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.CompanyService;
import domain.Company;

@Controller
@RequestMapping("/company")
public class CompanyController extends AbstractController {

	@Autowired
	CompanyService	companyService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listCompanies() {
		ModelAndView result;
		Collection<Company> companies;

		companies = this.companyService.findAll();

		result = new ModelAndView("company/list");

		result.addObject("companies", companies);
		result.addObject("requestURI", "company/list.do");

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showCompany(@RequestParam final int companyId) {
		ModelAndView result;
		Company actor;

		actor = this.companyService.findOne(companyId);

		result = new ModelAndView("actor/show");

		result.addObject("actor", actor);
		result.addObject("authority", Authority.COMPANY);

		return result;
	}
}
