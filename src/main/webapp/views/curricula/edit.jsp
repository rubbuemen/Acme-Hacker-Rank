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

<form:form action="${actionURL}" modelAttribute="curricula">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<fieldset>
	    <legend><spring:message code="curricula.personalData"/></legend>
	    <acme:textbox code="personalData.name" path="personalData.name" placeholder="Lorem Ipsum"/>
		<br />
	
		<acme:textbox code="personalData.statement" path="personalData.statement" placeholder="Lorem Ipsum"/>
		<br />
		
		<acme:textbox code="personalData.phoneNumber" path="personalData.phoneNumber" placeholder="+999 (999) 999999999" type="tel"/>
		<br />
		
		<acme:textbox code="personalData.gitHubProfile" path="personalData.gitHubProfile" placeholder="http://LoremIpsum.com" type="url" />
		<br />
		
		<acme:textbox code="personalData.linkedInProfile" path="personalData.linkedInProfile" placeholder="http://LoremIpsum.com" type="url" />
		<br />
	</fieldset>
	
	<br /><br />
	<spring:message code="curricula.createMoreDatas"/>.
	<br /><br />
	
	<acme:submit name="save" code="button.register" />
	<acme:cancel url="curricula/hacker/list.do" code="button.cancel" />

</form:form>