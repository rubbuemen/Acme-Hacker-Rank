<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<ul style="list-style-type: disc;">
	<spring:message code="position.ticker" var="ticker" />
	<li><b>${ticker}:</b> <jstl:out value="${position.ticker}" /></li>
	
	<spring:message code="position.title" var="title" />
	<li><b>${title}:</b> <jstl:out value="${position.title}" /></li>
	
	<spring:message code="position.description" var="description" />
	<li><b>${description}:</b> <jstl:out value="${position.description}" /></li>
	
	<fmt:formatDate var="format" value="${position.deadline}" pattern="dd/MM/YYYY" />	
	<spring:message code="position.deadline" var="deadline" />
	<li><b>${deadline}:</b> <jstl:out value="${format}" /></li>
		
	<spring:message code="position.skills" var="skills" />
	<li><b>${skills}:</b> <jstl:out value="${position.skills}" /></li>
	
	<spring:message code="position.technologies" var="technologies" />
	<li><b>${technologies}:</b> <jstl:out value="${position.technologies}" /></li>
	
	<spring:message code="position.salary" var="salary" />
	<li><b>${salary}:</b> <jstl:out value="${position.salary}" /></li>
	
	<spring:message code="position.profile" var="profile" />
	<li><b>${profile}:</b> <jstl:out value="${position.profile}" /></li>
</ul>

<security:authorize access="hasRole('COMPANY')">	
	<acme:button url="application/company/list.do" code="button.back" />
</security:authorize>

<security:authorize access="hasRole('HACKER')">	
	<acme:button url="application/hacker/list.do" code="button.back" />
</security:authorize>