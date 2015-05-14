<%@ page import="subscriberpoc.Subscriber" %>



<div class="fieldcontain ${hasErrors(bean: subscriberInstance, field: 'email', 'error')} required">
    <label for="email">
        <g:message code="subscriber.email.label" default="Email"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field type="email" name="email" required="" value="${subscriberInstance?.email}"/>

</div>


<div class="fieldcontain ${hasErrors(bean: subscriberInstance, field: 'subscriptions', 'error')} ">
    <label for="subscriptions">
        <g:message code="subscriber.subscriptions.label" default="Topics:"/>

    </label>
    <g:each in="${subscriberpoc.Topic.list()}" var="topic" status="i">
        <p>
            <g:checkBox name="topic" value="${topic.id}" class="many-to-many"/>
            ${topic.name}
        </p>

    </g:each>

</div>

