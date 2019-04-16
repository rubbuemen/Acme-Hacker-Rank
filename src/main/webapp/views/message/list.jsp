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

<display:table pagesize="5" class="displaytag" name="messages" requestURI="${requestURI}" id="row">
	<spring:message code="message.subject" var="subject" />
	<display:column property="subject" title="${subject}" />
	
	<spring:message code="message.tags" var="tags" />
	<display:column property="tags" title="${tags}" />
	
	<spring:message code="message.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="message.show" var="showH" />
	<display:column title="${showH}">
		<acme:button url="message/show.do?messageId=${row.id}" code="button.show" />
	</display:column>
	
	<spring:message code="message.delete" var="deleteH" />
	<display:column title="${deleteH}">
		<acme:button url="message/delete.do?messageId=${row.id}" code="button.delete" />
	</display:column>
</display:table>

<acme:button url="message/create.do" code="button.sendMessage" />
<security:authorize access="hasRole('ADMIN')">
	<acme:button url="message/create.do?broadcast=true" code="button.sendBroadcast" />
</security:authorize>
