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

<ul style="list-style-type: disc;">
	<spring:message code="actor.name" var="name" />
	<li><b>${name}:</b> <jstl:out value="${actor.name}" /></li>
	
	<spring:message code="actor.surnames" var="surnames" />
	<li><b>${surnames}:</b> <jstl:out value="${actor.surnames}" /></li>
	
	<spring:message code="actor.VATNumber" var="VATNumber" />
	<li><b>${VATNumber}:</b> <jstl:out value="${actor.VATNumber}" /></li>
	
	<jstl:if test="${not empty actor.photo}">
		<spring:message code="actor.photo" var="photo" />
		<li><b>${photo}:</b></li>
		<img src="<jstl:out value='${actor.photo}' />"  width="200px" height="200px" />
	</jstl:if>
	
	<spring:message code="actor.email" var="email" />
	<li><b>${email}:</b> <jstl:out value="${actor.email}" /></li>
	
	<jstl:if test="${not empty actor.phoneNumber}">
		<spring:message code="actor.phoneNumber" var="phoneNumber" />
		<li><b>${phoneNumber}:</b> <jstl:out value="${actor.phoneNumber}" /></li>
	</jstl:if>
	
	<jstl:if test="${not empty actor.address}">
		<spring:message code="actor.address" var="address" />
		<li><b>${address}:</b> <jstl:out value="${actor.address}" /></li>
	</jstl:if>
	
	<jstl:if test="${authority == 'COMPANY'}">
		<spring:message code="company.commercialName" var="commercialName" />
		<li><b>${commercialName}:</b> <jstl:out value="${actor.commercialName}" /></li>
	</jstl:if>

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="actor.isSpammer" var="isSpammer" />
		<li>
		<b>${isSpammer}: </b>
		<jstl:choose>
			<jstl:when test="${actor.isSpammer == true}">
				<spring:message code="actor.yes" var="yes"/>
				<jstl:out value="${yes}" />
			</jstl:when>
			<jstl:when test="${actor.isSpammer == false}">
				<spring:message code="actor.no" var="no"/>
				<jstl:out value="${no}" />
			</jstl:when>
			<jstl:otherwise>
			N/A
			</jstl:otherwise>
		</jstl:choose>
		</li>
	</security:authorize>
</ul>