<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Something went wrong"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%-- Details are written to the Tomcat log, never shown to users. --%>
<div class="container">
    <div class="error-page card">
        <span class="error-page__code">500</span>
        <h1>Something went wrong</h1>
        <p class="muted">An unexpected error occurred. Please try again in a moment.</p>
        <a href="${ctx}/" class="btn btn--primary"><ui:icon name="home" size="16"/> Go to home</a>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
