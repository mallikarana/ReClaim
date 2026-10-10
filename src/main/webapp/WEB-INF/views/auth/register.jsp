<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Create account"/>
<c:set var="activeNav" value="register"/>
<c:set var="bodyClass" value="auth-page"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container">
    <div class="auth auth--wide">
        <aside class="auth__aside">
            <h2>Join the campus Lost &amp; Found</h2>
            <p>One account lets you report lost or found belongings and see likely matches on your dashboard.</p>
            <ul class="auth__points">
                <li><ui:icon name="help"/> Report something you lost in under a minute</li>
                <li><ui:icon name="package"/> Help a fellow student by reporting what you found</li>
                <li><ui:icon name="lock"/> Passwords are stored as salted PBKDF2 hashes</li>
            </ul>
        </aside>

        <section class="auth__card card">
            <h1 class="auth__title">Create your account</h1>
            <p class="muted">Fields marked <span class="req">*</span> are required.</p>

            <c:if test="${not empty errors}">
                <div class="alert alert--error" role="alert"><ui:icon name="alert"/><span>Please correct the highlighted fields.</span></div>
            </c:if>

            <form action="${ctx}/register" method="post" class="form" novalidate data-validate>
                <ui:csrf/>
                <div class="form-grid">
                    <div class="field field--full ${not empty errors.fullName ? 'has-error' : ''}">
                        <label for="fullName">Full name <span class="req">*</span></label>
                        <input type="text" id="fullName" name="fullName" value="${fn:escapeXml(form.fullName)}"
                               required minlength="2" maxlength="100" autocomplete="name" placeholder="e.g. Kartik Maithani" autofocus>
                        <c:if test="${not empty errors.fullName}"><p class="field__error"><c:out value="${errors.fullName}"/></p></c:if>
                    </div>

                    <div class="field field--full ${not empty errors.email ? 'has-error' : ''}">
                        <label for="email">Email address <span class="req">*</span></label>
                        <input type="email" id="email" name="email" value="${fn:escapeXml(form.email)}"
                               required maxlength="150" autocomplete="email" placeholder="you@example.com">
                        <c:if test="${not empty errors.email}"><p class="field__error"><c:out value="${errors.email}"/></p></c:if>
                    </div>

                    <div class="field ${not empty errors.password ? 'has-error' : ''}">
                        <label for="password">Password <span class="req">*</span></label>
                        <div class="input-icon input-icon--right">
                            <input type="password" id="password" name="password" required minlength="8" maxlength="64"
                                   autocomplete="new-password" placeholder="At least 8 characters" data-strength>
                            <button type="button" class="input-icon__action" data-toggle-password="password" aria-label="Show password"><ui:icon name="eye" size="16"/></button>
                        </div>
                        <div class="strength" data-strength-meter><span></span></div>
                        <c:choose>
                            <c:when test="${not empty errors.password}"><p class="field__error"><c:out value="${errors.password}"/></p></c:when>
                            <c:otherwise><p class="field__hint">Use letters and at least one number.</p></c:otherwise>
                        </c:choose>
                    </div>

                    <div class="field ${not empty errors.confirmPassword ? 'has-error' : ''}">
                        <label for="confirmPassword">Confirm password <span class="req">*</span></label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required
                               autocomplete="new-password" placeholder="Repeat the password" data-match="password">
                        <c:if test="${not empty errors.confirmPassword}"><p class="field__error"><c:out value="${errors.confirmPassword}"/></p></c:if>
                    </div>

                    <div class="field field--full ${not empty errors.userType ? 'has-error' : ''}">
                        <span class="label">I am a <span class="req">*</span></span>
                        <div class="segmented">
                            <c:forEach var="t" items="${applicationScope.userTypes}">
                                <label class="segmented__option">
                                    <input type="radio" name="userType" value="${t.name}"
                                        ${(form.userType == t.name) or (empty form.userType and t.name == 'STUDENT') ? 'checked' : ''}>
                                    <span>${t.label}</span>
                                </label>
                            </c:forEach>
                        </div>
                        <c:if test="${not empty errors.userType}"><p class="field__error"><c:out value="${errors.userType}"/></p></c:if>
                    </div>

                    <div class="field ${not empty errors.universityId ? 'has-error' : ''}">
                        <label for="universityId">Student / Employee ID</label>
                        <input type="text" id="universityId" name="universityId" value="${fn:escapeXml(form.universityId)}"
                               maxlength="20" placeholder="e.g. 2520230302">
                        <c:if test="${not empty errors.universityId}"><p class="field__error"><c:out value="${errors.universityId}"/></p></c:if>
                    </div>

                    <div class="field ${not empty errors.phone ? 'has-error' : ''}">
                        <label for="phone">Mobile number</label>
                        <input type="tel" id="phone" name="phone" value="${fn:escapeXml(form.phone)}"
                               maxlength="10" pattern="[6-9][0-9]{9}" inputmode="numeric" placeholder="10-digit number">
                        <c:if test="${not empty errors.phone}"><p class="field__error"><c:out value="${errors.phone}"/></p></c:if>
                    </div>

                    <div class="field field--full ${not empty errors.department ? 'has-error' : ''}">
                        <label for="department">Department / Course</label>
                        <input type="text" id="department" name="department" value="${fn:escapeXml(form.department)}"
                               maxlength="100" placeholder="e.g. MCA, School of Computing">
                        <c:if test="${not empty errors.department}"><p class="field__error"><c:out value="${errors.department}"/></p></c:if>
                    </div>
                </div>

                <button type="submit" class="btn btn--primary btn--block btn--lg">Create account <ui:icon name="arrow-right" size="16"/></button>
            </form>
            <p class="auth__switch">Already registered? <a href="${ctx}/login">Log in</a></p>
        </section>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
