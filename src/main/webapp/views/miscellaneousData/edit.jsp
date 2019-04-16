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

<form:form action="${actionURL}" modelAttribute="miscellaneousData">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textarea code="miscellaneousData.text" path="text" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa"/>
	<br />

	<acme:textarea code="miscellaneousData.attachments" path="attachments" placeholder="http://Loremipsum.com, http://Loremipsum.com, ..." />
	<br />
	

	<jstl:choose>
		<jstl:when test="${miscellaneousData.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	
	<acme:cancel url="curricula/hacker/show.do?curriculaId=${curricula.id}" code="button.cancel" />
</form:form>