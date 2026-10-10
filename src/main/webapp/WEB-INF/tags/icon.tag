<%@ tag body-content="empty" pageEncoding="UTF-8" description="Inline SVG icon (Feather icon set, MIT licence)" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="size" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="px" value="${empty size ? 18 : size}"/>
<svg class="icon" width="${px}" height="${px}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true" focusable="false"><c:choose>
<c:when test="${name == 'search'}"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></c:when>
<c:when test="${name == 'plus'}"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></c:when>
<c:when test="${name == 'home'}"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></c:when>
<c:when test="${name == 'user'}"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></c:when>
<c:when test="${name == 'logout'}"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></c:when>
<c:when test="${name == 'map-pin'}"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></c:when>
<c:when test="${name == 'calendar'}"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></c:when>
<c:when test="${name == 'tag'}"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></c:when>
<c:when test="${name == 'list'}"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></c:when>
<c:when test="${name == 'link'}"><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/><path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/></c:when>
<c:when test="${name == 'alert'}"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></c:when>
<c:when test="${name == 'check'}"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></c:when>
<c:when test="${name == 'info'}"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></c:when>
<c:when test="${name == 'x'}"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></c:when>
<c:when test="${name == 'x-circle'}"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></c:when>
<c:when test="${name == 'menu'}"><line x1="3" y1="12" x2="21" y2="12"/><line x1="3" y1="6" x2="21" y2="6"/><line x1="3" y1="18" x2="21" y2="18"/></c:when>
<c:when test="${name == 'image'}"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></c:when>
<c:when test="${name == 'package'}"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></c:when>
<c:when test="${name == 'help'}"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></c:when>
<c:when test="${name == 'arrow-right'}"><line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/></c:when>
<c:when test="${name == 'arrow-left'}"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></c:when>
<c:when test="${name == 'upload'}"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></c:when>
<c:when test="${name == 'shield'}"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></c:when>
<c:when test="${name == 'lock'}"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></c:when>
<c:when test="${name == 'mail'}"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></c:when>
<c:when test="${name == 'edit'}"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></c:when>
<c:when test="${name == 'clock'}"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></c:when>
<c:when test="${name == 'droplet'}"><path d="M12 2.69l5.66 5.66a8 8 0 1 1-11.31 0z"/></c:when>
<c:when test="${name == 'zap'}"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></c:when>
<c:when test="${name == 'filter'}"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></c:when>
<c:when test="${name == 'briefcase'}"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"/><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/></c:when>
<c:when test="${name == 'card'}"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></c:when>
<c:when test="${name == 'phone'}"><rect x="5" y="2" width="14" height="20" rx="2" ry="2"/><line x1="12" y1="18" x2="12.01" y2="18"/></c:when>
<c:when test="${name == 'refresh'}"><polyline points="1 4 1 10 7 10"/><path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10"/></c:when>
<c:when test="${name == 'eye'}"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></c:when>
<c:when test="${name == 'grid'}"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></c:when>
<c:when test="${name == 'layers'}"><polygon points="12 2 2 7 12 12 22 7 12 2"/><polyline points="2 17 12 22 22 17"/><polyline points="2 12 12 17 22 12"/></c:when>
<c:otherwise><circle cx="12" cy="12" r="10"/></c:otherwise>
</c:choose></svg>
