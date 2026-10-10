<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="isLost" value="${type == 'LOST'}"/>
<c:set var="pageTitle" value="${isLost ? 'Report a lost item' : 'Report a found item'}"/>
<c:set var="activeNav" value="${isLost ? 'report-lost' : 'report-found'}"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="container container--narrow">
    <div class="page-head">
        <div>
            <p class="eyebrow">New report</p>
            <h1 data-type-title data-lost="Report a lost item" data-found="Report a found item">${pageTitle}</h1>
            <p class="muted" data-type-hint
               data-lost="Tell us what you lost. The more detail you give, the better the matching works."
               data-found="Thank you for helping! Describe what you found so the owner can recognise it.">
                ${isLost ? 'Tell us what you lost. The more detail you give, the better the matching works.'
                         : 'Thank you for helping! Describe what you found so the owner can recognise it.'}
            </p>
        </div>
    </div>

    <c:if test="${not empty errors}">
        <div class="alert alert--error" role="alert"><ui:icon name="alert"/><span>Please correct the highlighted fields below.</span></div>
    </c:if>

    <form action="${ctx}/report" method="post" enctype="multipart/form-data" class="form report-form ${isLost ? 'theme-lost' : 'theme-found'}"
          novalidate data-validate data-report-form>
        <ui:csrf/>

        <section class="card form-section">
            <div class="type-switch" role="radiogroup" aria-label="Report type">
                <label class="type-switch__option type-switch__option--lost">
                    <input type="radio" name="type" value="LOST" ${isLost ? 'checked' : ''}>
                    <span><ui:icon name="help" size="20"/> <b>I lost something</b><small>Looking for my item</small></span>
                </label>
                <label class="type-switch__option type-switch__option--found">
                    <input type="radio" name="type" value="FOUND" ${isLost ? '' : 'checked'}>
                    <span><ui:icon name="package" size="20"/> <b>I found something</b><small>Want to return it</small></span>
                </label>
            </div>
        </section>

        <section class="card form-section">
            <h2 class="form-section__title"><span class="form-section__num">1</span> What is it?</h2>
            <div class="form-grid">
                <div class="field field--full ${not empty errors.title ? 'has-error' : ''}">
                    <label for="title">Item name <span class="req">*</span></label>
                    <input type="text" id="title" name="title" value="${fn:escapeXml(form.title)}" required minlength="3" maxlength="100"
                           placeholder="e.g. Black Samsung Galaxy phone">
                    <c:if test="${not empty errors.title}"><p class="field__error"><c:out value="${errors.title}"/></p></c:if>
                </div>
                <div class="field field--full ${not empty errors.category ? 'has-error' : ''}">
                    <label for="category">Category <span class="req">*</span></label>
                    <select id="category" name="category" required>
                        <option value="">Select a category</option>
                        <c:forEach var="cat" items="${applicationScope.categories}">
                            <option value="${cat.name}" ${form.category == cat.name ? 'selected' : ''}>${cat.label}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errors.category}"><p class="field__error"><c:out value="${errors.category}"/></p></c:if>
                </div>
                <div class="field field--full ${not empty errors.colour ? 'has-error' : ''}">
                    <span class="label">Main colour <span class="req">*</span></span>
                    <div class="swatches" role="radiogroup" aria-label="Main colour">
                        <c:forEach var="col" items="${applicationScope.colours}">
                            <label class="swatch-option" title="${col.label}">
                                <input type="radio" name="colour" value="${col.name}" ${form.colour == col.name ? 'checked' : ''} required>
                                <span class="swatch-option__chip"><span class="swatch" style="background:${col.swatch}"></span>${col.label}</span>
                            </label>
                        </c:forEach>
                    </div>
                    <c:if test="${not empty errors.colour}"><p class="field__error"><c:out value="${errors.colour}"/></p></c:if>
                </div>
            </div>
        </section>

        <section class="card form-section">
            <h2 class="form-section__title"><span class="form-section__num">2</span> Where and when?</h2>
            <div class="form-grid">
                <div class="field ${not empty errors.location ? 'has-error' : ''}">
                    <label for="location">Campus location <span class="req">*</span></label>
                    <select id="location" name="location" required>
                        <option value="">Select a location</option>
                        <c:forEach var="loc" items="${applicationScope.locations}">
                            <option value="${loc.name}" ${form.location == loc.name ? 'selected' : ''}>${loc.label}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errors.location}"><p class="field__error"><c:out value="${errors.location}"/></p></c:if>
                </div>
                <div class="field ${not empty errors.incidentDate ? 'has-error' : ''}">
                    <label for="incidentDate"><span data-type-text data-lost="Date lost" data-found="Date found">${isLost ? 'Date lost' : 'Date found'}</span> <span class="req">*</span></label>
                    <input type="date" id="incidentDate" name="incidentDate" required min="${minDate}" max="${today}"
                           value="${empty form.incidentDate ? today : fn:escapeXml(form.incidentDate)}">
                    <c:if test="${not empty errors.incidentDate}"><p class="field__error"><c:out value="${errors.incidentDate}"/></p></c:if>
                </div>
                <div class="field field--full ${not empty errors.locationDetails ? 'has-error' : ''}">
                    <label for="locationDetails">Location details <span class="optional">(optional)</span></label>
                    <input type="text" id="locationDetails" name="locationDetails" value="${fn:escapeXml(form.locationDetails)}" maxlength="150"
                           placeholder="e.g. 2nd floor reading room, near the window">
                    <c:if test="${not empty errors.locationDetails}"><p class="field__error"><c:out value="${errors.locationDetails}"/></p></c:if>
                </div>
            </div>
        </section>

        <section class="card form-section">
            <h2 class="form-section__title"><span class="form-section__num">3</span> Describe it</h2>
            <div class="field ${not empty errors.description ? 'has-error' : ''}">
                <label for="description">Description <span class="req">*</span></label>
                <textarea id="description" name="description" rows="5" required minlength="10" maxlength="1000" data-counter
                          placeholder="Brand, model, stickers, scratches, contents... anything that helps identify it."><c:out value="${form.description}"/></textarea>
                <div class="field__row">
                    <c:choose>
                        <c:when test="${not empty errors.description}"><p class="field__error"><c:out value="${errors.description}"/></p></c:when>
                        <c:otherwise><p class="field__hint" data-type-text data-lost="Tip: mention details only the owner would know, too." data-found="Tip: keep one identifying detail back so you can verify the real owner.">${isLost ? 'Tip: mention details only the owner would know, too.' : 'Tip: keep one identifying detail back so you can verify the real owner.'}</p></c:otherwise>
                    </c:choose>
                    <span class="counter" data-counter-for="description">0 / 1000</span>
                </div>
            </div>

            <div class="field ${not empty errors.image ? 'has-error' : ''}">
                <span class="label">Photo <span class="optional">(optional, max 5 MB)</span></span>
                <label class="dropzone" data-dropzone>
                    <input type="file" name="image" accept="image/jpeg,image/png,image/gif,image/webp" data-image-input data-max-bytes="5242880">
                    <span class="dropzone__empty">
                        <ui:icon name="upload" size="28"/>
                        <b>Click to choose a photo</b>
                        <small>JPG, PNG, GIF or WEBP</small>
                    </span>
                    <img class="dropzone__preview" alt="Selected photo preview" hidden>
                </label>
                <c:if test="${not empty errors.image}"><p class="field__error"><c:out value="${errors.image}"/></p></c:if>
                <c:if test="${not empty errors and empty errors.image}"><p class="field__hint">If you had chosen a photo, please select it again.</p></c:if>
                <p class="field__error" data-image-error hidden></p>
            </div>
        </section>

        <div class="form-actions form-actions--sticky">
            <a href="${ctx}/dashboard" class="btn btn--ghost">Cancel</a>
            <button type="submit" class="btn btn--primary btn--lg" data-submit-label data-lost="Submit lost report" data-found="Submit found report">
                ${isLost ? 'Submit lost report' : 'Submit found report'}
            </button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
