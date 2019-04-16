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

<display:table pagesize="5" class="displaytag" name="curriculas" requestURI="${requestURI}" id="row">

	<spring:message code="personalData.name" var="name" />
	<display:column property="personalData.name" title="${name}" />
	
	<spring:message code="personalData.statement" var="statement" />
	<display:column property="personalData.statement" title="${statement}" />
	
	<spring:message code="curricula.show" var="showH" />
	<display:column title="${showH}">
		<acme:button url="curricula/hacker/show.do?curriculaId=${row.id}" code="button.show" />
	</display:column>
	
	<spring:message code="curricula.delete" var="deleteH" />
	<display:column title="${deleteH}">
		<acme:button url="curricula/hacker/delete.do?curriculaId=${row.id}" code="button.delete" />
	</display:column>
			
</display:table>


<acme:button url="curricula/hacker/create.do" code="button.create" />
