<%@ page import="subscriberpoc.Site" %>



<div class="fieldcontain ${hasErrors(bean: siteInstance, field: 'agency', 'error')} required">
	<label for="agency">
		<g:message code="site.agency.label" default="Agency" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="agency" name="agency.id" from="${subscriberpoc.Agency.list()}" optionKey="id" required="" value="${siteInstance?.agency?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: siteInstance, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="site.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="created" required="" value="${siteInstance?.created}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: siteInstance, field: 'createdRegex', 'error')} required">
	<label for="createdRegex">
		<g:message code="site.createdRegex.label" default="Created Regex" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="createdRegex" required="" value="${siteInstance?.createdRegex}"/>
    <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">See here for assistance</a>
</div>

<div class="fieldcontain ${hasErrors(bean: siteInstance, field: 'description', 'error')} required">
	<label for="description">
		<g:message code="site.description.label" default="Description" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="description" required="" value="${siteInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: siteInstance, field: 'releases', 'error')} ">
	<label for="releases">
		<g:message code="site.releases.label" default="Releases" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${siteInstance?.releases?}" var="r">
    <li><g:link controller="release" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="release" action="create" params="['site.id': siteInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'release.label', default: 'Release')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: siteInstance, field: 'url', 'error')} required">
	<label for="url">
		<g:message code="site.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="url" required="" value="${siteInstance?.url}"/>

</div>

