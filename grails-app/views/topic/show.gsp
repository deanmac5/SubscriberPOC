
<%@ page import="subscriberpoc.Topic" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'topic.label', default: 'Topic')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-topic" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-topic" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list topic">
			
				<g:if test="${topicInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="topic.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${topicInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${topicInstance?.agencies}">
				<li class="fieldcontain">
					<span id="agencies-label" class="property-label"><g:message code="topic.agencies.label" default="Agencies" /></span>
					
						<g:each in="${topicInstance.agencies}" var="a">
						<span class="property-value" aria-labelledby="agencies-label"><g:link controller="agency" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${topicInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="topic.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${topicInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:topicInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${topicInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
