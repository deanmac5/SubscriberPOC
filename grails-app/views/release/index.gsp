
<%@ page import="subscriberpoc.Release" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'release.label', default: 'Release')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-release" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-release" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="title" title="${message(code: 'release.title.label', default: 'Title')}" />
					
						<g:sortableColumn property="url" title="${message(code: 'release.url.label', default: 'Url')}" />
					
						<g:sortableColumn property="snippet" title="${message(code: 'release.snippet.label', default: 'Snippet')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'release.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="isMediaRelease" title="${message(code: 'release.isMediaRelease.label', default: 'Is Media Release')}" />
					
						<g:sortableColumn property="releaseDate" title="${message(code: 'release.releaseDate.label', default: 'Release Date')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${releaseInstanceList}" status="i" var="releaseInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${releaseInstance.id}">${fieldValue(bean: releaseInstance, field: "title")}</g:link></td>
					
						<td><a href="${fieldValue(bean: releaseInstance, field: "url")}">${fieldValue(bean: releaseInstance, field: "url")}</a></td>
					
						<td>${fieldValue(bean: releaseInstance, field: "snippet")}</td>
					
						<td><g:formatDate date="${releaseInstance.dateCreated}" /></td>
					
						<td><g:formatBoolean boolean="${releaseInstance.isMediaRelease}" /></td>
					
						<td><g:formatDate date="${releaseInstance.releaseDate}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${releaseInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
