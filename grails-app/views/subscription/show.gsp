
<%@ page import="subscriberpoc.Subscription" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'subscription.label', default: 'Subscription')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-subscription" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-subscription" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list subscription">
			
				<g:if test="${subscriptionInstance?.subscriber}">
				<li class="fieldcontain">
					<span id="subscriber-label" class="property-label"><g:message code="subscription.subscriber.label" default="Subscriber" /></span>
					
						<span class="property-value" aria-labelledby="subscriber-label"><g:link controller="subscriber" action="show" id="${subscriptionInstance?.subscriber?.id}">${subscriptionInstance?.subscriber?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${subscriptionInstance?.agencies}">
				<li class="fieldcontain">
					<span id="agencies-label" class="property-label"><g:message code="subscription.agencies.label" default="Agencies" /></span>
					
						<g:each in="${subscriptionInstance.agencies}" var="a">
						<span class="property-value" aria-labelledby="agencies-label"><g:link controller="agency" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${subscriptionInstance?.created}">
				<li class="fieldcontain">
					<span id="created-label" class="property-label"><g:message code="subscription.created.label" default="Created" /></span>
					
						<span class="property-value" aria-labelledby="created-label"><g:formatDate date="${subscriptionInstance?.created}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:subscriptionInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${subscriptionInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
