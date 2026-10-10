<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Log in"/>
<c:set var="activeNav" value="login"/>
<c:set var="bodyClass" value="auth-page"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <div class="auth">
        <aside class="auth__aside">
            <h2>Welcome back to ReClaim</h2>
            <p>Log in to report items, track your reports and see who may have found what you lost.</p>
            <ul class="auth__points">
                <li><ui:icon name="zap"/> Automatic matching of lost &amp; found reports</li>
                <li><ui:icon name="shield"/> Your contact details stay private</li>
                <li><ui:icon name="clock"/> Track the status of every report</li>
            </ul>
        </aside>

        <section class="auth__card card">
            <h1 class="auth__title">Log in</h1>
            <p class="muted">Use the email you registered with.</p>

            <c:if test="${not empty errors.login}">
                <div class="alert alert--error" role="alert"><ui:icon name="alert"/><span><c:out value="${errors.login}"/></span></div>
            </c:if>

            <form action="${ctx}/login" method="post" class="form" novalidate data-validate>
                <ui:csrf/>
                <div class="field">
                    <label for="email">Email address</label>
                    <div class="input-icon">
                        <ui:icon name="mail" size="16"/>
                        <input type="email" id="email" name="email" value="${fn:escapeXml(email)}" required
                               autocomplete="email" placeholder="you@example.com" ${empty email ? 'autofocus' : ''}>
                    </div>
                </div>
                <div class="field">
                    <label for="password">Password</label>
                    <div class="input-icon">
                        <ui:icon name="lock" size="16"/>
                        <input type="password" id="password" name="password" required autocomplete="current-password"
                               placeholder="Your password" ${empty email ? '' : 'autofocus'}>
                        <button type="button" class="input-icon__action" data-toggle-password="password" aria-label="Show password"><ui:icon name="eye" size="16"/></button>
                    </div>
                </div>
                <label class="checkbox">
                    <input type="checkbox" name="rememberEmail" ${remembered ? 'checked' : ''}>
                    <span>Remember my email on this device</span>
                </label>
                <button type="submit" class="btn btn--primary btn--block btn--lg">Log in <ui:icon name="arrow-right" size="16"/></button>
            </form>
            <p class="auth__switch">New to ReClaim? <a href="${ctx}/register">Create an account</a></p>
        </section>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
