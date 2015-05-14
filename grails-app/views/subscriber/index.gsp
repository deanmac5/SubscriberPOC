<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'subscriber.label', default: 'Subscriber')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-subscriber" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div id="create-subscriber" class="content scaffold-create" role="main">

		<h1>Subscribe for Media Release Notifications</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>


			<g:hasErrors bean="${subscriberInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${subscriberInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form url="[resource:subscriberInstance, action:'signup']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">

					<g:submitButton name="Subscribe" class="btn btn-primary"
									value="${message(code: 'subscribe.button.create.label', default: 'Subscribe')}"/>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
