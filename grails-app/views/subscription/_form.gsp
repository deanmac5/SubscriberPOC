<%@ page import="subscriberpoc.Subscription" %>



<div class="fieldcontain ${hasErrors(bean: subscriptionInstance, field: 'subscriber', 'error')} required">
	<label for="subscriber">
		<g:message code="subscription.subscriber.label" default="Subscriber" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="subscriber" name="subscriber.id" from="${subscriberpoc.Subscriber.list()}" optionKey="id" required="" value="${subscriptionInstance?.subscriber?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: subscriptionInstance, field: 'agencies', 'error')} ">
	<label for="agencies">
		<g:message code="subscription.agencies.label" default="Agencies" />
		
	</label>
	<g:select name="agencies" from="${subscriberpoc.Agency.list()}" multiple="multiple" optionKey="id" size="5" value="${subscriptionInstance?.agencies*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: subscriptionInstance, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="subscription.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${subscriptionInstance?.created}"  />

</div>

