# ReClaim — project guide

## Purpose
ReClaim is the campus Lost & Found web application for **Graphic Era University**, built as an
MCA major project (Team Vision: Kartik Maithani, Mallika Rana, Dev Parashar).
The source of truth for the scope is the proposal in `docs/ReClaim_New.pdf`.
Students and staff register, report lost or found items, browse reports and get
automatically scored matches between LOST and FOUND reports.

The project is also an academic deliverable: the team must be able to explain every layer to
faculty. Prefer simple, explicit, well-commented code over clever abstractions.

## Stack (fixed by the proposal: do not replace)
- Java 17+ (compiled with `--release 17`; developed on JDK 21)
- Jakarta Servlet 6.0 + JSP 3.1 + JSTL 3.0 (**`jakarta.*` only, never `javax.servlet`**)
- Apache Tomcat **10.1** (portable copy in `.tools/`, git-ignored)
- JDBC with MySQL Connector/J 8.4 against MySQL 8 / MariaDB 10.4 (XAMPP)
- Maven (WAR packaging), JUnit 5
- Plain HTML/CSS/JS, no CSS framework or CDN, so the app works offline

No Spring, React, Node, Hibernate or other frameworks.

## Architecture (MVC, from the proposal)
```
JSP views (WEB-INF/views) + tag files (WEB-INF/tags) + assets (CSS/JS)
        ↓  forward / redirect
Servlet controllers  (com.reclaim.controller)       ← filters: SecurityHeaders → Auth → CSRF
        ↓
Services / business logic (com.reclaim.service)    ← MatchScorer = matching algorithm
        ↓
DAOs, JDBC + PreparedStatement (com.reclaim.dao)
        ↓
MySQL  (database/schema.sql)
JavaBeans / enums: com.reclaim.model
```

| Package | Contents |
|---|---|
| `model` | JavaBeans `User`, `Item`, `Match`, `ItemFilter`; enums `ItemType`, `ItemStatus`, `Category`, `Colour`, `CampusLocation`, `UserType` |
| `dao` | `DBConnection` (reads `db.properties` / env vars), `UserDAO`, `ItemDAO` |
| `service` | `UserService`, `ItemService`, `ImageStorageService`, `MatchingService`, `MatchScorer` |
| `controller` | One servlet per use case (`@WebServlet`) |
| `filter` | `SecurityHeadersFilter`, `AuthFilter`, `CsrfFilter` (order is set in `web.xml`) |
| `listener` | `AppContextListener`: DB check at startup, dropdown lists, JDBC driver cleanup |
| `util` | `PasswordUtil` (PBKDF2), `ValidationUtil`, `TextSimilarity`, `DateUtil`, `WebUtil` |
| `exception` | `ValidationException` (field → message map), `DataAccessException` (wraps SQLException) |

### URL map (context path `/reclaim`)
`/` landing · `/register` · `/login` · `/logout` (POST) · `/dashboard` · `/profile` ·
`/report?type=lost|found` · `/items` (browse/search) · `/item?id=` · `/item/status` (POST) ·
`/my-reports` · `/matches` · `/uploads/{file}` (photos)

### Matching algorithm (`MatchScorer`)
Deterministic, rule-based, out of 100. The weights are public constants and covered by unit tests.

| Attribute | Max | Rule |
|---|---|---|
| Category | 25 | same category |
| Location | 25 | same campus zone = 25; otherwise location details share a keyword = 10 |
| Colour | 20 | same = 20; same shade family (grey/silver, gold/yellow, red/pink, brown/beige) = 10; multicolour/unsure = 5 |
| Date | 15 | found − lost: ≤1 day 15, ≤3 12, ≤7 9, ≤14 6, ≤30 3; found >1 day before lost = 0 |
| Description | 15 | 15 × overlap coefficient of keywords (title + description, stop words removed) |

Pairs scoring ≥ 40 are potential matches (High ≥ 75, Medium ≥ 55, Low ≥ 40). Matches are computed
**on demand** from OPEN reports (no `matches` table), so they are never stale. Same-user pairs are skipped.

## Conventions
- All SQL lives in DAOs and always uses `PreparedStatement`. No SQL or business logic in JSPs.
- DAOs wrap `SQLException` in `DataAccessException`; web.xml maps it to a friendly error page.
- Services validate input and throw `ValidationException`. Controllers catch it, set
  `errors` + `form` request attributes and re-render the form.
- Post/Redirect/Get after every successful POST; one-time messages via `WebUtil.flash(...)`.
- JSPs live under `WEB-INF/views` and are reached only through controllers (`WebUtil.render`).
- **Escape all user data in JSPs**: `<c:out>` in body text, `${fn:escapeXml(..)}` in attributes.
- Every POST form includes `<ui:csrf/>`. Logout is POST only.
- Enum values are stored in the DB as their constant name (`MOBILE_PHONE`); labels come from the enum.
- The session holds the `User` bean (`authUser`) with `passwordHash` nulled.
- Passwords: PBKDF2-HMAC-SHA256, 120k iterations, per-user salt (`PasswordUtil`).
- Photos: max 5 MB, type detected from magic bytes, stored as UUID names outside the webapp
  (default `<tomcat>/reclaim-uploads`, configurable with the `reclaim.uploadDir` context-param).
- Don't add `trim-directive-whitespaces` to web.xml: it strips the spaces between EL expressions.
- The `*.jspf` pattern must stay in the JSP property group, or the fragments are read as ISO-8859-1.

## Build / run
Helper scripts are in `scripts/` (Windows `.cmd`); paths are set in `scripts/config.cmd`.
- `scripts\setup-database.cmd [sample]`: create schema (+ demo data)
- `scripts\build.cmd`: `mvn clean package` (runs the unit tests) → `target/reclaim.war`
- `scripts\run.cmd`: start MySQL if needed, build, deploy to Tomcat, start Tomcat → http://localhost:8080/reclaim/
- `scripts\stop.cmd`: stop Tomcat
- Maven is not on PATH on the dev machine; IntelliJ's bundled Maven is used
  (`C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2023.3.2\plugins\maven\lib\maven3\bin\mvn.cmd`).
- DB credentials: `src/main/resources/db.properties` (git-ignored; copy `db.properties.example`).
  The app connects as the least-privilege `reclaim_app` user (`database/create_app_user.sql`), never as root.
- In sandboxed shells, Tomcat needs `CATALINA_OPTS=-Djdk.net.unixdomain.tmpdir=<tomcat>\temp`
  (already set in `scripts/config.cmd`). Otherwise the NIO connector fails with "Unable to establish loopback connection".

## Current milestone (October 2026)
Done, tested end to end:
1. **Module 1 Authentication**: register, login, logout, sessions, profile edit, change password, remember-email cookie
2. **Module 2 Reporting**: lost/found report with optional photo, My Reports, status tracking (Open/Resolved/Closed)
3. **Module 3 Browse + Matching**: search/filter, weighted matching with score breakdown, dashboard

Planned next (not built yet; don't build early unless asked):
- **Claims & verification**: a `claims` table (id, item_id, claimant_id, message, verification answer,
  status PENDING/APPROVED/REJECTED, timestamps); the finder approves or rejects; item → RESOLVED
- Real-time status/match updates (AJAX polling), background matching thread + notifications
- Admin dashboard (`users.role = 'ADMIN'` already exists), deployment to a live server

## Constraints
- Never commit secrets (`db.properties` is git-ignored). Demo password `Demo@2026` is only for sample data.
- Don't change the architecture or stack without discussing it with the product owner first.
- Work only inside `D:\ReClaim`.

### Matching Score Interpretation

ReClaim uses rule-based weighted scoring to compare lost and found item reports. The score is expressed as a percentage out of 100, with higher scores indicating a stronger match based on the attributes being compared. The score breakdown helps users understand which attributes contributed to a potential match. A high score indicates a potential match, not confirmation that the items are identical.