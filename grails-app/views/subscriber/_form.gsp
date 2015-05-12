<%@ page import="subscriberpoc.Subscriber" %>



<div class="fieldcontain ${hasErrors(bean: subscriberInstance, field: 'email', 'error')} required">
	<label for="email">
		<g:message code="subscriber.email.label" default="Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="email" name="email" required="" value="${subscriberInstance?.email}"/>

</div>



<div class="fieldcontain ${hasErrors(bean: subscriberInstance, field: 'subscriptions', 'error')} ">
	<label for="subscriptions">
		<g:message code="subscriber.subscriptions.label" default="Agency subscriptions:" />

	</label>
	<g:each in="${subscriberpoc.Agency.list()}" var="agency" status="i">
		${agency.title}
		<g:checkBox name="agency" value="${agency.id}" class="many-to-many"/>

	</g:each>


</div>

