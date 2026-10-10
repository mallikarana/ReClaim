<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Potential matches"/>
<c:set var="activeNav" value="matches"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <div class="page-head">
        <div>
            <p class="eyebrow">Matching</p>
            <h1>Potential matches</h1>
            <p class="muted">Each of your open reports is compared with every open report of the opposite type.
                Pairs scoring ${minScore} or more out of 100 are shown here, best first.</p>
        </div>
    </div>

    <details class="card explain">
        <summary><ui:icon name="info" size="16"/> How is the match score calculated?</summary>
        <div class="explain__body">
            <table class="table">
                <thead><tr><th>Attribute</th><th>Max points</th><th>Rule</th></tr></thead>
                <tbody>
                <tr><td>Category</td><td>25</td><td>Same category = 25</td></tr>
                <tr><td>Location</td><td>25</td><td>Same campus zone = 25; different zone but location details share a word = 10</td></tr>
                <tr><td>Colour</td><td>20</td><td>Same colour = 20; similar shade (e.g. grey/silver) = 10; multicolour or unsure = 5</td></tr>
                <tr><td>Date</td><td>15</td><td>Found within 1 day = 15, 3 days = 12, a week = 9, two weeks = 6, a month = 3</td></tr>
                <tr><td>Description</td><td>15</td><td>Share of keywords (from name + description) the two reports have in common</td></tr>
                </tbody>
            </table>
            <p class="muted small">Confidence: <b>High</b> &ge; 75 · <b>Medium</b> 55-74 · <b>Low</b> ${minScore}-54.</p>
        </div>
    </details>

    <c:choose>
        <c:when test="${empty matches}">
            <div class="empty card">
                <ui:icon name="link" size="32"/>
                <h3>No potential matches right now</h3>
                <p class="muted">Matches appear when one of your <b>open</b> reports is similar to someone else's.
                    The more detail your reports include, the better the matching works.</p>
                <div class="empty__actions">
                    <a href="${ctx}/my-reports" class="btn btn--secondary btn--sm">View my reports</a>
                    <a href="${ctx}/report?type=lost" class="btn btn--primary btn--sm">Report an item</a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="match-list">
                <c:forEach var="m" items="${matches}">
                    <ui:matchCard match="${m}" viewerId="${authUser.id}"/>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
