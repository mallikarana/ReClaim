<%@ tag body-content="empty" pageEncoding="UTF-8" description="Hidden CSRF token field - include inside every POST form" %>
<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
