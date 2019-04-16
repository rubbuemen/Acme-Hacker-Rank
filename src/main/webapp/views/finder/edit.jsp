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

<form:form action="${actionURL}" modelAttribute="finder">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="finder.keyWord" path="keyWord" placeholder="Lorem Ipsum" />
	<br />
	
	<acme:textbox code="finder.deadline" path="deadline" placeholder="dd/MM/yyyy" />
	<br />
	
	<acme:textbox code="finder.minSalary" path="minSalary" placeholder="NNNNNN.NN" type="number" min="0" step="0.01" />
	<br />

	<acme:textbox code="finder.maxDeadline" path="maxDeadline" placeholder="dd/MM/yyyy" />
	<br />
	
	<acme:submit name="save" code="button.search" />
	<acme:button url="finder/hacker/clear.do" code="button.clear" />

</form:form>


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
			
</display:table>