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

<display:table pagesize="5" class="displaytag" name="positions" requestURI="${requestURI}" id="row">

	<spring:message code="position.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" />
	
	<spring:message code="position.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="position.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="position.deadline" var="deadline" />
	<display:column title="${deadline}">
			<fmt:formatDate var="format" value="${row.deadline}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="position.skills" var="skills" />
	<display:column title="${skills}" >
	<ul>
	<jstl:forEach items="${row.skills}" var="skill">
		<li><jstl:out value="${skill}"/></li>
	</jstl:forEach>
	</ul>
	</display:column>
	
	<spring:message code="position.technologies" var="technologies" />
	<display:column title="${technologies}" >
	<ul>
	<jstl:forEach items="${row.technologies}" var="technology">
		<li><jstl:out value="${technology}"/></li>
	</jstl:forEach>
	</ul>
	</display:column>
	
	<spring:message code="position.salary" var="salary" />
	<display:column property="salary" title="${salary}" />
	
	<spring:message code="position.profile" var="profile" />
	<display:column property="profile" title="${profile}" />
	
	<spring:message code="position.edit" var="editH" />
	<display:column title="${editH}" >
		<jstl:if test="${!row.isFinalMode}">
			<acme:button url="position/company/edit.do?positionId=${row.id}" code="button.edit" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="position.delete" var="deleteH" />
	<display:column title="${deleteH}" >
		<jstl:if test="${!row.isFinalMode}">
			<acme:button url="position/company/delete.do?positionId=${row.id}" code="button.delete" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="position.changeFinalMode" var="changeFinalModeH" />
	<display:column title="${changeFinalModeH}" >
		<jstl:choose>
			<jstl:when test="${!row.isFinalMode}">
				<acme:button url="position/company/change.do?positionId=${row.id}" code="button.change" />
			</jstl:when>
			<jstl:when test="${row.isFinalMode}">
				<spring:message code="position.isFinalMode" />
			</jstl:when>
		</jstl:choose>
	</display:column>
	
	<spring:message code="position.cancel" var="cancelH" />
	<display:column title="${cancelH}" >
		<jstl:choose>
			<jstl:when test="${row.isFinalMode and !row.isCancelled}">
				<acme:button url="position/company/cancel.do?positionId=${row.id}" code="button.cancel" />
			</jstl:when>
			<jstl:when test="${row.isCancelled}">
				<spring:message code="position.cancelled" />
			</jstl:when>
		</jstl:choose>
	</display:column>
			
</display:table>


<acme:button url="position/company/create.do" code="button.create" />
