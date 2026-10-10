<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="My profile"/>
<c:set var="activeNav" value="profile"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<%-- After a failed update, show what the user typed; otherwise the saved values. --%>
<c:set var="f_fullName" value="${not empty form ? form.fullName : profile.fullName}"/>
<c:set var="f_userType" value="${not empty form ? form.userType : profile.userType.name}"/>
<c:set var="f_universityId" value="${not empty form ? form.universityId : profile.universityId}"/>
<c:set var="f_department" value="${not empty form ? form.department : profile.department}"/>
<c:set var="f_phone" value="${not empty form ? form.phone : profile.phone}"/>

<div class="container">
    <div class="page-head">
        <div>
            <p class="eyebrow">Account</p>
            <h1>My profile</h1>
        </div>
    </div>

    <div class="profile-layout">
        <aside class="card profile-card">
            <span class="avatar avatar--lg"><c:out value="${profile.initials}"/></span>
            <h2><c:out value="${profile.fullName}"/></h2>
            <p class="muted"><c:out value="${profile.email}"/></p>
            <span class="badge badge--neutral">${profile.userType.label}</span>
            <div class="profile-card__stats">
                <div><b>${reportCount}</b><span>Reports</span></div>
                <div><b>${lostCount}</b><span>Lost</span></div>
                <div><b>${foundCount}</b><span>Found</span></div>
            </div>
            <dl class="details">
                <div><dt><ui:icon name="card" size="14"/> University ID</dt><dd><c:out value="${empty profile.universityId ? '—' : profile.universityId}"/></dd></div>
                <div><dt><ui:icon name="briefcase" size="14"/> Department</dt><dd><c:out value="${empty profile.department ? '—' : profile.department}"/></dd></div>
                <div><dt><ui:icon name="phone" size="14"/> Mobile</dt><dd><c:out value="${empty profile.phone ? '—' : profile.phone}"/></dd></div>
                <div><dt><ui:icon name="calendar" size="14"/> Member since</dt><dd>${profile.createdAtFormatted}</dd></div>
                <div><dt><ui:icon name="clock" size="14"/> Last login</dt><dd>${profile.lastLoginAtFormatted}</dd></div>
            </dl>
        </aside>

        <div class="profile-forms">
            <section class="card">
                <h2 class="card__title"><ui:icon name="edit"/> Profile details</h2>
                <c:if test="${editOpen and not empty errors}">
                    <div class="alert alert--error" role="alert"><ui:icon name="alert"/><span>Please correct the highlighted fields.</span></div>
                </c:if>
                <form action="${ctx}/profile" method="post" class="form" novalidate data-validate>
                    <ui:csrf/>
                    <input type="hidden" name="action" value="profile">
                    <div class="form-grid">
                        <div class="field field--full ${editOpen and not empty errors.fullName ? 'has-error' : ''}">
                            <label for="fullName">Full name <span class="req">*</span></label>
                            <input type="text" id="fullName" name="fullName" value="${fn:escapeXml(f_fullName)}" required minlength="2" maxlength="100">
                            <c:if test="${editOpen and not empty errors.fullName}"><p class="field__error"><c:out value="${errors.fullName}"/></p></c:if>
                        </div>
                        <div class="field field--full">
                            <label for="emailRo">Email address</label>
                            <input type="email" id="emailRo" value="${fn:escapeXml(profile.email)}" disabled>
                            <p class="field__hint">Your email is your login and cannot be changed.</p>
                        </div>
                        <div class="field field--full ${editOpen and not empty errors.userType ? 'has-error' : ''}">
                            <span class="label">I am a</span>
                            <div class="segmented">
                                <c:forEach var="t" items="${applicationScope.userTypes}">
                                    <label class="segmented__option">
                                        <input type="radio" name="userType" value="${t.name}" ${f_userType == t.name ? 'checked' : ''}>
                                        <span>${t.label}</span>
                                    </label>
                                </c:forEach>
                            </div>
                            <c:if test="${editOpen and not empty errors.userType}"><p class="field__error"><c:out value="${errors.userType}"/></p></c:if>
                        </div>
                        <div class="field ${editOpen and not empty errors.universityId ? 'has-error' : ''}">
                            <label for="universityId">Student / Employee ID</label>
                            <input type="text" id="universityId" name="universityId" value="${fn:escapeXml(f_universityId)}" maxlength="20">
                            <c:if test="${editOpen and not empty errors.universityId}"><p class="field__error"><c:out value="${errors.universityId}"/></p></c:if>
                        </div>
                        <div class="field ${editOpen and not empty errors.phone ? 'has-error' : ''}">
                            <label for="phone">Mobile number</label>
                            <input type="tel" id="phone" name="phone" value="${fn:escapeXml(f_phone)}" maxlength="10" pattern="[6-9][0-9]{9}" inputmode="numeric">
                            <c:if test="${editOpen and not empty errors.phone}"><p class="field__error"><c:out value="${errors.phone}"/></p></c:if>
                        </div>
                        <div class="field field--full ${editOpen and not empty errors.department ? 'has-error' : ''}">
                            <label for="department">Department / Course</label>
                            <input type="text" id="department" name="department" value="${fn:escapeXml(f_department)}" maxlength="100">
                            <c:if test="${editOpen and not empty errors.department}"><p class="field__error"><c:out value="${errors.department}"/></p></c:if>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn--primary">Save changes</button>
                    </div>
                </form>
            </section>

            <section class="card">
                <h2 class="card__title"><ui:icon name="lock"/> Change password</h2>
                <form action="${ctx}/profile" method="post" class="form" novalidate data-validate>
                    <ui:csrf/>
                    <input type="hidden" name="action" value="password">
                    <div class="form-grid">
                        <div class="field field--full ${passwordOpen and not empty errors.currentPassword ? 'has-error' : ''}">
                            <label for="currentPassword">Current password</label>
                            <input type="password" id="currentPassword" name="currentPassword" required autocomplete="current-password">
                            <c:if test="${passwordOpen and not empty errors.currentPassword}"><p class="field__error"><c:out value="${errors.currentPassword}"/></p></c:if>
                        </div>
                        <div class="field ${passwordOpen and not empty errors.newPassword ? 'has-error' : ''}">
                            <label for="newPassword">New password</label>
                            <input type="password" id="newPassword" name="newPassword" required minlength="8" maxlength="64" autocomplete="new-password" data-strength>
                            <div class="strength" data-strength-meter><span></span></div>
                            <c:if test="${passwordOpen and not empty errors.newPassword}"><p class="field__error"><c:out value="${errors.newPassword}"/></p></c:if>
                        </div>
                        <div class="field ${passwordOpen and not empty errors.confirmNewPassword ? 'has-error' : ''}">
                            <label for="confirmNewPassword">Confirm new password</label>
                            <input type="password" id="confirmNewPassword" name="confirmNewPassword" required autocomplete="new-password" data-match="newPassword">
                            <c:if test="${passwordOpen and not empty errors.confirmNewPassword}"><p class="field__error"><c:out value="${errors.confirmNewPassword}"/></p></c:if>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn--secondary">Update password</button>
                    </div>
                </form>
            </section>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
