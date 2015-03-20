
<%@ page import="subscriberpoc.Release" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'release.label', default: 'Release')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-release" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-release" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list release">
			
				<g:if test="${releaseInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="release.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${releaseInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="release.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><a href="<g:fieldValue bean="${releaseInstance}" field="url"/>"><g:fieldValue bean="${releaseInstance}" field="url"/></a></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.snippet}">
				<li class="fieldcontain">
					<span id="snippet-label" class="property-label"><g:message code="release.snippet.label" default="Snippet" /></span>
					
						<span class="property-value" aria-labelledby="snippet-label"><g:fieldValue bean="${releaseInstance}" field="snippet"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="release.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${releaseInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.isMediaRelease}">
				<li class="fieldcontain">
					<span id="isMediaRelease-label" class="property-label"><g:message code="release.isMediaRelease.label" default="Is Media Release" /></span>
					
						<span class="property-value" aria-labelledby="isMediaRelease-label"><g:formatBoolean boolean="${releaseInstance?.isMediaRelease}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.releaseDate}">
				<li class="fieldcontain">
					<span id="releaseDate-label" class="property-label"><g:message code="release.releaseDate.label" default="Release Date" /></span>
					
						<span class="property-value" aria-labelledby="releaseDate-label"><g:formatDate date="${releaseInstance?.releaseDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.site}">
				<li class="fieldcontain">
					<span id="site-label" class="property-label"><g:message code="release.site.label" default="Site" /></span>
					
						<span class="property-value" aria-labelledby="site-label"><g:link controller="site" action="show" id="${releaseInstance?.site?.id}">${releaseInstance?.site?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:releaseInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${releaseInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
