<%@ tag body-content="empty" pageEncoding="UTF-8" description="Compact report summary used inside match cards" %>
<%@ attribute name="item" required="true" type="com.reclaim.model.Item" %>
<%@ attribute name="caption" required="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<a class="mini-item ${item.lost ? 'is-lost' : 'is-found'}" href="${ctx}/item?id=${item.id}">
    <span class="mini-item__thumb">
        <c:choose>
            <c:when test="${item.hasImage}"><img src="${ctx}/uploads/${item.imagePath}" alt="" loading="lazy"></c:when>
            <c:otherwise><ui:icon name="${item.lost ? 'help' : 'package'}" size="24"/></c:otherwise>
        </c:choose>
    </span>
    <span class="mini-item__text">
        <span class="mini-item__caption"><c:out value="${caption}"/></span>
        <strong class="mini-item__title"><c:out value="${item.title}"/></strong>
        <span class="mini-item__meta">${item.category.label} · ${item.colour.label}</span>
        <span class="mini-item__meta"><ui:icon name="map-pin" size="12"/> ${item.location.label} · ${item.incidentDateFormatted}</span>
    </span>
</a>
