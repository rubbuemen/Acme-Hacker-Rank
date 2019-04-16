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

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="curricula.personalData"/></summary>

<display:table class="displaytag" name="personalData" id="row1">
	<spring:message code="personalData.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="personalData.statement" var="statement" />
	<display:column property="statement" title="${statement}" />
	
	<spring:message code="personalData.phoneNumber" var="phoneNumber" />
	<display:column property="phoneNumber" title="${phoneNumber}" />
	
	<spring:message code="personalData.gitHubProfile" var="gitHubProfile" />
	<display:column title="${gitHubProfile}" >
		<a href="<jstl:out value="${row1.gitHubProfile}"/>"><jstl:out value="${row1.gitHubProfile}"/></a>
	</display:column>
	
	<spring:message code="personalData.linkedInProfile" var="linkedInProfile" />
	<display:column title="${linkedInProfile}" >
		<a href="<jstl:out value="${row1.linkedInProfile}"/>"><jstl:out value="${row1.linkedInProfile}"/></a>
	</display:column>

	<jstl:if test="${authority == 'HACKER'}">
		<spring:message code="personalData.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="personalData/hacker/edit.do?personalDataId=${row1.id}&curriculaId=${curricula.id}" code="button.edit" />
		</display:column>
	</jstl:if>
	
</display:table>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="curricula.positionDatas"/></summary>

<display:table class="displaytag" name="positionDatas" id="row2">
	<spring:message code="positionData.title" var="title" />
	<display:column property="title" title="${title}" />
	
	<spring:message code="positionData.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="positionData.startDate" var="startDate" />
	<display:column title="${startDate}">
			<fmt:formatDate var="format1" value="${row2.startDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format1}" />
	</display:column>
	
	<spring:message code="positionData.endDate" var="endDate" />
	<display:column title="${endDate}">
			<fmt:formatDate var="format2" value="${row2.endDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format2}" />
	</display:column>
	
	<jstl:if test="${authority == 'HACKER'}">
		<spring:message code="positionData.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="positionData/hacker/edit.do?positionDataId=${row2.id}&curriculaId=${curricula.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="positionData.delete" var="deleteH" />
		<display:column title="${deleteH}">
			<acme:button url="positionData/hacker/delete.do?positionDataId=${row2.id}&curriculaId=${curricula.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${authority == 'HACKER'}">
	<acme:button url="positionData/hacker/create.do?curriculaId=${curricula.id}" code="button.create" />
</jstl:if>


</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="curricula.educationDatas"/></summary>

<display:table class="displaytag" name="educationDatas" id="row3">
	<spring:message code="educationData.degree" var="degree" />
	<display:column property="degree" title="${degree}" />
	
	<spring:message code="educationData.institution" var="institution" />
	<display:column property="institution" title="${institution}" />
	
	<spring:message code="educationData.mark" var="mark" />
	<display:column property="mark" title="${mark}" />
	
	<spring:message code="educationData.startDate" var="startDate2" />
	<display:column title="${startDate2}">
			<fmt:formatDate var="format3" value="${row3.startDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format3}" />
	</display:column>
	
	<spring:message code="educationData.endDate" var="endDate2" />
	<display:column title="${endDate2}">
			<fmt:formatDate var="format4" value="${row3.endDate}" pattern="dd/MM/YYYY" />
			<jstl:out value="${format4}" />
	</display:column>
	
	<jstl:if test="${authority == 'HACKER'}">
		<spring:message code="educationData.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="educationData/hacker/edit.do?educationDataId=${row3.id}&curriculaId=${curricula.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="educationData.delete" var="deleteH" />
		<display:column title="${deleteH}">
			<acme:button url="educationData/hacker/delete.do?educationDataId=${row3.id}&curriculaId=${curricula.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${authority == 'HACKER'}">
	<acme:button url="educationData/hacker/create.do?curriculaId=${curricula.id}" code="button.create" />
</jstl:if>

</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="curricula.miscellaneousDatas"/></summary>

<display:table class="displaytag" name="miscellaneousDatas" id="row4">
	<spring:message code="miscellaneousData.text" var="text" />
	<display:column property="text" title="${text}" />
	
	<spring:message code="miscellaneousData.attachments" var="attachments" />
	<display:column property="attachments" title="${attachments}" />
	
	<jstl:if test="${authority == 'HACKER'}">	
		<spring:message code="positionData.edit" var="editH" />
		<display:column title="${editH}">
			<acme:button url="miscellaneousData/hacker/edit.do?miscellaneousDataId=${row4.id}&curriculaId=${curricula.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="miscellaneousData.delete" var="deleteH" />
		<display:column title="${deleteH}">
			<acme:button url="miscellaneousData/hacker/delete.do?miscellaneousDataId=${row4.id}&curriculaId=${curricula.id}" code="button.delete" />
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${authority == 'HACKER'}">
	<acme:button url="miscellaneousData/hacker/create.do?curriculaId=${curricula.id}" code="button.create" />
</jstl:if>

</details><br/>

<jstl:choose>
<jstl:when test="${authority == 'HACKER'}">
	<acme:button url="curricula/hacker/list.do" code="button.back" />
</jstl:when>
<jstl:otherwise>
	<security:authorize access="hasRole('HACKER')">
		<acme:button url="application/hacker/list.do" code="button.back" />
	</security:authorize>
		<security:authorize access="hasRole('COMPANY')">
		<acme:button url="application/company/list.do" code="button.back" />
	</security:authorize>
</jstl:otherwise>
</jstl:choose>
