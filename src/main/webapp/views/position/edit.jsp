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

<form:form action="${actionURL}" modelAttribute="position">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="position.title" path="title" placeholder="Lorem Ipsum"/>
	<br />

	<acme:textbox code="position.description" path="description" placeholder="Lorem Ipsum"/>
	<br />
	
	<acme:textbox code="position.deadline" path="deadline" placeholder="dd/MM/yyyy"  />
	<br />
	
	<acme:textarea code="position.skills" path="skills" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
	<br />
	
	<acme:textarea code="position.technologies" path="technologies" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
	<br />
	
	<acme:textbox code="position.salary" path="salary" placeholder="NNNNNN.NN" type="number" min="0" step="0.01" />
	<br />
	
	<acme:textbox code="position.profile" path="profile" placeholder="Lorem Ipsum"/>
	<br />
	
	<acme:select items="${problems}" itemLabel="title" code="position.problems" path="problems" multiple="true" />
	<br />

	<jstl:choose>
		<jstl:when test="${position.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	
	<acme:cancel url="position/company/list.do" code="button.cancel" />
</form:form>