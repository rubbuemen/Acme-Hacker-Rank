<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<a href="welcome/index.do"><img src="${bannerUrl}" alt="${nameSystem} Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->		
		<security:authorize access="isAnonymous()">
		<li><a class="fNiv" href="position/listGeneric.do"><spring:message code="master.page.positionsAvailables" /></a></li>
		<li><a class="fNiv" href="company/list.do"><spring:message code="master.page.companies" /></a></li>
			<li><a class="fNiv"><spring:message code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/register-company.do"><spring:message code="master.page.register.company" /></a></li>
					<li><a href="actor/register-hacker.do"><spring:message code="master.page.register.hacker" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('COMPANY')">
			<li>
				<a class="fNiv"><spring:message code="master.page.company" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="position/company/list.do"><spring:message code="master.page.positions" /></a></li>
					<li><a href="problem/company/list.do"><spring:message code="master.page.problems" /></a></li>
					<li><a href="application/company/list.do"><spring:message code="master.page.applications" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('HACKER')">
			<li>
				<a class="fNiv"><spring:message code="master.page.hacker" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="application/hacker/list.do"><spring:message code="master.page.applications" /></a></li>
					<li><a href="curricula/hacker/list.do"><spring:message code="master.page.curriculas" /></a></li>
					<li><a href="finder/hacker/edit.do"><spring:message code="master.page.finder" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		
		<security:authorize access="hasRole('ADMIN')">
			<li>
				<a class="fNiv"><spring:message code="master.page.admin" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/administrator/register-administrator.do"><spring:message code="master.page.register.admin" /></a></li>
					<li><a href="dashboard/administrator/show.do"><spring:message code="master.page.dashboard" /></a></li>	
					<li><a href="systemConfiguration/administrator/show.do"><spring:message code="master.page.systemConfiguration" /></a></li>	
					<li><a href="systemConfiguration/administrator/actorsList.do"><spring:message code="master.page.actorsList" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
		<li><a class="fNiv" href="position/listGeneric.do"><spring:message code="master.page.positionsAvailables" /></a></li>
		<li><a class="fNiv" href="company/list.do"><spring:message code="master.page.companies" /></a></li>
		<li><a class="fNiv" href="socialProfile/list.do"><spring:message code="master.page.socialProfiles" /></a></li>
		<li><a class="fNiv" href="message/list.do"><spring:message code="master.page.messages" /></a></li>
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('COMPANY')">
						<li><a href="actor/company/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('HACKER')">
						<li><a href="actor/hacker/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="actor/administrator/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<spring:message code="master.page.delete.account.confirm" var="confirm" />
					<security:authorize access="!hasRole('ADMIN')">				
						<li><a href="actor/delete.do" onClick="return checkPosting('${confirm}');" ><spring:message code="master.page.delete.account" /> </a></li>
					</security:authorize>	
					<li><a href="actor/export.do" ><spring:message code="master.page.export.data" /> </a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>