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
	<spring:message code="problem.title" var="title" />
	<li><b>${title}:</b> <jstl:out value="${problem.title}" /></li>
	
	<spring:message code="problem.statement" var="statement" />
	<li><b>${statement}:</b> <jstl:out value="${problem.statement}" /></li>
	
	<jstl:if test="${not empty problem.hint}">
		<spring:message code="problem.hint" var="hint" />
		<li><b>${hint}:</b> <jstl:out value="${problem.hint}" /></li>
	</jstl:if>
	
	<jstl:if test="${not empty problem.attachments}">
		<spring:message code="problem.attachments" var="attachments" />
		<li><b>${attachments}:</b> <jstl:out value="${problem.attachments}" /></li>
	</jstl:if>
</ul>

<security:authorize access="hasRole('COMPANY')">	
	<acme:button url="application/company/list.do" code="button.back" />
</security:authorize>

<security:authorize access="hasRole('HACKER')">	
	<acme:button url="application/hacker/list.do" code="button.back" />
</security:authorize>