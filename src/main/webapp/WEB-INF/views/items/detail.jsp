<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="${item.title}"/>
<c:set var="activeNav" value="${owner ? 'my-reports' : 'browse'}"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <a href="${ctx}${owner ? '/my-reports' : '/items'}" class="back-link"><ui:icon name="arrow-left" size="16"/> ${owner ? 'My reports' : 'Browse'}</a>

    <article class="detail card ${item.lost ? 'is-lost' : 'is-found'}">
        <div class="detail__media">
            <c:choose>
                <c:when test="${item.hasImage}">
                    <img src="${ctx}/uploads/${item.imagePath}" alt="Photo of ${fn:escapeXml(item.title)}">
                </c:when>
                <c:otherwise>
                    <div class="detail__placeholder">
                        <ui:icon name="${item.lost ? 'help' : 'package'}" size="64"/>
                        <span>No photo provided</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="detail__body">
            <div class="detail__badges">
                <span class="badge badge--${item.lost ? 'lost' : 'found'}">${item.type.label}</span>
                <span class="badge badge--status-${item.status}">${item.status.label}</span>
                <span class="muted small">Report #${item.id}</span>
            </div>
            <h1 class="detail__title"><c:out value="${item.title}"/></h1>
            <p class="detail__desc"><c:out value="${item.description}"/></p>

            <dl class="facts">
                <div><dt><ui:icon name="tag" size="16"/> Category</dt><dd>${item.category.label}</dd></div>
                <div><dt><ui:icon name="droplet" size="16"/> Colour</dt><dd><span class="swatch" style="background:${item.colour.swatch}"></span> ${item.colour.label}</dd></div>
                <div><dt><ui:icon name="map-pin" size="16"/> Location</dt>
                    <dd>${item.location.label}<c:if test="${not empty item.locationDetails}"><br><span class="muted small"><c:out value="${item.locationDetails}"/></span></c:if></dd></div>
                <div><dt><ui:icon name="calendar" size="16"/> ${item.lost ? 'Date lost' : 'Date found'}</dt><dd>${item.incidentDateFormatted}</dd></div>
                <div><dt><ui:icon name="user" size="16"/> Reported by</dt><dd><c:out value="${owner ? 'You' : item.reporterName}"/></dd></div>
                <div><dt><ui:icon name="clock" size="16"/> Reported</dt><dd>${item.createdAtFormatted}</dd></div>
            </dl>

            <c:choose>
                <c:when test="${owner}">
                    <div class="owner-actions">
                        <span class="muted small">Update status:</span>
                        <c:if test="${item.status != 'RESOLVED'}">
                            <form action="${ctx}/item/status" method="post" class="inline-form">
                                <ui:csrf/><input type="hidden" name="id" value="${item.id}"><input type="hidden" name="status" value="RESOLVED">
                                <button class="btn btn--found btn--sm" type="submit"><ui:icon name="check" size="14"/> ${item.lost ? 'I got it back' : 'Returned to owner'}</button>
                            </form>
                        </c:if>
                        <c:if test="${item.status != 'CLOSED'}">
                            <form action="${ctx}/item/status" method="post" class="inline-form" data-confirm="Close this report? It will no longer be matched.">
                                <ui:csrf/><input type="hidden" name="id" value="${item.id}"><input type="hidden" name="status" value="CLOSED">
                                <button class="btn btn--ghost btn--sm" type="submit"><ui:icon name="x-circle" size="14"/> Close report</button>
                            </form>
                        </c:if>
                        <c:if test="${item.status != 'OPEN'}">
                            <form action="${ctx}/item/status" method="post" class="inline-form">
                                <ui:csrf/><input type="hidden" name="id" value="${item.id}"><input type="hidden" name="status" value="OPEN">
                                <button class="btn btn--secondary btn--sm" type="submit"><ui:icon name="refresh" size="14"/> Re-open</button>
                            </form>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="notice">
                        <ui:icon name="info" size="18"/>
                        <div>
                            <b>${item.lost ? 'Have you found this item?' : 'Is this yours?'}</b>
                            <p class="small">Online claims with ownership verification are coming in the next module.
                                For now, contact the campus Lost &amp; Found desk and quote <b>Report #${item.id}</b>.</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </article>

    <c:if test="${owner}">
        <section class="section-block">
            <div class="section-head">
                <h2><ui:icon name="zap"/> Potential matches for this report</h2>
            </div>
            <c:choose>
                <c:when test="${not item.open}">
                    <div class="empty card">
                        <ui:icon name="check" size="32"/>
                        <h3>This report is ${fn:toLowerCase(item.status.label)}</h3>
                        <p class="muted">Matching runs only for open reports. Re-open it to look for matches again.</p>
                    </div>
                </c:when>
                <c:when test="${empty matches}">
                    <div class="empty card">
                        <ui:icon name="link" size="32"/>
                        <h3>No potential matches yet</h3>
                        <p class="muted">We compared your report with every open ${item.lost ? 'found' : 'lost'} report and none scored 40 or more.
                            New reports are checked automatically, so come back later.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="muted small">${fn:length(matches)} open ${item.lost ? 'found' : 'lost'} ${fn:length(matches) == 1 ? 'report looks' : 'reports look'} similar to yours, best first.</p>
                    <div class="match-list">
                        <c:forEach var="m" items="${matches}">
                            <ui:matchCard match="${m}" viewerId="${authUser.id}"/>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </c:if>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
