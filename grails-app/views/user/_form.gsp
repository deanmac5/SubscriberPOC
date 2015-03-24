<%@ page import="subscriberpoc.User" %>



<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="user.username.label" default="UserName" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" maxlength="100" required="" value="${userInstance?.username}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'email', 'error')} required">
	<label for="email">
		<g:message code="user.email.label" default="Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="email" name="email" required="" value="${userInstance?.email}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'subscriptions', 'error')} ">
	<label for="subscriptions">
		<g:message code="user.subscriptions.label" default="Agency subscriptions:" />

	</label>
    <g:each in="${subscriberpoc.Agency.list()}" var="agency" status="i">
        ${agency.title}
        <g:checkBox name="agency" value="${agency.id}" class="many-to-many"/>

    </g:each>


</div>

