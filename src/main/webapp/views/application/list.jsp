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

<display:table pagesize="5" class="displaytag" name="applications" requestURI="${requestURI}" id="row">

	<spring:message code="application.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="application.explications" var="explications" />
	<display:column property="explications" title="${explications}" />
	
	<spring:message code="application.codeLink" var="codeLink" />
	<display:column title="${codeLink}" >
		<a href="<jstl:out value="${row.codeLink}"/>"><jstl:out value="${row.codeLink}"/></a>
	</display:column>
	
	<spring:message code="application.momentSubmit" var="momentSubmit" />
	<display:column title="${momentSubmit}">
			<fmt:formatDate var="format2" value="${row.momentSubmit}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format2}" />
	</display:column>
	
	<spring:message code="application.status" var="status" />
	<display:column property="status" title="${status}" />
	
	<security:authorize access="hasRole('COMPANY')">	
		<spring:message code="application.position" var="position" />
		<display:column title="${position}">
			<acme:button url="position/company/show.do?positionId=${row.position.id}" code="button.show" />
		</display:column>
		
		<spring:message code="application.problem" var="problem" />
		<display:column title="${problem}">
			<acme:button url="problem/company/show.do?problemId=${row.problem.id}" code="button.show" />
		</display:column>
		
		<spring:message code="curricula.c" var="showH" />
		<display:column title="${showH}">
			<acme:button url="curricula/company/show.do?curriculaId=${row.curricula.id}" code="button.show" />
		</display:column>
	
		<spring:message code="application.decideApplication" var="decideApplication" />
		<display:column title="${decideApplication}">
			<jstl:if test="${row.status eq 'SUBMITTED'}">
				<acme:button url="application/company/accept.do?applicationId=${row.id}" code="button.accept" />
				<acme:button url="application/company/reject.do?applicationId=${row.id}" code="button.reject" />
			</jstl:if>	
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('HACKER')">	
		<spring:message code="application.position" var="position" />
		<display:column title="${position}">
			<acme:button url="position/hacker/show.do?positionId=${row.position.id}" code="button.show" />
		</display:column>
		
		<spring:message code="application.problem" var="problem" />
		<display:column title="${problem}">
			<acme:button url="problem/hacker/show.do?problemId=${row.problem.id}" code="button.show" />
		</display:column>
		
		<spring:message code="curricula.show" var="showH" />
		<display:column title="${showH}">
			<acme:button url="curricula/hacker/show.do?curriculaId=${row.curricula.id}" code="button.show" />
		</display:column>
		
		<spring:message code="application.submitSolution" var="submitSolution" />
		<display:column title="${submitSolution}">
			<jstl:if test="${row.status eq 'PENDING'}">
				<acme:button url="application/hacker/edit.do?applicationId=${row.id}" code="button.submit" />
			</jstl:if>	
		</display:column>
	</security:authorize>
</display:table>

<security:authorize access="hasRole('HACKER')">	
	<acme:button url="application/hacker/create.do" code="button.create" />
</security:authorize>
