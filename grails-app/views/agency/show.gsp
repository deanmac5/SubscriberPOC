
<%@ page import="subscriberpoc.Agency" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'agency.label', default: 'Agency')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-agency" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-agency" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list agency">
			
				<g:if test="${agencyInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="agency.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${agencyInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${agencyInstance?.portfolio}">
				<li class="fieldcontain">
					<span id="portfolio-label" class="property-label"><g:message code="agency.portfolio.label" default="Portfolio" /></span>
					
						<span class="property-value" aria-labelledby="portfolio-label"><g:fieldValue bean="${agencyInstance}" field="portfolio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${agencyInstance?.releases}">
				<li class="fieldcontain">
					<span id="releases-label" class="property-label"><g:message code="agency.releases.label" default="Releases" /></span>
					
						<g:each in="${agencyInstance.releases}" var="r">
						<span class="property-value" aria-labelledby="releases-label"><g:link controller="release" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${agencyInstance?.urls}">
				<li class="fieldcontain">
					<span id="urls-label" class="property-label"><g:message code="agency.urls.label" default="Urls" /></span>
					
						<span class="property-value" aria-labelledby="urls-label"><g:fieldValue bean="${agencyInstance}" field="urls"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:agencyInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${agencyInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
