<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Page not found"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="container">
    <div class="error-page card">
        <span class="error-page__code">404</span>
        <h1>We couldn't find that page</h1>
        <p class="muted">The link may be broken, or the report may no longer exist.</p>
        <a href="${ctx}/" class="btn btn--primary"><ui:icon name="home" size="16"/> Go to home</a>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
