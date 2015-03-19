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
		<g:message code="subscriber.subscriptions.label" default="Agency subscriptions:" />

	</label>
    <g:each in="${subscriberpoc.Agency.list()}" var="agency">
        ${agency.title}
        <g:checkBox name="agency[${i}]?.id"  value="${agency.title}" checked="${}"  class="many-to-many"/>

    </g:each>


</div>

