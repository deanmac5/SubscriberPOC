<%@ page import="subscriberpoc.Subscriber" %>



<div class="fieldcontain ${hasErrors(bean: subscriberInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="subscriber.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" maxlength="100" required="" value="${subscriberInstance?.name}"/>

</div>

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
	
<ul class="one-to-many">
<g:each in="${subscriberInstance?.subscriptions?}" var="s">
    <li><g:link controller="subscription" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="subscription" action="create" params="['subscriber.id': subscriberInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'subscription.label', default: 'Subscription')])}</g:link>
</li>
</ul>


</div>

