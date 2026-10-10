<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Dashboard"/>
<c:set var="activeNav" value="dashboard"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <section class="welcome card">
        <div>
            <p class="eyebrow">Dashboard</p>
            <h1>Hello, <c:out value="${authUser.firstName}"/> 👋</h1>
            <p class="muted">Here's what's happening with your reports and across campus.</p>
        </div>
        <div class="welcome__actions">
            <a href="${ctx}/report?type=lost" class="btn btn--lost"><ui:icon name="help" size="16"/> I lost something</a>
            <a href="${ctx}/report?type=found" class="btn btn--found"><ui:icon name="package" size="16"/> I found something</a>
        </div>
    </section>

    <section class="stats">
        <a class="stat card" href="${ctx}/my-reports?type=LOST">
            <span class="stat__icon stat__icon--lost"><ui:icon name="help" size="20"/></span>
            <span class="stat__value">${myLostOpen}</span>
            <span class="stat__label">My open lost reports</span>
        </a>
        <a class="stat card" href="${ctx}/my-reports?type=FOUND">
            <span class="stat__icon stat__icon--found"><ui:icon name="package" size="20"/></span>
            <span class="stat__value">${myFoundOpen}</span>
            <span class="stat__label">My open found reports</span>
        </a>
        <a class="stat card" href="${ctx}/matches">
            <span class="stat__icon stat__icon--match"><ui:icon name="zap" size="20"/></span>
            <span class="stat__value">${matchCount}</span>
            <span class="stat__label">Potential matches</span>
        </a>
        <a class="stat card" href="${ctx}/my-reports">
            <span class="stat__icon stat__icon--resolved"><ui:icon name="check" size="20"/></span>
            <span class="stat__value">${myResolved}</span>
            <span class="stat__label">Resolved reports</span>
        </a>
    </section>

    <section class="section-block">
        <div class="section-head">
            <h2><ui:icon name="zap"/> Your best matches</h2>
            <c:if test="${matchCount > 0}"><a href="${ctx}/matches" class="link-arrow">See all ${matchCount} <ui:icon name="arrow-right" size="14"/></a></c:if>
        </div>
        <c:choose>
            <c:when test="${empty topMatches}">
                <div class="empty card">
                    <ui:icon name="link" size="32"/>
                    <h3>No potential matches yet</h3>
                    <p class="muted">When one of your open reports looks similar to someone else's, it will appear here.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="match-list">
                    <c:forEach var="m" items="${topMatches}">
                        <ui:matchCard match="${m}" viewerId="${authUser.id}" compact="${true}"/>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="section-block">
        <div class="section-head">
            <h2><ui:icon name="clock"/> Recently reported on campus</h2>
            <a href="${ctx}/items" class="link-arrow">Browse all <ui:icon name="arrow-right" size="14"/></a>
        </div>
        <p class="muted small campus-line">
            <span class="dot dot--lost"></span> ${campusLost} open lost
            <span class="dot dot--found"></span> ${campusFound} open found
            <span class="dot dot--resolved"></span> ${campusResolved} resolved
        </p>
        <c:choose>
            <c:when test="${empty recentItems}">
                <div class="empty card">
                    <ui:icon name="layers" size="32"/>
                    <h3>No reports yet</h3>
                    <p class="muted">Be the first: report something you lost or found.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="item-grid">
                    <c:forEach var="it" items="${recentItems}">
                        <ui:itemCard item="${it}"/>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
