<%@ page import="subscriberpoc.Topic" %>



<div class="fieldcontain ${hasErrors(bean: topicInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="topic.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" maxlength="100" required="" value="${topicInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: topicInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="topic.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${topicInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: topicInstance, field: 'agencies', 'error')} ">
	<label for="agencies">
		<g:message code="topic.agencies.label" default="Agencies" />
		
	</label>
	<g:select name="agencies" from="${subscriberpoc.Agency.list()}" multiple="multiple" optionKey="id" size="5" value="${topicInstance?.agencies*.id}" class="many-to-many"/>

</div>

