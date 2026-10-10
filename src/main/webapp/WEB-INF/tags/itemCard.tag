<%@ tag body-content="empty" pageEncoding="UTF-8" description="Card summarising one lost/found report" %>
<%@ attribute name="item" required="true" type="com.reclaim.model.Item" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<a class="item-card ${item.lost ? 'is-lost' : 'is-found'}" href="${ctx}/item?id=${item.id}">
    <div class="item-card__media">
        <c:choose>
            <c:when test="${item.hasImage}">
                <img src="${ctx}/uploads/${item.imagePath}" alt="Photo of ${fn:escapeXml(item.title)}" loading="lazy">
            </c:when>
            <c:otherwise>
                <div class="item-card__placeholder"><ui:icon name="${item.lost ? 'help' : 'package'}" size="40"/></div>
            </c:otherwise>
        </c:choose>
        <span class="badge badge--${item.lost ? 'lost' : 'found'} item-card__type">${item.type.label}</span>
        <c:if test="${not item.open}">
            <span class="badge badge--status-${item.status} item-card__status">${item.status.label}</span>
        </c:if>
    </div>
    <div class="item-card__body">
        <h3 class="item-card__title"><c:out value="${item.title}"/></h3>
        <p class="item-card__meta"><ui:icon name="tag" size="14"/> ${item.category.label}</p>
        <p class="item-card__meta"><ui:icon name="map-pin" size="14"/> ${item.location.label}</p>
        <div class="item-card__footer">
            <span class="swatch" style="background:${item.colour.swatch}" title="${item.colour.label}"></span>
            <span class="muted">${item.colour.label}</span>
            <span class="item-card__date"><ui:icon name="calendar" size="14"/> ${item.incidentDateFormatted}</span>
        </div>
    </div>
</a>
