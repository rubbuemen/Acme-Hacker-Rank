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

<spring:message code="actor.confirm.phone" var="confirmPhone" />
<form:form id="form" action="${actionURL}" modelAttribute="actorForm" onsubmit="return checkPhone('${confirmPhone}');">

	<form:hidden path="actor.id" />
	<form:hidden path="actor.version" />

	<fieldset>
		<legend><spring:message code="actor.userAccount"/></legend>
		<jstl:choose>
			<jstl:when test="${actorForm.actor.id != 0}">
				<acme:textbox path="actor.userAccount.username" code="actor.username" readonly="true" />
			</jstl:when>
			<jstl:otherwise>
				<acme:textbox path="actor.userAccount.username" code="actor.username" placeholder="LoremIpsum" />
			</jstl:otherwise>
		</jstl:choose>
		<br />
	
		<jstl:if test="${actorForm.actor.id == 0}">
			<acme:password code="actor.password" path="actor.userAccount.password" placeholder="Lorem Ipsum" />
			<br />
			<acme:password code="actor.password.confirm" path="passwordCheck" placeholder="Lorem Ipsum"  />
			<br />
		</jstl:if>
	</fieldset>

	<fieldset>
		<legend><spring:message code="actor.personalData"/></legend>
		<acme:textbox code="actor.name" path="actor.name" placeholder="Lorem Ipsum"/>
		<br />
	
		<acme:textbox code="actor.surnames" path="actor.surnames" placeholder="Lorem Ipsum" />
		<br />
		
		<acme:textbox code="actor.VATNumber" path="actor.VATNumber" placeholder="Lorem Ipsum" />
		<br />
	
		<acme:textbox code="actor.photo" path="actor.photo" placeholder="http://LoremIpsum.com" type="url" />
		<jstl:if test="${not empty actorForm.actor.photo}">
			<br />
			<img src="<jstl:out value='${actorForm.actor.photo}' />" />
			<br />
		</jstl:if>
		<br />
	
		<acme:textbox code="actor.email" path="actor.email" placeholder="Lorem Ipsum" type="email" />
		<br />
	
		<acme:textbox code="actor.phoneNumber" path="actor.phoneNumber" placeholder="+999 (999) 999999999" type="tel"/>
		<br />
	
		<acme:textbox code="actor.address" path="actor.address" placeholder="Lorem Ipsum" />
		<br />
	
		<jstl:if test="${authority == 'COMPANY'}">
			<acme:textbox code="company.commercialName" path="actor.commercialName" placeholder="Lorem Ipsum" />
			<br />
		</jstl:if>
	</fieldset>
	
	<fieldset>
		<legend><spring:message code="actor.creditCard"/></legend>
		<acme:textbox code="actor.creditCard.holder" path="actor.creditCard.holder" placeholder="Lorem Ipsum" />
		<br />

		<acme:textbox code="actor.creditCard.make" path="actor.creditCard.make"	placeholder="Lorem Ipsum" />
		<br />
		
		<acme:textbox code="actor.creditCard.number" path="actor.creditCard.number"	placeholder="NNNNNNNNNNNNNNNN" />
		<br />
		
		<acme:textbox code="actor.creditCard.expirationMonth" path="actor.creditCard.expirationMonth" placeholder="MM" type="number" min="1" max="12" />
		<br />
		
		<acme:textbox code="actor.creditCard.expirationYear" path="actor.creditCard.expirationYear" placeholder="YY" type="number" min="0" max="99" />
		<br />
		
		<acme:textbox code="actor.creditCard.cvv" path="actor.creditCard.cvv" placeholder="NNN" type="number" min="100" max="999" />
		<br />
	</fieldset>

	<jstl:if test="${actorForm.actor.id == 0}">
		<acme:checkbox code="actor.legal.accept" path="termsConditions" />
		<a href="welcome/legal.do"><spring:message code="actor.legal.moreinfo" /></a>
		<br /><br />
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${actorForm.actor.id == 0}">
			<acme:submit name="save" code="button.register"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save"/>
		</jstl:otherwise>
	</jstl:choose>
	<acme:cancel url="welcome/index.do" code="button.cancel" />

</form:form>