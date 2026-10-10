<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Browse items"/>
<c:set var="activeNav" value="browse"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <div class="page-head">
        <div>
            <p class="eyebrow">Browse</p>
            <h1>Lost &amp; found on campus</h1>
            <p class="muted">Search every report. Recognise something? Open it to see the details.</p>
        </div>
    </div>

    <form action="${ctx}/items" method="get" class="filters card" role="search">
        <div class="filters__search">
            <ui:icon name="search" size="18"/>
            <input type="search" name="q" value="${fn:escapeXml(filter.keyword)}" placeholder="Search by name, description or place..." maxlength="100" aria-label="Keyword">
        </div>
        <div class="pills" role="radiogroup" aria-label="Type">
            <label class="pill"><input type="radio" name="type" value="" ${empty filter.type ? 'checked' : ''} data-autosubmit><span>All</span></label>
            <label class="pill pill--lost"><input type="radio" name="type" value="LOST" ${filter.type == 'LOST' ? 'checked' : ''} data-autosubmit><span>Lost</span></label>
            <label class="pill pill--found"><input type="radio" name="type" value="FOUND" ${filter.type == 'FOUND' ? 'checked' : ''} data-autosubmit><span>Found</span></label>
        </div>
        <div class="filters__selects">
            <select name="category" aria-label="Category" data-autosubmit>
                <option value="">All categories</option>
                <c:forEach var="cat" items="${applicationScope.categories}">
                    <option value="${cat.name}" ${filter.category == cat ? 'selected' : ''}>${cat.label}</option>
                </c:forEach>
            </select>
            <select name="location" aria-label="Location" data-autosubmit>
                <option value="">All locations</option>
                <c:forEach var="loc" items="${applicationScope.locations}">
                    <option value="${loc.name}" ${filter.location == loc ? 'selected' : ''}>${loc.label}</option>
                </c:forEach>
            </select>
            <select name="status" aria-label="Status" data-autosubmit>
                <option value="OPEN" ${statusParam == 'OPEN' ? 'selected' : ''}>Open only</option>
                <option value="RESOLVED" ${statusParam == 'RESOLVED' ? 'selected' : ''}>Resolved</option>
                <option value="CLOSED" ${statusParam == 'CLOSED' ? 'selected' : ''}>Closed</option>
                <option value="ALL" ${statusParam == 'ALL' ? 'selected' : ''}>Any status</option>
            </select>
        </div>
        <div class="filters__actions">
            <button type="submit" class="btn btn--primary"><ui:icon name="filter" size="16"/> Apply</button>
            <c:if test="${filter.active}"><a href="${ctx}/items" class="btn btn--ghost">Reset</a></c:if>
        </div>
    </form>

    <p class="result-count">
        <b>${fn:length(items)}</b> ${fn:length(items) == 1 ? 'report' : 'reports'} found
        <c:if test="${limitReached}"><span class="muted">(showing the newest ${fn:length(items)} - refine your search)</span></c:if>
    </p>

    <c:choose>
        <c:when test="${empty items}">
            <div class="empty card">
                <ui:icon name="search" size="32"/>
                <h3>No reports match your search</h3>
                <p class="muted">Try fewer filters, or report the item yourself so others can find it.</p>
                <div class="empty__actions">
                    <a href="${ctx}/report?type=lost" class="btn btn--lost btn--sm">Report lost</a>
                    <a href="${ctx}/report?type=found" class="btn btn--found btn--sm">Report found</a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="item-grid">
                <c:forEach var="it" items="${items}">
                    <ui:itemCard item="${it}"/>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
