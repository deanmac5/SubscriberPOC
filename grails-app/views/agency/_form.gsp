<%@ page import="subscriberpoc.Agency" %>



<div class="fieldcontain ${hasErrors(bean: agencyInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="agency.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" maxlength="100" required="" value="${agencyInstance?.title}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: agencyInstance, field: 'portfolio', 'error')} required">
	<label for="portfolio">
		<g:message code="agency.portfolio.label" default="Portfolio" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="portfolio" required="" value="${agencyInstance?.portfolio}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: agencyInstance, field: 'releases', 'error')} ">
	<label for="releases">
		<g:message code="agency.releases.label" default="Releases" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${agencyInstance?.releases?}" var="r">
    <li><g:link controller="release" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="release" action="create" params="['agency.id': agencyInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'release.label', default: 'Release')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: agencyInstance, field: 'urls', 'error')} ">
	<label for="urls">
		<g:message code="agency.urls.label" default="Urls" />
		
	</label>
	

</div>

