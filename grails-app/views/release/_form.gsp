<%@ page import="subscriberpoc.Release" %>



<div class="fieldcontain ${hasErrors(bean: releaseInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="release.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${releaseInstance?.title}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: releaseInstance, field: 'url', 'error')} required">
	<label for="url">
		<g:message code="release.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="url" name="url" required="" value="${releaseInstance?.url}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: releaseInstance, field: 'snippet', 'error')} required">
	<label for="snippet">
		<g:message code="release.snippet.label" default="Snippet" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="snippet" cols="40" rows="5" maxlength="1024" required="" value="${releaseInstance?.snippet}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: releaseInstance, field: 'isMediaRelease', 'error')} ">
	<label for="isMediaRelease">
		<g:message code="release.isMediaRelease.label" default="Is Media Release" />
		
	</label>
	<g:checkBox name="isMediaRelease" value="${releaseInstance?.isMediaRelease}" />

</div>

<div class="fieldcontain ${hasErrors(bean: releaseInstance, field: 'releaseDate', 'error')} required">
	<label for="releaseDate">
		<g:message code="release.releaseDate.label" default="Release Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="releaseDate" precision="day"  value="${releaseInstance?.releaseDate}"  />

</div>

<div class="fieldcontain ${hasErrors(bean: releaseInstance, field: 'site', 'error')} required">
	<label for="site">
		<g:message code="release.site.label" default="Site" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="site" name="site.id" from="${subscriberpoc.Site.list()}" optionKey="id" required="" value="${releaseInstance?.site?.id}" class="many-to-one"/>

</div>

