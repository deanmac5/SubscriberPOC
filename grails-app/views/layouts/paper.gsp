<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
  		<asset:stylesheet src="bootstrap.min.css"/>
		<asset:javascript src="application.js"/>
		<g:layoutHead/>
	</head>
	<body>
		%{--<div id="grailsLogo" role="banner"><a href="http://grails.org"><asset:image src="grails_logo.png" alt="Grails"/></a>--}%

	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="${createLink(uri:'/')}">Australian Government Media Release Subscription Service</a>
				</div>
            <sec:ifNotLoggedIn>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${createLink(uri:'/login')}">Login</a></li>
                </ul>
            </sec:ifNotLoggedIn>


			</div>
		</div>
	</nav>


			%{--<sec:ifNotLoggedIn>--}%
				%{--<g:form name="loginForm" controller="login" action="index">--}%
					%{--<g:submitButton name="login" value="Log In"/>--}%
				%{--</g:form>--}%
			%{--</sec:ifNotLoggedIn>--}%
	          %{--<sec:ifLoggedIn>--}%

				  %{--<g:form name="logoutForm" controller="logout" action="index">--}%
					  %{--Currently logged in as <sec:username/>--}%
					  %{--<g:submitButton name="signOut" value="Sign Out"/>--}%
				  %{--</g:form>--}%
			  %{--</sec:ifLoggedIn>--}%
		%{--</div>--}%
		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
	</body>
</html>