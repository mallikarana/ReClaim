<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="My reports"/>
<c:set var="activeNav" value="my-reports"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <div class="page-head">
        <div>
            <p class="eyebrow">My reports</p>
            <h1>Everything you've reported</h1>
            <p class="muted">Track the status of your reports and jump to their potential matches.</p>
        </div>
        <div class="page-head__actions">
            <a href="${ctx}/report?type=lost" class="btn btn--lost btn--sm"><ui:icon name="plus" size="14"/> Lost</a>
            <a href="${ctx}/report?type=found" class="btn btn--found btn--sm"><ui:icon name="plus" size="14"/> Found</a>
        </div>
    </div>

    <nav class="tabs" aria-label="Filter reports">
        <a href="${ctx}/my-reports" class="${tab == 'ALL' ? 'active' : ''}">All</a>
        <a href="${ctx}/my-reports?type=LOST" class="${tab == 'LOST' ? 'active' : ''}">Lost</a>
        <a href="${ctx}/my-reports?type=FOUND" class="${tab == 'FOUND' ? 'active' : ''}">Found</a>
    </nav>

    <c:choose>
        <c:when test="${empty items}">
            <div class="empty card">
                <ui:icon name="list" size="32"/>
                <h3>No reports here yet</h3>
                <p class="muted">Lost something or found something on campus? Create your first report.</p>
                <div class="empty__actions">
                    <a href="${ctx}/report?type=lost" class="btn btn--lost btn--sm">Report lost</a>
                    <a href="${ctx}/report?type=found" class="btn btn--found btn--sm">Report found</a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="report-list">
                <c:forEach var="it" items="${items}">
                    <c:set var="mc" value="${matchCounts[it.id]}"/>
                    <article class="report-row card ${it.lost ? 'is-lost' : 'is-found'}">
                        <a class="report-row__thumb" href="${ctx}/item?id=${it.id}">
                            <c:choose>
                                <c:when test="${it.hasImage}"><img src="${ctx}/uploads/${it.imagePath}" alt="" loading="lazy"></c:when>
                                <c:otherwise><ui:icon name="${it.lost ? 'help' : 'package'}" size="28"/></c:otherwise>
                            </c:choose>
                        </a>
                        <div class="report-row__main">
                            <div class="report-row__badges">
                                <span class="badge badge--${it.lost ? 'lost' : 'found'}">${it.type.label}</span>
                                <span class="badge badge--status-${it.status}">${it.status.label}</span>
                            </div>
                            <a class="report-row__title" href="${ctx}/item?id=${it.id}"><c:out value="${it.title}"/></a>
                            <p class="report-row__meta">
                                <span><ui:icon name="tag" size="13"/> ${it.category.label}</span>
                                <span><ui:icon name="map-pin" size="13"/> ${it.location.label}</span>
                                <span><ui:icon name="calendar" size="13"/> ${it.incidentDateFormatted}</span>
                                <span class="muted">Reported ${it.createdAgo}</span>
                            </p>
                        </div>
                        <div class="report-row__side">
                            <c:choose>
                                <c:when test="${it.open and mc > 0}">
                                    <a href="${ctx}/item?id=${it.id}" class="match-chip"><ui:icon name="zap" size="14"/> ${mc} ${mc == 1 ? 'match' : 'matches'}</a>
                                </c:when>
                                <c:when test="${it.open}"><span class="match-chip match-chip--none">No matches yet</span></c:when>
                            </c:choose>
                            <div class="report-row__actions">
                                <c:if test="${it.open}">
                                    <form action="${ctx}/item/status" method="post" class="inline-form">
                                        <ui:csrf/><input type="hidden" name="id" value="${it.id}"><input type="hidden" name="status" value="RESOLVED"><input type="hidden" name="back" value="my-reports">
                                        <button class="btn btn--found btn--xs" type="submit" title="Mark as resolved"><ui:icon name="check" size="13"/> Resolved</button>
                                    </form>
                                    <form action="${ctx}/item/status" method="post" class="inline-form" data-confirm="Close this report? It will no longer be matched.">
                                        <ui:csrf/><input type="hidden" name="id" value="${it.id}"><input type="hidden" name="status" value="CLOSED"><input type="hidden" name="back" value="my-reports">
                                        <button class="btn btn--ghost btn--xs" type="submit" title="Close report"><ui:icon name="x-circle" size="13"/> Close</button>
                                    </form>
                                </c:if>
                                <c:if test="${not it.open}">
                                    <form action="${ctx}/item/status" method="post" class="inline-form">
                                        <ui:csrf/><input type="hidden" name="id" value="${it.id}"><input type="hidden" name="status" value="OPEN"><input type="hidden" name="back" value="my-reports">
                                        <button class="btn btn--secondary btn--xs" type="submit"><ui:icon name="refresh" size="13"/> Re-open</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
