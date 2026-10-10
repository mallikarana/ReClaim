<%@ tag body-content="empty" pageEncoding="UTF-8" description="One potential match: both reports, the total score and its breakdown" %>
<%@ attribute name="match" required="true" type="com.reclaim.model.Match" %>
<%@ attribute name="viewerId" required="true" type="java.lang.Integer" %>
<%@ attribute name="compact" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<c:set var="iOwnLost" value="${match.lostItem.userId == viewerId}"/>
<c:set var="mine" value="${iOwnLost ? match.lostItem : match.foundItem}"/>
<c:set var="theirs" value="${iOwnLost ? match.foundItem : match.lostItem}"/>
<article class="match-card match-card--${fn:toLowerCase(match.confidence)}">
    <div class="match-card__pair">
        <ui:miniItem item="${mine}" caption="Your ${fn:toLowerCase(mine.type.label)} report"/>
        <div class="score-ring" style="--p:${match.score}" title="Match score ${match.score} out of 100">
            <div class="score-ring__inner">
                <strong>${match.score}%</strong>
                <small>${match.confidence}</small>
            </div>
        </div>
        <ui:miniItem item="${theirs}" caption="${theirs.type.label} by ${theirs.reporterName}"/>
    </div>
    <c:if test="${not compact}">
        <div class="breakdown" aria-label="Score breakdown">
            <div class="breakdown__row">
                <span class="breakdown__label"><ui:icon name="tag" size="14"/> Category</span>
                <span class="breakdown__bar"><span style="width:${match.categoryScore * 4}%"></span></span>
                <span class="breakdown__value">${match.categoryScore}/25</span>
            </div>
            <div class="breakdown__row">
                <span class="breakdown__label"><ui:icon name="droplet" size="14"/> Colour</span>
                <span class="breakdown__bar"><span style="width:${match.colourScore * 5}%"></span></span>
                <span class="breakdown__value">${match.colourScore}/20</span>
            </div>
            <div class="breakdown__row">
                <span class="breakdown__label"><ui:icon name="map-pin" size="14"/> Location</span>
                <span class="breakdown__bar"><span style="width:${match.locationScore * 4}%"></span></span>
                <span class="breakdown__value">${match.locationScore}/25</span>
            </div>
            <div class="breakdown__row">
                <span class="breakdown__label"><ui:icon name="calendar" size="14"/> Date</span>
                <span class="breakdown__bar"><span style="width:${fn:substringBefore(match.dateScore * 100 / 15, '.')}%"></span></span>
                <span class="breakdown__value">${match.dateScore}/15</span>
            </div>
            <div class="breakdown__row">
                <span class="breakdown__label"><ui:icon name="list" size="14"/> Description</span>
                <span class="breakdown__bar"><span style="width:${fn:substringBefore(match.descriptionScore * 100 / 15, '.')}%"></span></span>
                <span class="breakdown__value">${match.descriptionScore}/15</span>
            </div>
        </div>
    </c:if>
</article>
