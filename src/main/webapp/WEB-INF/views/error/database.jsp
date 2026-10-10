<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Service unavailable"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%-- Shown when a DataAccessException reaches the container (e.g. MySQL is not running). --%>
<div class="container">
    <div class="error-page card">
        <span class="error-page__code"><ui:icon name="layers" size="48"/></span>
        <h1>We can't reach the database right now</h1>
        <p class="muted">Your data is safe. Please try again in a minute. If the problem continues, contact the administrator.</p>
        <a href="" class="btn btn--primary"><ui:icon name="refresh" size="16"/> Try again</a>
    </div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
