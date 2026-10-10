<%@ include file="/WEB-INF/views/common/taglibs.jspf" %>
<c:set var="pageTitle" value="Campus Lost & Found"/>
<c:set var="activeNav" value="home"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<section class="hero">
    <div class="container hero__inner">
        <div class="hero__text">
            <span class="eyebrow">Graphic Era University · Lost &amp; Found</span>
            <h1>Lost something on campus?<br><span class="gradient-text">ReClaim it.</span></h1>
            <p class="lead">Report lost or found belongings in a minute. ReClaim compares every lost report
                with every found report and shows you the most likely matches, with a clear score.</p>
            <div class="hero__actions">
                <a href="${ctx}/register" class="btn btn--primary btn--lg">Get started <ui:icon name="arrow-right" size="16"/></a>
                <a href="${ctx}/login" class="btn btn--secondary btn--lg">I already have an account</a>
            </div>
        </div>
        <%-- Static illustration only: this is NOT live data from the database. --%>
        <figure class="hero__visual">
            <span class="badge badge--neutral hero__example-tag">Example</span>
            <div class="demo-card demo-card--lost" aria-hidden="true">
                <span class="badge badge--lost">Lost</span>
                <strong>Black Samsung phone</strong>
                <small><ui:icon name="map-pin" size="12"/> Library · yesterday</small>
            </div>
            <div class="demo-score" aria-hidden="true">
                <div class="score-ring score-ring--lg" style="--p:92"><div class="score-ring__inner"><strong>92%</strong><small>High</small></div></div>
            </div>
            <div class="demo-card demo-card--found" aria-hidden="true">
                <span class="badge badge--found">Found</span>
                <strong>Samsung phone, black cover</strong>
                <small><ui:icon name="map-pin" size="12"/> Library · today</small>
            </div>
            <figcaption class="hero__caption">Illustration of how a match is displayed. Not a real report.
                Real matches appear on your dashboard after you log in.</figcaption>
        </figure>
    </div>
</section>

<section class="section">
    <div class="container">
        <h2 class="section__title">How ReClaim works</h2>
        <div class="steps">
            <div class="step card">
                <span class="step__num">1</span>
                <h3>Report</h3>
                <p>Describe what you lost or found: category, colour, where and when. Add a photo if you have one.</p>
            </div>
            <div class="step card">
                <span class="step__num">2</span>
                <h3>Match</h3>
                <p>ReClaim scores every lost report against every found report and ranks the likely matches.</p>
            </div>
            <div class="step card">
                <span class="step__num">3</span>
                <h3>Track</h3>
                <p>Review each suggested match and its score breakdown, then mark your report resolved once the item is back.</p>
            </div>
        </div>
    </div>
</section>

<section class="section section--alt">
    <div class="container two-col">
        <div>
            <h2 class="section__title">Transparent matching, not guesswork</h2>
            <p class="lead">Every potential match comes with a score out of 100, built from the five details
                in each report. You can always see <em>why</em> two reports were matched.</p>
        </div>
        <div class="card weights">
            <div class="weights__row"><span><ui:icon name="tag" size="16"/> Category</span><span class="weights__bar"><span style="width:100%"></span></span><b>25</b></div>
            <div class="weights__row"><span><ui:icon name="map-pin" size="16"/> Location</span><span class="weights__bar"><span style="width:100%"></span></span><b>25</b></div>
            <div class="weights__row"><span><ui:icon name="droplet" size="16"/> Colour</span><span class="weights__bar"><span style="width:80%"></span></span><b>20</b></div>
            <div class="weights__row"><span><ui:icon name="calendar" size="16"/> Date</span><span class="weights__bar"><span style="width:60%"></span></span><b>15</b></div>
            <div class="weights__row"><span><ui:icon name="list" size="16"/> Description</span><span class="weights__bar"><span style="width:60%"></span></span><b>15</b></div>
            <p class="muted small">Pairs scoring 40 or more are shown as potential matches.</p>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
