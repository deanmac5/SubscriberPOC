<div>
    <h1> Email Confirmation</h1>

    <g:link absolute="true" controller="subscriber" action="confirm" id="${code}">Click this link to confirm your account</g:link>

    <g:createLink absolute="true" controller="subscriber" action="confirm" id="${code}"></g:createLink>
</div>