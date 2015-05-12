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
		<g:message code="subscriber.subscriptions.label" default="Subscriptions" />
		
	</label>
	<g:select name="subscriptions" from="${subscriberpoc.Agency.list()}" multiple="multiple" optionKey="id" size="5" value="${subscriberInstance?.subscriptions*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: subscriberInstance, field: 'verified', 'error')} ">
	<label for="verified">
		<g:message code="subscriber.verified.label" default="Verified" />
		
	</label>
	<g:checkBox name="verified" value="${subscriberInstance?.verified}" />

</div>

