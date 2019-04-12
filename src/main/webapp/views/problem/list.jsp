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

<display:table pagesize="5" class="displaytag" name="problems" requestURI="${requestURI}" id="row">

	<spring:message code="problem.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="problem.statement" var="statement" />
	<display:column property="statement" title="${statement}" />
	
	<spring:message code="problem.hint" var="hint" />
	<display:column property="hint" title="${hint}" />
	
	<spring:message code="problem.attachments" var="attachments" />
	<display:column title="${attachments}" >
	<ul>
	<jstl:forEach items="${row.attachments}" var="attachment">
		<li><a href="<jstl:out value="${attachment}"/>"><jstl:out value="${attachment}"/></a></li>
	</jstl:forEach>
	</ul>
	</display:column>
	
	<spring:message code="problem.edit" var="editH" />
	<display:column title="${editH}" >
		<jstl:if test="${!row.isFinalMode}">
			<acme:button url="problem/company/edit.do?problemId=${row.id}" code="button.edit" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="problem.delete" var="deleteH" />
	<display:column title="${deleteH}" >
		<jstl:if test="${!row.isFinalMode}">
			<acme:button url="problem/company/delete.do?problemId=${row.id}" code="button.delete" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="problem.changeFinalMode" var="changeFinalModeH" />
	<display:column title="${changeFinalModeH}" >
		<jstl:choose>
			<jstl:when test="${!row.isFinalMode}">
				<acme:button url="problem/company/change.do?problemId=${row.id}" code="button.change" />
			</jstl:when>
			<jstl:when test="${row.isFinalMode}">
				<spring:message code="problem.isFinalMode" />
			</jstl:when>
		</jstl:choose>
	</display:column>
			
</display:table>

<acme:button url="problem/company/create.do" code="button.create" />
