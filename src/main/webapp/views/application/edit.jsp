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

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${actionURL}" modelAttribute="application">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<jstl:choose>
		<jstl:when test="${application.id == 0}">
			<acme:select items="${positions}" itemLabel="ticker" code="application.position" path="position"/>
			<br />
			
			<acme:select items="${curriculas}" itemLabel="personalData.name" code="application.curricula" path="curricula"/>
			<br />
		</jstl:when>
		<jstl:otherwise>
			<acme:textarea code="application.explications" path="explications" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa" />
			<br />
			
			<acme:textbox code="application.codeLink" path="codeLink" placeholder="http://LoremIpsum.com" type="url" />
			<br />
		</jstl:otherwise>
	</jstl:choose>

	<jstl:choose>
		<jstl:when test="${application.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	
	<acme:cancel url="application/hacker/list.do" code="button.cancel" />
</form:form>