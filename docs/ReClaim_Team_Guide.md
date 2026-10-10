# ReClaim — Team Guide

**For:** Team Vision (Kartik Maithani, Mallika Rana, Dev Parashar)
**Covers:** everything built up to Phase-II (Git commit `2bde66b`, 9 October 2026)
**Purpose:** so that every team member can understand the project, explain it to the teacher, answer viva questions, and use this material for reports and presentations.

> **How to read this guide.** Each part starts with a simple explanation, then goes into the technical detail. If you only have 15 minutes, read Parts 1, 2, 5, 10 and 16.

---

## Table of contents

1. [ReClaim in one minute](#1-reclaim-in-one-minute)
2. [Where we are now](#2-where-we-are-now)
3. [What changed compared to the proposal (and why)](#3-what-changed-compared-to-the-proposal-and-why)
4. [The technology we use, in simple words](#4-the-technology-we-use-in-simple-words)
5. [Architecture: how the system is organised](#5-architecture-how-the-system-is-organised)
6. [The folders and files, one by one](#6-the-folders-and-files-one-by-one)
7. [The database](#7-the-database)
8. [Module 1 — Authentication and user profile](#8-module-1--authentication-and-user-profile)
9. [Module 2 — Reporting lost and found items](#9-module-2--reporting-lost-and-found-items)
10. [Module 3 — Browsing and the matching algorithm](#10-module-3--browsing-and-the-matching-algorithm)
11. [Security, explained simply](#11-security-explained-simply)
12. [All pages and URLs](#12-all-pages-and-urls)
13. [How to set up and run the project](#13-how-to-set-up-and-run-the-project)
14. [Testing, in detail](#14-testing-in-detail)
15. [How to demonstrate ReClaim to the teacher](#15-how-to-demonstrate-reclaim-to-the-teacher)
16. [Likely teacher questions, with answers](#16-likely-teacher-questions-with-answers)
17. [Honest limitations — what we must not claim](#17-honest-limitations--what-we-must-not-claim)
18. [Future direction (Phase-III)](#18-future-direction-phase-iii)
19. [Ready-made material for reports and PPTs](#19-ready-made-material-for-reports-and-ppts)
20. [Glossary](#20-glossary)

---

## 1. ReClaim in one minute

**The problem.** At Graphic Era, when someone loses something (phone, keys, ID card, earphones, wallet), they go to the front desk, ask friends, or post in WhatsApp groups. There is no single place to report it, and nothing connects "I lost X" with "I found X". Recovery depends on luck.

**Our solution.** ReClaim is a website where students and staff can:

1. create an account and log in,
2. report something they **lost** or **found** (what it is, category, colour, where, when, description, optional photo),
3. browse and search all reports,
4. see **automatic matches** — the system compares every lost report with every found report and gives each pair a **score out of 100**, with an explanation of where the points came from,
5. mark their report as **Resolved** (item returned) or **Closed**.

**How it is built.** Java web application: JSP pages for the screens, Java Servlets for handling requests, JDBC to talk to a MySQL-compatible database (MariaDB), running on Apache Tomcat. It follows the **MVC** (Model–View–Controller) design that we promised in the proposal.

---

## 2. Where we are now

| Part | Status | In one line |
|---|---|---|
| Phase-I: Proposal & design | ✅ Done | Proposal submitted (`docs/ReClaim_New.pdf`) |
| **Module 1** — Registration, login, logout, sessions, profile | ✅ Built and tested | Users can create accounts and securely log in |
| **Module 2** — Report lost / found items, My Reports, status | ✅ Built and tested | Reports are saved in the database, with optional photo |
| **Module 3** — Browse, search, filters, automatic matching | ✅ Built and tested | Lost and found reports are scored against each other |
| Automated tests | ✅ 23 tests, all passing | Matching rules, passwords, validation, image checks |
| Module 4 — Claims & ownership verification | ⏳ Planned | Next phase |
| Notifications / real-time updates | ⏳ Planned | Next phase |
| Admin dashboard | ⏳ Planned | Next phase |
| Deployment on a live server | ⏳ Planned | Final phase |

**Git history so far**

| Commit | Date | What |
|---|---|---|
| `bb70d4d` | 9 Oct 2026, 00:56 | Initial project setup (proposal documents) |
| `2bde66b` | 9 Oct 2026, 03:01 | Implement initial ReClaim modules (85 files: Modules 1–3, tests, database scripts, run scripts) |

**Size of the code:** 42 Java classes in 8 packages, 13 servlets, 13 JSP pages, 3 shared JSP fragments, 5 custom JSP tags, 5 test classes (23 tests), 2 database tables.

> **Why this guide matters.** Every team member must understand the code well enough to explain any part of it and to extend it in Phase-III. Use this guide to study the project together.

---

## 3. What changed compared to the proposal (and why)

Most of the proposal was followed exactly. These are the differences — know them, because the teacher may compare.

| Proposal said | What we actually did | Why |
|---|---|---|
| MySQL | **MariaDB 10.4** (comes with XAMPP) | MariaDB is a drop-in replacement for MySQL: same SQL, same JDBC driver. It was already installed. The code would run on MySQL 8 unchanged. |
| Apache Tomcat | **Tomcat 10.1.60** | Tomcat 10.1 supports the modern **Jakarta** Servlet API (`jakarta.servlet`). The XAMPP Tomcat (8.5) only supports the old `javax.servlet` and is end-of-life. |
| Cookies to remember search preferences | Cookie to **remember the user's email** on the login page | Simpler and more useful for the first milestone. |
| Java I/O for file uploads **and log tracking** | Java I/O for **photo uploads** only | Log tracking is not done yet (planned). Normal server logs exist in Tomcat. |
| Multithreading for background matching and notifications | **Not done yet** — matching runs when a page is opened | Planned for Phase-III. |
| Real-time updates | **Not done yet** | Planned (AJAX polling). |
| Claims & verification | **Not done yet** | Planned as Module 4. |
| Admin dashboard | **Not done yet** (a `role` column already exists in the database) | Planned. |
| Matching using category, colour, location, date, description | ✅ **Exactly these 5 attributes** | As proposed. |
| MVC + JavaBeans + JDBC + Servlets + JSP | ✅ **Exactly this** | As proposed. |

---

## 4. The technology we use, in simple words

| Technology | Version | What it is, simply | What we use it for |
|---|---|---|---|
| **Java** | JDK 21 (code compiled for Java 17) | The programming language | All server-side logic |
| **Jakarta Servlets** | 6.0 | Java classes that receive a web request and send back a response | Our "controllers" — they handle every page and form |
| **JSP** (JavaServer Pages) | 3.1 | HTML pages with small Java-related tags that are filled in on the server | Our "views" — the screens users see |
| **JSTL** (JSP Standard Tag Library) | 3.0 | Ready-made tags for loops, conditions and safe output in JSP (`<c:forEach>`, `<c:if>`, `<c:out>`) | So JSP pages contain no Java code |
| **EL** (Expression Language) | 5.0 | The `${...}` syntax in JSP to read data, e.g. `${item.title}` | Showing data from Java objects |
| **HTML / CSS / JavaScript** | — | Structure / styling / small browser behaviours | Page layout, design, image preview, password strength meter |
| **JDBC** | part of Java | Java's standard way to talk to databases | Running SQL from Java |
| **MySQL Connector/J** | 8.4 | The JDBC "driver" that knows how to talk to MySQL/MariaDB | Connecting to the database |
| **MariaDB** | 10.4 (XAMPP) | The relational database (MySQL-compatible) | Stores users and reports |
| **Apache Tomcat** | 10.1.60 | The web server that runs Java web applications | Runs ReClaim at `http://localhost:8080/reclaim/` |
| **Maven** | 3.9 | Build tool: downloads libraries, compiles, runs tests, packages | Produces `reclaim.war` |
| **JUnit** | 5.10 | Testing framework for Java | Our 23 automated tests |
| **Git** | 2.53 | Version control | Project history (GitHub repository to be added) |

**Why this stack?** (1) It is exactly what the proposal promised. (2) It has no heavy frameworks (no Spring, no React), so every layer is visible and explainable — important for an academic project. (3) Tomcat 10.1 is current and supported.

**What is a WAR file?** "Web ARchive". A single zip file (`reclaim.war`) containing our compiled classes, JSP pages, CSS/JS and libraries. You copy it into Tomcat's `webapps` folder and Tomcat runs it. The file name `reclaim` becomes the URL path `/reclaim`.

---

## 5. Architecture: how the system is organised

### 5.1 The big picture (layers)

Think of the application as floors of a building. A request comes in at the top and goes down floor by floor; the answer goes back up.

```
            ┌──────────────────────────────────────────────┐
            │  USER — web browser                          │
            └───────────────────────┬──────────────────────┘
                                    │  HTTP request (GET / POST)
            ┌───────────────────────▼──────────────────────┐
            │  FILTERS (run before every request)          │
            │  1. SecurityHeadersFilter  2. AuthFilter     │
            │  3. CsrfFilter                               │
            └───────────────────────┬──────────────────────┘
            ┌───────────────────────▼──────────────────────┐
  CONTROLLER│  SERVLETS (13)  e.g. LoginServlet,           │
            │  ReportItemServlet, MatchesServlet           │
            └──────────┬────────────────────────┬──────────┘
                       │ calls                  │ forwards data to
            ┌──────────▼──────────┐   ┌─────────▼──────────┐
   BUSINESS │  SERVICES           │   │  VIEW: JSP pages   │
    LOGIC   │  UserService,       │   │  (+ JSTL, tag      │
            │  ItemService,       │   │   files, CSS, JS)  │
            │  MatchingService,   │   └─────────┬──────────┘
            │  MatchScorer, ...   │             │ HTML response
            └──────────┬──────────┘             ▼ back to browser
            ┌──────────▼──────────┐
  DATA      │  DAOs (JDBC)        │      MODEL = JavaBeans
  ACCESS    │  UserDAO, ItemDAO,  │      (User, Item, Match)
            │  DBConnection       │      carried between all layers
            └──────────┬──────────┘
            ┌──────────▼──────────┐
            │  MariaDB: reclaim_db│
            │  tables users, items│
            └─────────────────────┘
```

### 5.2 Each layer in simple words

| Layer | Simple meaning | Rule we follow |
|---|---|---|
| **View (JSP)** | What the user sees | Only displays data. No SQL, no business logic. |
| **Filters** | Security guards at the door | Check every request before it reaches a servlet. |
| **Controller (Servlets)** | Receptionist | Reads the form/URL, asks the right service to do the work, then chooses which page to show. |
| **Service** | The brain | Business rules: validation, password checking, matching. Does not know about HTTP or SQL. |
| **DAO** (Data Access Object) | The librarian | The **only** place with SQL. Saves and loads data. |
| **Model (JavaBeans)** | The data containers | Simple classes like `User` and `Item` with private fields and getters/setters. |
| **Database** | The storage room | Tables `users` and `items`. |

### 5.3 How MVC maps to our code

| MVC part | Our code |
|---|---|
| **Model** | `com.reclaim.model` (JavaBeans + enums) and the data layers behind them |
| **View** | `src/main/webapp/WEB-INF/views/*.jsp` + tag files |
| **Controller** | `com.reclaim.controller` (servlets) |

### 5.4 The life of one request (example: opening "My Reports")

1. Browser asks for `http://localhost:8080/reclaim/my-reports`.
2. **SecurityHeadersFilter** adds protective headers to the response.
3. **AuthFilter** checks the session: is someone logged in? If not → redirect to `/login`. If yes → continue.
4. **CsrfFilter** — this is a GET request, so it only makes sure a CSRF token exists for later forms.
5. Tomcat calls **MyReportsServlet.doGet()**.
6. The servlet reads the logged-in user from the session, asks **ItemService** for that user's reports and **MatchingService** for the match counts.
7. **ItemService** calls **ItemDAO.findByUser()**, which runs a SQL `SELECT ... JOIN users ...` with a **PreparedStatement** and turns each row into an `Item` object.
8. The servlet puts the list into the request (`request.setAttribute("items", ...)`) and **forwards** to `WEB-INF/views/items/my-reports.jsp`.
9. The JSP loops over the items with `<c:forEach>` and produces HTML.
10. The HTML goes back to the browser.

### 5.5 Two important patterns

- **Post/Redirect/Get (PRG).** After a successful form submit (POST), the servlet sends a **redirect** instead of showing a page directly. Why: if the user presses refresh, the browser repeats a harmless GET, not the POST — so no duplicate accounts or duplicate reports.
- **Flash messages.** A one-time message ("Report submitted", "Welcome back") is stored in the session, shown once on the next page, then removed.

---

## 6. The folders and files, one by one

```
D:\ReClaim
├── pom.xml                       Maven build file (libraries + how to build)
├── README.md                     Short setup + demo instructions
├── DEVELOPER_GUIDE.md            Technical project guide / conventions
├── .gitignore                    Files Git must ignore (passwords, build output, Tomcat)
├── docs/                         Proposal, Phase-II report, PPT, this guide
├── database/                     SQL scripts
├── scripts/                      One-click Windows scripts (build, run, stop, setup DB)
├── .tools/                       (not in Git) portable Tomcat 10.1.60
├── target/                       (not in Git) build output → reclaim.war
└── src/
    ├── main/java/com/reclaim/    All Java source code (8 packages)
    ├── main/resources/           db.properties (DB login, not in Git) + example file
    ├── main/webapp/              Web part: JSP pages, CSS, JS, images, web.xml
    └── test/java/com/reclaim/    Automated tests (JUnit)
```

### 6.1 Root files

| File | What it does |
|---|---|
| `pom.xml` | Tells Maven: project name, Java version (17), libraries (Jakarta Servlet API, JSTL, MySQL Connector/J, JUnit) and to package a WAR named `reclaim.war`. Libraries marked `provided` (Servlet API, EL) are supplied by Tomcat, so they are not packed into the WAR. |
| `.gitignore` | Keeps `target/`, `.tools/`, `uploads/` and **`db.properties`** (contains the DB password) out of Git. |
| `README.md` | Quick start and demo flow. |
| `DEVELOPER_GUIDE.md` | Full technical reference: architecture, conventions, matching rules, build steps. |

### 6.2 `database/`

| File | What it does |
|---|---|
| `schema.sql` | Creates the database `reclaim_db` and the tables `users` and `items`. Uses `IF NOT EXISTS`, so it is safe to run again — it never deletes anything. |
| `create_app_user.sql` | Template that creates a special database account `reclaim_app` with only SELECT/INSERT/UPDATE/DELETE rights. The application never uses `root`. You replace `CHANGE_ME_PASSWORD` before running it. |
| `sample_data.sql` | Demo data: 3 demo accounts and 10 realistic reports (dates relative to today). Safe to re-run: it deletes only the demo accounts first. Password for all demo accounts: `Demo@2026`. |

### 6.3 `scripts/` (Windows `.cmd` files)

| Script | What it does |
|---|---|
| `config.cmd` | Shared settings: where Tomcat, Maven and MySQL are. **Edit this on a new computer.** |
| `setup-database.cmd` | Runs `schema.sql` (and `sample_data.sql` if you add the word `sample`). |
| `build.cmd` | Runs `mvn clean package`: compiles, runs the 23 tests, builds `target\reclaim.war`. |
| `run.cmd` | One-click start: starts MySQL if not running → builds → copies the WAR to Tomcat → starts Tomcat (or lets a running Tomcat redeploy). |
| `stop.cmd` | Stops Tomcat. |

### 6.4 Java packages — `src/main/java/com/reclaim/`

**`model/` — the data containers (JavaBeans and enums)**

| Class | What it is |
|---|---|
| `User` | One user account (id, name, email, password hash, type, university ID, department, phone, role, dates). Has helpers `getFirstName()`, `getInitials()`. |
| `Item` | One lost or found report (type, title, category, description, colour, location, details, date, photo, status, reporter name). |
| `Match` | A pair (one lost item + one found item) with 5 part-scores; `getScore()` adds them; `getConfidence()` says High/Medium/Low. |
| `ItemFilter` | The search options from the Browse page (type, category, location, status, keyword). |
| `ItemType` | Enum: `LOST`, `FOUND` (with `opposite()` → LOST↔FOUND). |
| `ItemStatus` | Enum: `OPEN`, `RESOLVED`, `CLOSED`. |
| `Category` | Enum: 13 categories (see Part 9). |
| `Colour` | Enum: 16 colours, each in a "shade family" (e.g. Grey and Silver are both GREY family). |
| `CampusLocation` | Enum: 12 campus zones. |
| `UserType` | Enum: `STUDENT`, `FACULTY`, `STAFF`. |

*Why enums?* A fixed list means a report's category is always spelled exactly the same, so matching can compare them reliably. The database stores the enum name (e.g. `MOBILE_PHONE`); the screen shows the label ("Mobile Phone").

**`dao/` — database access (the only place with SQL)**

| Class | What it does |
|---|---|
| `DBConnection` | Reads `db.properties` (or environment variables) once and opens JDBC connections with `DriverManager`. |
| `UserDAO` | SQL for users: create, find by email/id, check email exists, update profile, update password, update last login. |
| `ItemDAO` | SQL for items: create, find by id, find by user, find all OPEN of a type (for matching), recent items, search with filters, update status (only by owner), counts for the dashboard. |

Every query uses a **PreparedStatement** (`?` placeholders). Every connection is closed automatically with **try-with-resources**. SQL errors are wrapped in `DataAccessException`.

**`service/` — business logic**

| Class | What it does |
|---|---|
| `UserService` | Register (validate + hash password + save), authenticate (check password), update profile, change password. |
| `ItemService` | Create a report (validate all fields, save photo, insert), browse/search, update status, counts. |
| `ImageStorageService` | Saves photos to disk with Java I/O: checks size (≤ 5 MB), checks the real file type from the first bytes, gives a random UUID file name. |
| `MatchingService` | Finds potential matches: loads the open reports of the opposite type, scores each pair, keeps score ≥ 40, sorts best first. |
| `MatchScorer` | **The matching algorithm** — the 5 scoring rules (Part 10). Pure Java, no database, so it is fully unit-tested. |

**`controller/` — the 13 servlets** (full URL list in Part 12)

`HomeServlet`, `RegisterServlet`, `LoginServlet`, `LogoutServlet`, `DashboardServlet`, `ProfileServlet`, `ReportItemServlet`, `BrowseServlet`, `ItemDetailServlet`, `ItemStatusServlet`, `MyReportsServlet`, `MatchesServlet`, `ImageServlet`.

**`filter/` — run on every request (order set in `web.xml`)**

| Filter | Job |
|---|---|
| `SecurityHeadersFilter` | Adds browser security headers (no MIME sniffing, no framing, referrer policy). |
| `AuthFilter` | Lets only public pages (home, login, register, CSS/JS) through without login; everything else needs a logged-in session. Remembers where a guest wanted to go. |
| `CsrfFilter` | Makes sure every POST form carries the secret CSRF token of the session. |

**`listener/`**

| Class | Job |
|---|---|
| `AppContextListener` | Runs when Tomcat starts the app: tests the database connection (and logs the result), prepares the photo folder, and makes the dropdown lists (categories, colours, locations…) available to all JSPs. On shutdown it unregisters the JDBC driver (prevents memory leaks). |

**`util/` — helpers**

| Class | Job |
|---|---|
| `PasswordUtil` | Hashes and verifies passwords with PBKDF2-SHA256 (120,000 rounds, random salt per user). |
| `ValidationUtil` | Checks email, Indian mobile number, name, password strength, etc. |
| `TextSimilarity` | Turns text into keywords and measures how much two keyword sets overlap (for matching). |
| `DateUtil` | Formats dates nicely ("09 Oct 2026", "3 days ago"). |
| `WebUtil` | Small helpers for servlets: get logged-in user, forward to a JSP, redirect, flash messages, CSRF token. |

**`exception/`**

| Class | Job |
|---|---|
| `ValidationException` | "The user typed something wrong" — carries one message per form field. |
| `DataAccessException` | "The database failed" — leads to a friendly error page. |

### 6.5 Web folder — `src/main/webapp/`

| Path | What it is |
|---|---|
| `WEB-INF/web.xml` | The deployment descriptor: filter order, session timeout (30 min), session cookie settings, UTF-8 encoding, error pages. |
| `WEB-INF/views/` | All JSP pages. They are inside `WEB-INF`, so a browser **cannot open them directly** — only through a servlet. |
| `WEB-INF/views/common/` | `taglibs.jspf` (tag declarations), `header.jspf` (top bar + navigation + flash message), `footer.jspf`. Included in every page. |
| `WEB-INF/views/auth/` | `login.jsp`, `register.jsp` |
| `WEB-INF/views/items/` | `report.jsp`, `browse.jsp`, `detail.jsp`, `my-reports.jsp`, `matches.jsp` |
| `WEB-INF/views/` | `home.jsp` (landing page), `dashboard.jsp`, `profile.jsp` |
| `WEB-INF/views/error/` | `404.jsp`, `500.jsp`, `database.jsp` |
| `WEB-INF/tags/` | **Custom JSP tags** (reusable components): `icon.tag` (SVG icons), `csrf.tag` (hidden token field), `itemCard.tag` (report card), `miniItem.tag`, `matchCard.tag` (match with score ring and breakdown bars). |
| `assets/css/style.css` | The whole design (plain CSS, no framework, works offline). |
| `assets/js/app.js` | Small extras: mobile menu, show/hide password, strength meter, photo preview and size check, character counter, confirmation pop-ups, double-submit protection. |
| `assets/img/logo.svg` | ReClaim logo. |

### 6.6 Tests — `src/test/java/com/reclaim/`

`service/MatchScorerTest`, `service/ImageStorageServiceTest`, `util/TextSimilarityTest`, `util/PasswordUtilTest`, `util/ValidationUtilTest` — details in Part 14.

---

## 7. The database

### 7.1 Tables and relationship

```
users (1) ─────────────< items (many)
   id  (Primary Key) ◄────── user_id (Foreign Key, ON DELETE CASCADE)
```

**One user can have many reports. Each report belongs to exactly one user.** If a user is deleted, their reports are deleted too (`ON DELETE CASCADE`).

### 7.2 `users` table

| Column | Type | Meaning |
|---|---|---|
| `id` | INT, PK, AUTO_INCREMENT | Unique number for each user |
| `full_name` | VARCHAR(100) | Name |
| `email` | VARCHAR(150), **UNIQUE** | Login email (no two accounts with the same email) |
| `password_hash` | VARCHAR(255) | **Not the password** — a PBKDF2 hash (`iterations:salt:hash`) |
| `user_type` | ENUM('STUDENT','FACULTY','STAFF') | Kind of campus member |
| `university_id` | VARCHAR(20), optional | Student / employee ID |
| `department` | VARCHAR(100), optional | Department / course |
| `phone` | VARCHAR(15), optional | Mobile number |
| `role` | ENUM('USER','ADMIN') | Reserved for the future admin module |
| `created_at` | TIMESTAMP | When the account was created |
| `last_login_at` | TIMESTAMP | Last successful login |

### 7.3 `items` table

| Column | Type | Meaning |
|---|---|---|
| `id` | INT, PK | Report number |
| `user_id` | INT, **FK → users.id** | Who reported it |
| `type` | ENUM('LOST','FOUND') | Lost or found |
| `title` | VARCHAR(100) | Item name |
| `category` | VARCHAR(40) | Enum name, e.g. `MOBILE_PHONE` |
| `description` | VARCHAR(1000) | Details |
| `colour` | VARCHAR(30) | Enum name, e.g. `BLACK` |
| `location` | VARCHAR(40) | Enum name, e.g. `LIBRARY` |
| `location_details` | VARCHAR(150), optional | Free text, e.g. "2nd floor, near window" |
| `incident_date` | DATE | Date lost / found |
| `image_path` | VARCHAR(255), optional | File name of the photo (photo itself is on disk) |
| `status` | ENUM('OPEN','RESOLVED','CLOSED') | Report status (starts OPEN) |
| `created_at`, `updated_at` | TIMESTAMP | Created / last changed (automatic) |

**Indexes** (make searching faster): `(type, status)`, `user_id`, `category`.

### 7.4 Database design points to mention

- **Primary keys** on both tables; **foreign key** from items to users.
- **No duplicated data:** the reporter's name is not stored in `items`; it is fetched with a **JOIN** when needed.
- **Constraints:** `NOT NULL`, `UNIQUE(email)`, `ENUM` value lists.
- **Least privilege:** the app logs in as `reclaim_app`, which can only read/write rows in `reclaim_db` — it cannot drop tables or touch other databases.
- **Photos are not stored in the database** — only their file name. The files live on disk (`<tomcat>/reclaim-uploads/`).
- **Matches are not stored** — they are calculated fresh each time (Part 10).
- **Not done yet:** explicit transactions, stored procedures, triggers.

---

## 8. Module 1 — Authentication and user profile

### 8.1 What the user can do

- **Register** with name, email, password (+ confirm), type (Student/Faculty/Staff), and optional university ID, phone, department.
- **Log in** with email and password; optional "Remember my email on this device".
- See the **Dashboard** (greeting, own statistics, best matches, recent campus reports).
- View and **edit the profile**; **change password**.
- **Log out**.

### 8.2 Validation rules (checked on the server)

| Field | Rule |
|---|---|
| Full name | 2–100 characters; letters, spaces, `.` `'` `-` only |
| Email | Valid format, max 150 characters, must not already exist |
| Password | 8–64 characters, at least one letter **and** one number |
| Confirm password | Must equal the password |
| User type | Must be Student, Faculty or Staff |
| University ID (optional) | 3–20 characters: letters, digits, `/`, `-` |
| Phone (optional) | Indian mobile: 10 digits starting with 6–9 |
| Department (optional) | Max 100 characters |

The browser also checks some fields (HTML + JavaScript) for convenience, but **the server always re-checks**, because browser checks can be bypassed.

### 8.3 Step by step: registration

1. `GET /register` → `RegisterServlet` shows `register.jsp`. (The CSRF filter puts a secret token in the session; the form includes it as a hidden field.)
2. User submits → `POST /register`.
3. `CsrfFilter` checks the token → OK.
4. `RegisterServlet` calls `UserService.register(...)`.
5. The service trims the inputs, lowercases the email, checks every rule, and checks the email is not taken (`UserDAO.emailExists`).
6. If any rule fails → `ValidationException` with a message per field → the form is shown again **with the typed values kept** and red messages under the wrong fields.
7. If all good → password is hashed with `PasswordUtil.hash()` → `UserDAO.create()` runs an `INSERT`.
8. Rare case: two people register the same email at the same instant → the database's UNIQUE rule rejects the second → shown as a normal error.
9. Success → flash message "Account created…" → **redirect** to `/login` with the email already filled in.

### 8.4 Step by step: login

1. `POST /login` → `LoginServlet` → `UserService.authenticate(email, password)`.
2. Find the user by email (`UserDAO.findByEmail`).
3. Verify the password with `PasswordUtil.verify()` (re-computes the hash with the stored salt and compares in constant time).
4. If wrong email **or** wrong password → the same message "Invalid email or password" (so attackers can't find out which emails are registered). If the email doesn't exist, a dummy hash is still checked so the response time is similar.
5. Success → update `last_login_at` → remove the password hash from the user object.
6. **The old session is destroyed and a new one is created** (prevents "session fixation" attacks). The `User` object is stored in the session as `authUser`.
7. If "Remember my email" was ticked → an HttpOnly cookie `reclaim_email` (30 days) stores the email only — never the password.
8. Redirect to the page the user originally wanted, or to `/dashboard`.

### 8.5 Sessions in simple words

- After login, Tomcat gives the browser a cookie with a random **session ID**. On every request the browser sends it back, and Tomcat finds that user's session data on the server.
- Our `AuthFilter` checks "is there an `authUser` in the session?" for every protected page.
- Session settings (`web.xml`): times out after **30 minutes** of inactivity; the session cookie is **HttpOnly** (JavaScript cannot read it); session ID is **never put in URLs**.
- **Logout** (`POST /logout`) calls `session.invalidate()` — everything in the session is destroyed. Protected pages also send "no-cache" headers, so pressing Back after logout does not show private pages.

### 8.6 Password storage in simple words

We never store the password itself. We store a **hash** — a one-way scrambled fingerprint.
- **PBKDF2-HMAC-SHA256** (built into Java), **120,000 iterations** (slow on purpose, so guessing millions of passwords is impractical), **16-byte random salt** per user (so two users with the same password get different hashes).
- Stored as: `120000:<salt in Base64>:<hash in Base64>`.
- At login, we hash what the user typed with the same salt and compare.

### 8.7 Profile

- `GET /profile` shows the details card (initials avatar, report counts, member since, last login) and two forms.
- `POST /profile` with `action=profile` → `UserService.updateProfile` (same validation rules; email cannot be changed) → the session copy of the user is refreshed so the name updates in the top bar.
- `POST /profile` with `action=password` → `UserService.changePassword` (current password must be correct; new one must be strong, confirmed, and different).

---

## 9. Module 2 — Reporting lost and found items

### 9.1 The report form (`/report?type=lost` or `?type=found`)

| Field | Required | Rule |
|---|---|---|
| Lost / Found switch | Yes | LOST or FOUND (switching changes colours and texts without reloading) |
| Item name (title) | Yes | 3–100 characters |
| Category | Yes | One of 13 |
| Main colour | Yes | One of 16 (shown as colour chips) |
| Campus location | Yes | One of 12 zones |
| Date lost / found | Yes | Not in the future, not older than 365 days |
| Location details | No | Max 150 characters |
| Description | Yes | 10–1000 characters (live character counter) |
| Photo | No | JPG/PNG/GIF/WEBP, max 5 MB (live preview) |

**Categories (13):** Mobile Phone · Laptop / Tablet · Earphones / Headphones · Other Electronics · Wallet / Purse · Keys · ID Card / Documents · Bag / Backpack · Books / Stationery · Clothing · Watch / Jewellery / Accessories · Water Bottle / Lunch Box · Other

**Colours (16) and their shade families:** Black · White · Grey & Silver (grey family) · Gold & Yellow (yellow family) · Orange · Red & Pink (red family) · Purple · Blue · Green · Brown & Beige (brown family) · Multicolour · Other / Not sure

**Campus locations (12):** Main Gate / Security · Academic Block / Classrooms · Computer Labs · Library · Cafeteria / Food Court · Auditorium / Seminar Hall · Administrative Block · Sports Ground / Gym · Hostel · Parking Area · Bus Stop / University Transport · Other (see details)

> These locations are general campus zones, not official building names. They can easily be changed in `CampusLocation.java`.

### 9.2 Step by step: submitting a report

1. The form is sent as `multipart/form-data` (needed for file upload). `ReportItemServlet` has `@MultipartConfig` so Tomcat can read it.
2. `CsrfFilter` checks the token (and detects uploads that are far too large).
3. The servlet gets the photo part (`request.getPart("image")`) and calls `ItemService.createReport(...)`.
4. The service fills an `Item` object (status always starts **OPEN**) and checks every rule. Errors → the form comes back with the user's text kept and messages under each field.
5. Only if everything is valid, the photo is saved by `ImageStorageService` (see 9.3).
6. `ItemDAO.create()` runs the `INSERT`. If the insert fails, the saved photo is deleted again (no orphan files).
7. Redirect to `/item?id=<new id>` — the detail page, which **immediately shows potential matches** for the new report.

### 9.3 Photo upload in simple words (Java I/O)

- Max 5 MB (checked in the browser **and** on the server).
- The real file type is detected from the **first bytes** of the file ("magic numbers": JPEG starts with `FF D8 FF`, PNG with `89 50 4E 47`, …). We don't trust the file name — a virus renamed `photo.jpg` is rejected.
- The file is renamed to a random **UUID** (e.g. `3f2c…-….png`) so users can't overwrite each other's files or use tricks like `../../`.
- Saved **outside** the web application folder (`<tomcat>/reclaim-uploads/`), so photos survive redeployments.
- Shown through `ImageServlet` (`/uploads/<file>`), which only serves valid file names from that folder, and only to logged-in users.

### 9.4 My Reports and status tracking

- `/my-reports` lists all your reports (tabs: All / Lost / Found) with photo, badges, date, "reported 3 days ago", and how many potential matches each open report has.
- Status lifecycle:

```
        ┌──────── Re-open ─────────┐
        ▼                           │
      OPEN ──► RESOLVED (item returned / got it back)
        │
        └────► CLOSED   (withdrawn / reported by mistake)
```

- Only **OPEN** reports take part in matching and appear in Browse by default.
- Changing status is a `POST /item/status`. The SQL is `UPDATE items SET status=? WHERE id=? AND user_id=?` — the `user_id` condition means **only the owner can change it** (tested: another user's attempt has no effect).

---

## 10. Module 3 — Browsing and the matching algorithm

### 10.1 Browse and search (`/items`)

- Keyword search in title, description and location details (SQL `LIKE`, with special characters escaped).
- Filters: type (All/Lost/Found), category, location, status (Open by default, or Resolved/Closed/Any). Changing a filter applies it immediately.
- Filters are in the URL (`/items?type=LOST&category=KEYS`), so a search can be bookmarked or shared.
- Shows up to the newest 100 results as cards.
- The SQL `WHERE` clause is built from the chosen filters, **but every value is still a `?` parameter** — never pasted into the SQL text.

### 10.2 The matching algorithm (the most important part to explain)

**Idea in one sentence:** for every lost report and every open found report, give points for each detail they agree on; the total out of 100 tells how likely they are the same item.

**The five rules (in `MatchScorer.java`):**

| Attribute | Max points | Rule |
|---|---|---|
| **Category** | 25 | Same category → 25, otherwise 0 |
| **Location** | 25 | Same campus zone → 25. Different zones, but the "location details" texts share a keyword → 10. Otherwise 0 |
| **Colour** | 20 | Same colour → 20. Same shade family (grey/silver, gold/yellow, red/pink, brown/beige) → 10. One of them is "Multicolour" or "Not sure" → 5. Otherwise 0 |
| **Date** | 15 | d = date found − date lost (in days). Found within 1 day (or up to 1 day *before*, to allow for mistakes) → 15; ≤ 3 days → 12; ≤ 7 → 9; ≤ 14 → 6; ≤ 30 → 3; more → 0. Found **more than 1 day before** it was lost → 0 (impossible) |
| **Description** | 15 | 15 × (keyword overlap between the two reports' title + description), rounded |
| **Total** | **100** | |

**Description similarity, step by step (`TextSimilarity.java`):**
1. Take title + description, convert to lowercase.
2. Split into words (anything that is not a letter or digit is a separator).
3. Remove very common words ("the", "my", "near", "lost", "found", …) and words shorter than 2 letters.
4. Remove a plural "s" ("keys" → "key"), but not from words ending in "ss" ("glass").
5. Overlap = (number of common keywords) ÷ (size of the **smaller** keyword set).
   *Why the smaller set?* The owner usually writes a long description and the finder a short one; we don't want the length difference alone to lower the score. (This measure is called the **overlap coefficient**.)

**Decision thresholds:**

| Score | Meaning |
|---|---|
| ≥ 75 | **High** confidence (green) |
| 55 – 74 | **Medium** (amber) |
| 40 – 54 | **Low** (grey) |
| < 40 | Not shown |

**Worked example (from our testing):**

| | Lost report | Found report | Points |
|---|---|---|---|
| Category | Mobile Phone | Mobile Phone | 25 / 25 |
| Colour | Black | Black | 20 / 20 |
| Location | Library | Library | 25 / 25 |
| Date | 8 Oct | 9 Oct (1 day later) | 15 / 15 |
| Description | "Black Samsung Galaxy phone… transparent back cover… cracked screen guard" | "Samsung phone with black cover… cracked screen guard" | 12 / 15 |
| **Total** | | | **97 → High** |

**How matching runs (`MatchingService`):**
1. Only **OPEN** reports are used.
2. For a lost report, load all open **found** reports (and vice versa) with one SQL query.
3. Skip pairs where both reports are by the **same user**.
4. Score each pair with `MatchScorer`; keep pairs scoring **40 or more**.
5. Sort by score (highest first; ties → newest first).
6. Matches are calculated **when the page is opened** and **not stored** in the database. Advantage: always up to date (a resolved report disappears from matches immediately). Fine for campus scale; for a very large system we would move this to a background job (planned).

**Where matches appear:** the item detail page (owner only), the Matches page (`/matches`, all your matches with the scoring table explained), and the Dashboard (top 3). Each match card shows both reports, a score ring, and **five bars** showing the points for each attribute — so the result is **explainable**, not a black box.

> **It is NOT AI or machine learning.** It is a deterministic, rule-based algorithm: the same inputs always give the same score. Say "rule-based weighted scoring".

---

## 11. Security, explained simply

| Threat (simple) | How ReClaim prevents it | Where |
|---|---|---|
| Someone reads the database and sees passwords | Passwords are stored only as salted PBKDF2 hashes | `PasswordUtil` |
| **SQL injection** (typing SQL into a form to attack the DB) | Every query uses `PreparedStatement` with `?` parameters | All DAOs |
| **XSS** (typing `<script>` into a form so it runs in others' browsers) | All user text is escaped when shown (`<c:out>`, `fn:escapeXml`) | All JSPs |
| **CSRF** (a malicious website secretly submits a form as you) | Every POST form must carry a secret per-session token | `CsrfFilter`, `csrf.tag` |
| **Session fixation** (attacker plants a session ID before you log in) | A brand-new session is created at login | `LoginServlet` |
| Session ID stolen by JavaScript | Session cookie is HttpOnly; ID never in URLs | `web.xml` |
| Seeing private pages without login | `AuthFilter` blocks them; no-cache headers after logout | `AuthFilter` |
| Changing someone else's report | SQL only updates rows where `user_id` = logged-in user | `ItemDAO.updateStatus` |
| Uploading a malicious file | Type checked from file bytes, size limit, random file name, stored outside web folder | `ImageStorageService` |
| Path traversal (`../../secret`) through image URLs | Only UUID-style file names are accepted | `ImageStorageService.resolve` |
| Finding out which emails are registered | Same error message and similar timing for wrong email / wrong password | `UserService.authenticate` |
| Clickjacking / MIME sniffing | Security headers | `SecurityHeadersFilter` |
| Database account misuse | App uses `reclaim_app` with minimum rights, never root | `create_app_user.sql` |
| Password committed to GitHub | `db.properties` is git-ignored; only an example file is committed | `.gitignore` |

---

## 12. All pages and URLs

Base address: `http://localhost:8080/reclaim`

| URL | Method | Servlet | Login needed | What it does |
|---|---|---|---|---|
| `/` | GET | HomeServlet | No | Landing page (logged-in users → dashboard) |
| `/register` | GET, POST | RegisterServlet | No | Registration form / create account |
| `/login` | GET, POST | LoginServlet | No | Login form / log in |
| `/logout` | POST | LogoutServlet | Yes | Log out |
| `/dashboard` | GET | DashboardServlet | Yes | Stats, top matches, recent reports |
| `/profile` | GET, POST | ProfileServlet | Yes | View/edit profile, change password |
| `/report?type=lost\|found` | GET, POST | ReportItemServlet | Yes | Report form / save report |
| `/items` | GET | BrowseServlet | Yes | Browse, search, filter |
| `/item?id=N` | GET | ItemDetailServlet | Yes | One report (+ matches if yours) |
| `/item/status` | POST | ItemStatusServlet | Yes | Mark Resolved / Closed / Re-open |
| `/my-reports` | GET | MyReportsServlet | Yes | Your reports |
| `/matches` | GET | MatchesServlet | Yes | All your potential matches |
| `/uploads/<file>` | GET | ImageServlet | Yes | Shows a photo |

Error pages: 404 (not found), 500 (unexpected error), and a special "can't reach the database" page.

---

## 13. How to set up and run the project

### 13.1 What you need (one time)

| Tool | Where it comes from |
|---|---|
| JDK 17 or newer (we use 21) | Oracle / Adoptium |
| XAMPP (for MariaDB/MySQL) | apachefriends.org |
| Apache Tomcat **10.1** (zip version) | tomcat.apache.org → extract to `D:\ReClaim\.tools\apache-tomcat-10.1.60` (or change the path in `scripts\config.cmd`) |
| Maven 3.9 | Either install Maven, or use the copy bundled with IntelliJ IDEA (path set in `scripts\config.cmd`) |
| Git | git-scm.com |

> **On a team member's computer:** `.tools/` (Tomcat) and `db.properties` are **not in Git** on purpose. Each person must download Tomcat, create their own `db.properties`, and check the paths in `scripts\config.cmd`.

### 13.2 First-time setup

1. Start **MySQL** from the XAMPP Control Panel.
2. Create the database and tables (and demo data):
   ```
   scripts\setup-database.cmd sample
   ```
3. Create the app's database account: open `database\create_app_user.sql`, replace `CHANGE_ME_PASSWORD` with a password, run it as root:
   ```
   C:\xampp\mysql\bin\mysql.exe -u root < database\create_app_user.sql
   ```
   (Don't commit the file with your real password.)
4. Copy `src\main\resources\db.properties.example` to `db.properties` in the same folder and put the same password in it.

### 13.3 Every time you want to run it

```
scripts\run.cmd
```
Then open **http://localhost:8080/reclaim/**. To stop: `scripts\stop.cmd`.

**What `run.cmd` does, step by step:**
1. Loads settings from `config.cmd`.
2. Checks if something is listening on port **3306** (MySQL). If not, starts XAMPP's MySQL.
3. Runs `mvn clean package` → compiles code, **runs all 23 tests**, builds `target\reclaim.war`. If a test fails, it stops.
4. Copies `reclaim.war` into Tomcat's `webapps` folder.
5. Checks port **8080**. If Tomcat is not running → starts it. If it is running → Tomcat notices the new WAR and redeploys automatically.

**What happens when Tomcat starts ReClaim:** it unpacks the WAR, `AppContextListener` runs, checks the DB connection and writes either `ReClaim started - database connection OK` or a clear "DATABASE IS NOT REACHABLE" message into the Tomcat console/log.

### 13.4 Build or test only

```
scripts\build.cmd
```
Shows each test class and a final line like `Tests run: 23, Failures: 0, Errors: 0`.

### 13.5 Troubleshooting

| Problem | Likely cause | Fix |
|---|---|---|
| Browser: "can't reach the database" page | MySQL not running, or wrong password in `db.properties` | Start MySQL in XAMPP; check `db.properties` |
| Tomcat window closes immediately | Port 8080 already used (e.g. XAMPP's own Tomcat) | Stop the other program, or change Tomcat's port |
| `BUILD FAILURE` | A test failed or code doesn't compile | Read the first error message in the output |
| `mvn` not found | Maven path wrong | Fix the `MVN` line in `scripts\config.cmd` |
| Tomcat error "Unable to establish loopback connection" | Windows temp folder restriction | Already handled by `CATALINA_OPTS` in `config.cmd` |
| Login page says session expired | Page was open > 30 minutes (CSRF token/session expired) | Just submit again |

---

## 14. Testing, in detail

### 14.1 Two kinds of testing we did

1. **Automated unit tests (JUnit 5)** — small programs that test one piece of logic and check the result automatically. They run every time we build.
2. **Manual functional testing** — using the real application (browser and HTTP requests) step by step and checking results, including the database.

### 14.2 How to run the automated tests

```
scripts\build.cmd
```
(or `mvn test`). Current result: **23 tests run, 0 failures, 0 errors.**

### 14.3 All 23 automated tests, explained

**`MatchScorerTest` — the matching algorithm (8 tests)**

| # | Test | What it proves |
|---|---|---|
| 1 | `weightsAddUpTo100` | 25 + 20 + 25 + 15 + 15 = 100 |
| 2 | `identicalReportsScore100` | Two identical reports get 100 and "High" |
| 3 | `argumentOrderDoesNotMatter` | score(lost, found) = score(found, lost) |
| 4 | `unrelatedReportsAreNotMatched` | A brown wallet in the hostel and a blue bottle at the sports ground score below 40 |
| 5 | `colourRules` | Same = 20, grey/silver = 10, gold/yellow = 10, with multicolour = 5, unsure+unsure = 5, black/white = 0 |
| 6 | `dateRules` | Every date tier: 0–1 day → 15, 3 → 12, 7 → 9, 14 → 6, 30 → 3, 31 → 0, found 5 days *before* lost → 0, 1 day before → 15 (tolerance) |
| 7 | `locationDetailsGivePartialCredit` | Different zones = 0; sharing "third floor" in details = 10; same zone = 25 |
| 8 | `realisticPairIsAStrongMatch` | Real-looking earbuds reports score ≥ 75 |

**`TextSimilarityTest` — description similarity (4 tests)**

| # | Test | What it proves |
|---|---|---|
| 9 | `keywordsDropStopWordsAndPunctuationAndPlurals` | "I lost my KEYS near the Library, with a red keychain!" → {key, library, red, keychain} |
| 10 | `keywordsHandleNullAndEmpty` | Empty / missing text gives no keywords (no crash) |
| 11 | `overlapUsesTheShorterSet` | {samsung, phone} vs a longer set containing both → 1.0; {blue, bottle} vs {blue, bag, pen} → 0.5 |
| 12 | `overlapIsZeroForEmptyOrDisjointSets` | No common words → 0 |

**`PasswordUtilTest` — password hashing (4 tests)**

| # | Test | What it proves |
|---|---|---|
| 13 | `correctPasswordVerifies` | The right password is accepted |
| 14 | `wrongPasswordFails` | Wrong case, empty or null passwords are rejected |
| 15 | `samePasswordGetsDifferentSalts` | Same password hashed twice → different hashes (random salt) |
| 16 | `malformedStoredHashIsRejected` | Broken stored hash values don't crash; they just fail |

**`ValidationUtilTest` — input checks (5 tests)**

| # | Test | What it proves |
|---|---|---|
| 17 | `email` | Good email accepted; incomplete or spaced emails rejected |
| 18 | `phone` | 98765… accepted; numbers starting with 1, or too short, rejected |
| 19 | `name` | Normal names and D'Souza accepted; "A" and `<script>` rejected |
| 20 | `passwordStrength` | "campus2026" OK; too short / letters only / digits only rejected |
| 21 | `cleanTrimsAndNullsBlank` | Extra spaces removed; blank becomes empty |

**`ImageStorageServiceTest` — upload safety (2 tests)**

| # | Test | What it proves |
|---|---|---|
| 22 | `detectsImageTypesFromMagicBytes` | JPG, PNG, GIF, WEBP recognised from their first bytes |
| 23 | `rejectsNonImages` | A PHP script disguised as an image, and empty data, are rejected |

### 14.4 Manual functional tests that were performed

| # | What we did | Expected / observed result |
|---|---|---|
| 1 | Register a new user | Account created, redirected to login with email pre-filled ✔ |
| 2 | Register with a short name, existing email, short password | Three field-level error messages, form keeps typed values ✔ |
| 3 | Log in | Redirect to dashboard, "Welcome back" message ✔ |
| 4 | Submit a form without the CSRF token | Rejected (redirect with "session expired" message), nothing changed ✔ |
| 5 | Report a LOST phone (no photo) | Saved in `items`, redirected to its detail page ✔ |
| 6 | Report a FOUND phone with a real PNG photo | Saved, photo stored and displayed ✔ |
| 7 | Upload a fake image (text file named .png) | Rejected: "Only JPG, PNG, GIF or WEBP images are allowed" ✔ |
| 8 | Report an unrelated FOUND item (water bottle) | Does **not** appear as a match for the phone ✔ |
| 9 | Open the lost phone's detail page | One match, **97% High**, breakdown 25/20/25/15/12 ✔ |
| 10 | Matches page from the finder's side | Same 97% match shown ✔ |
| 11 | Browse: all / lost only / keyword "bottle" | 3 / 1 / 1 results ✔ |
| 12 | My Reports | Shows the report and "1 match" ✔ |
| 13 | Open a photo URL | Served as `image/png` ✔ |
| 14 | Path traversal attempt `/uploads/..%2F..%2Fconf%2Ftomcat-users.xml` | Blocked (400) ✔ |
| 15 | Open `/items` without logging in | Redirect to login ✔ |
| 16 | User B tries to close user A's report | No change in the database ✔ |
| 17 | Check `users` table in MySQL | Passwords stored as `120000:…` hashes ✔ |
| 18 | Phone-size screen (375 px) | No horizontal scrolling; menu collapses to a button ✔ |
| 19 | Log out, then open `/dashboard` | Redirect to login ✔ |
| 20 | Cold start: `stop.cmd` then `run.cmd` | Tomcat 10.1.60 started, connected to `reclaim_db`, app loads ✔ |

Bugs found during manual testing and fixed: (a) "1match" missing a space — caused by a JSP whitespace setting; (b) "Â·" broken symbol in page titles — the shared JSP fragments were read with the wrong character encoding; (c) Tomcat's connector failing to start in a restricted environment — fixed with `CATALINA_OPTS`.

### 14.5 What is NOT tested yet (be honest)

- No automated tests for DAOs (database), servlets or JSP pages — the manual tests above cover them. Integration tests are planned.
- The manual test steps are not saved as scripts in the repository.
- No load/performance testing, no user study yet.

---

## 15. How to demonstrate ReClaim to the teacher

**Before:** run `scripts\run.cmd`, open `http://localhost:8080/reclaim/`, keep phpMyAdmin (`http://localhost/phpmyadmin`) open in another tab.

**Demo accounts** (password `Demo@2026` for all — these are fictional demo data, say so if asked):
`dev.demo@example.com`, `mallika.demo@example.com`, `security.demo@example.com`

**5-minute demo:**
1. **Landing page** — explain the idea. (The "92%" card there is labelled *Example*; it is an illustration, not real data.)
2. **Register** — first show a validation error (e.g. passwords don't match), then register properly.
3. **Log in** → **Dashboard**.
4. **Report Lost**: "Samsung phone", Mobile Phone, Black, Library, yesterday, description "Black Samsung Galaxy phone with a transparent cover".
5. The detail page shows a **~100% match** with the phone in the demo data. Explain the five bars.
6. **Report Found**: "HP laptop charger", category Laptop / Tablet, Grey, Academic Block, today → matches the charger in the demo data.
7. Show **My Reports** (match counts, Resolved/Close buttons), **Browse** (filters, search) and **Matches** (expand "How is the score calculated?").
8. Show **phpMyAdmin → reclaim_db → users/items** — point out the password hash and the foreign key.
9. **Log out**, try `/dashboard` → redirected to login. Log in again.
10. Optionally run `scripts\build.cmd` to show **23 tests passing**.

---

## 16. Likely teacher questions, with answers

**Q: What is MVC in your project?**
Model = JavaBeans (`User`, `Item`, `Match`) + data layer; View = JSP pages; Controller = servlets. Example: `LoginServlet` (controller) uses `UserService` and `UserDAO` (model side) and forwards to `login.jsp` (view).

**Q: Why do you have a service layer and a DAO layer?**
Separation of concerns. DAO = only SQL. Service = only business rules. Servlet = only HTTP. Each part is easy to understand, test and change. `MatchScorer` has no database code, so it can be unit-tested.

**Q: What is a JavaBean?**
A class with private fields, a public no-argument constructor, and public getters/setters, usually `Serializable`. Our `User` is stored in the session, so it is `Serializable`.

**Q: How are passwords stored?**
Salted PBKDF2-SHA256 hash with 120,000 iterations; never the plain password (Part 8.6).

**Q: What is a PreparedStatement and why use it?**
A pre-compiled SQL statement with `?` placeholders; values are sent separately, so user input can never change the SQL — this prevents SQL injection.

**Q: How does session management work?**
Tomcat issues a session-ID cookie; we store the logged-in `User` in the session; `AuthFilter` checks it on every request; new session at login; invalidated at logout; 30-minute timeout (Part 8.5).

**Q: What is a filter? Name yours.**
Code that runs before servlets for every request. `SecurityHeadersFilter`, `AuthFilter`, `CsrfFilter`; their order is fixed in `web.xml`.

**Q: What is CSRF?**
A trick where another website makes your browser submit a form to our site while you're logged in. We block it with a secret token in every form that the other site cannot know.

**Q: Explain the matching algorithm.**
Weighted rule-based scoring of 5 attributes = 100 points; show pairs ≥ 40 (Part 10). Give the 97% example.

**Q: Is it AI / machine learning?**
No. It is deterministic and rule-based, which makes it transparent and explainable.

**Q: Why the overlap coefficient for description?**
The owner writes long descriptions, the finder short ones. Dividing by the smaller keyword set avoids penalising that difference.

**Q: Why are matches not stored in a table?**
They are computed fresh from current open reports, so they are never out of date. For a large system we would add a background job and a matches table.

**Q: Explain your database design / relationships.**
Two tables, `users` 1–N `items` via foreign key `user_id`, primary keys, unique email, ENUMs, indexes; user data not duplicated (JOIN when needed); least-privilege DB account.

**Q: Why Tomcat 10.1 and not the XAMPP Tomcat?**
Tomcat 10.1 supports the modern `jakarta.servlet` API; XAMPP's Tomcat 8.5 only supports old `javax.servlet` and is end-of-life.

**Q: Why MariaDB if the proposal says MySQL?**
MariaDB is MySQL-compatible (same SQL and driver) and comes with XAMPP. The code works on MySQL too.

**Q: How do photos work?**
Uploaded with `@MultipartConfig`, checked (size, real file type from first bytes), saved with Java I/O under a random UUID name outside the web folder, served by `ImageServlet`.

**Q: What happens if the database is down?**
The DAO throws `DataAccessException`; `web.xml` maps it to a friendly error page; the startup listener also logs a clear message.

**Q: How did you test it?**
23 JUnit unit tests (all pass) + a manual test checklist covering all user flows and security checks (Part 14).

**Q: What is left to do?**
Claims & ownership verification, notifications/real-time updates, admin dashboard, background matching with multithreading, logging, more tests, deployment (Part 18).


---

## 17. Honest limitations — what we must not claim

| Do NOT say | Say instead |
|---|---|
| "Users can claim items and we verify ownership" | "Claims and ownership verification are planned for the next phase" |
| "Users get notified of matches" / "real-time updates" | "Matches are shown when the user opens the dashboard, item page or matches page" |
| "There is an admin dashboard" | "The database already has a role column; the admin module is planned" |
| "AI-based / smart matching" | "Deterministic rule-based weighted matching" |
| "We use multithreading" | "Background (multithreaded) matching is planned" |
| "It is deployed online" | "It runs locally on Tomcat; deployment is planned" |
| "We use MySQL Server" | "MariaDB 10.4, which is MySQL-compatible" |
| "These are real reports from students" | "This is demo data created for the presentation" |
| "Fully tested" | "23 unit tests for the core logic + manual end-to-end tests; integration tests are planned" |
| "These are the official Graphic Era building names" | "These are general campus zones" |

Also: the "92% High" card on the landing page is a labelled **example illustration**; the demo accounts are **fictional**.

---

## 18. Future direction (Phase-III)

### 18.1 Planned modules, in suggested order

| # | Feature | What it will do | How we plan to build it (simple) |
|---|---|---|---|
| 1 | **Claims & ownership verification** (Module 4) | The owner of a lost item (or anyone who sees a found item) clicks "Claim", answers a verification question; the finder approves or rejects | New `claims` table; `Claim` JavaBean; `ClaimDAO`, `ClaimService`, `ClaimServlet`; new JSP pages; approved claim → item becomes RESOLVED |
| 2 | **Contact after approval** | Show contact details only after a claim is approved | Uses the existing `phone`/`email` columns; privacy kept until approval |
| 3 | **Notifications** | "You have a new match / a claim was approved" | `notifications` table + a badge in the navigation bar; optional email later |
| 4 | **Near-real-time updates** | Pages refresh match counts/status without reloading | JavaScript `fetch()` every 30 s to a small servlet that returns JSON (AJAX polling) |
| 5 | **Background matching (multithreading)** | Compute matches when a report is created, not when a page opens | `ExecutorService` thread pool started in `AppContextListener`; results saved in a `matches` table |
| 6 | **Admin dashboard** | Staff can see all reports, statistics, remove spam, block users | `role = 'ADMIN'` already exists; new `AdminFilter` + admin pages |
| 7 | **Logging / audit trail** (Java I/O) | Record important actions (login, report, claim) | Write to a log file or an `audit_log` table |
| 8 | **More testing** | Database and servlet integration tests | JUnit tests against a test database |
| 9 | **UI refinement** | Based on teacher/user feedback | CSS/JSP changes |
| 10 | **Deployment** | Make it accessible online | Tomcat on a server/VPS with MySQL; HTTPS |

### 18.2 Planned `claims` table (draft design)

| Column | Type | Meaning |
|---|---|---|
| `id` | INT PK | Claim number |
| `item_id` | INT FK → items.id | The found (or lost) report being claimed |
| `claimant_id` | INT FK → users.id | Who is claiming |
| `message` | VARCHAR(500) | Why they think it's theirs |
| `verification_answer` | VARCHAR(255) | Answer to a detail only the owner would know |
| `status` | ENUM('PENDING','APPROVED','REJECTED') | Decision |
| `created_at`, `decided_at` | TIMESTAMP | Dates |

**Claim flow:** Claimant submits claim → finder sees it (PENDING) → finder compares the hidden detail → APPROVE (item → RESOLVED, contact details shared) or REJECT.

### 18.3 Suggested work split (to be agreed by the team)

| Area | Can be owned by |
|---|---|
| Claims module (DB table, DAO, service, servlet, JSP) | _to be decided_ |
| Notifications + AJAX polling | _to be decided_ |
| Admin module | _to be decided_ |
| Testing (integration tests, manual test scripts) + documentation | _to be decided_ |

Recommended way of working: each member works on a **Git branch**, opens a pull request, another member reviews, then merge. This also makes individual contributions visible in the Git history (the teacher will inspect commits).

### 18.4 Immediate to-do list

- [ ] Create the GitHub repository and push (do **not** push `db.properties`).
- [ ] Commit the Phase-II documents in `docs/`.
- [ ] Each member sets up the project on their own computer (Part 13) and runs the demo once.
- [ ] Fill the placeholders in the PPT: mentor, semester, section, domain, PBL subject names, individual contributions.
- [ ] Agree the Phase-III work split.

---

## 19. Ready-made material for reports and PPTs

### 19.1 Key numbers

| Item | Value |
|---|---|
| Modules complete | 3 (Authentication, Reporting, Browse + Matching) |
| Java classes | 42 in 8 packages |
| Servlets (controllers) | 13 |
| Filters | 3 |
| JSP pages / fragments / custom tags | 13 / 3 / 5 |
| Database tables | 2 (`users`, `items`) |
| Automated tests | 23, all passing (5 test classes) |
| Manual test cases | 20 (Part 14.4) |
| Matching attributes | 5 (25 + 25 + 20 + 15 + 15 = 100) |
| Match threshold | 40 (High ≥ 75, Medium 55–74, Low 40–54) |
| Categories / colours / locations | 13 / 16 / 12 |
| Password hashing | PBKDF2-SHA256, 120,000 iterations, 16-byte salt |
| Session timeout | 30 minutes |
| Photo limit | 5 MB, JPG/PNG/GIF/WEBP |

### 19.2 One-line descriptions (copy-ready)

- **ReClaim** is a Java web application that centralizes lost-and-found reporting on the Graphic Era campus and automatically suggests matches between lost and found reports.
- **Architecture:** layered MVC — JSP views, Jakarta Servlet controllers, a service layer, DAOs using JDBC, and a MariaDB database, with JavaBeans as the model.
- **Matching:** deterministic rule-based scoring of category, location, colour, date and description similarity, out of 100.
- **Security:** salted PBKDF2 passwords, prepared statements, output escaping, CSRF tokens, session-fixation protection, owner-only updates and safe image uploads.
- **Testing:** 23 automated JUnit tests for the core logic plus a 20-step manual functional test of all flows.

### 19.3 Suggested PPT slide list (if you need a longer deck)

1. Title — ReClaim, Team Vision
2. Problem & motivation
3. Objectives & Phase-II status
4. Solution overview (what a user can do)
5. Architecture diagram (layers)
6. Database design (2 tables, relationship)
7. Module 1 — Authentication (flow + security)
8. Module 2 — Reporting (form, validation, photo upload, status)
9. Module 3 — Matching algorithm (table of 5 rules + worked example)
10. Screenshots (dashboard, report form, match card)
11. Testing (23 unit tests + manual tests)
12. Technology stack & methodology
13. Changes from proposal + limitations
14. Phase-III roadmap (claims, notifications, admin, deployment)
15. Team contribution + Thank you

### 19.4 Existing documents in `docs/`

| File | What it is |
|---|---|
| `ReClaim_New.pdf / .docx` | Phase-I proposal |
| `ReClaim_Phase_II_Progress_Report.pdf / .docx` | Phase-II report (same format as the proposal) |
| `ReClaim_Phase_II_Presentation.pptx` | 10-slide Phase-II presentation (some placeholders to fill) |
| `ReClaim_Team_Guide.md` | This guide |

---

## 20. Glossary

| Term | Simple meaning |
|---|---|
| **Servlet** | A Java class that handles a web request and produces a response |
| **JSP** | An HTML page with special tags that the server fills with data |
| **JSTL / EL** | Ready-made JSP tags (`<c:if>`, `<c:forEach>`) and the `${...}` expressions |
| **Tag file** | Our own reusable JSP component (e.g. `<ui:matchCard>`) |
| **Filter** | Code that runs before every servlet (security checks) |
| **Listener** | Code that runs when the application starts/stops |
| **MVC** | Model–View–Controller: data, screens and request-handling kept separate |
| **JavaBean** | Simple class with private fields + getters/setters |
| **DAO** | Data Access Object: the class that contains the SQL |
| **JDBC** | Java's standard API for databases |
| **PreparedStatement** | SQL with `?` placeholders — safe from SQL injection |
| **Session** | Server-side memory for one logged-in user, linked by a cookie |
| **Cookie** | Small piece of data the browser stores and sends back |
| **CSRF token** | Secret value in each form proving the form came from our site |
| **Hash / salt** | One-way scrambled password / random value added before hashing |
| **PBKDF2** | A slow, standard password-hashing method |
| **Enum** | A fixed list of allowed values in Java |
| **WAR** | The packaged web application file Tomcat runs |
| **Maven** | Tool that builds the project and downloads libraries |
| **Tomcat** | The server that runs the WAR |
| **MariaDB** | MySQL-compatible database |
| **Primary key / Foreign key** | Unique ID of a row / a column pointing to another table's ID |
| **Index** | Database structure that makes searching faster |
| **PRG (Post/Redirect/Get)** | After a form submit, redirect so refresh doesn't resubmit |
| **Flash message** | A message shown once on the next page |
| **Overlap coefficient** | Common words ÷ size of the smaller word set |
| **Unit test** | Automated test of one small piece of code |
| **Deterministic** | Same input always gives the same output |
| **XSS** | Attack that injects scripts into pages — prevented by escaping output |
| **SQL injection** | Attack that injects SQL through inputs — prevented by PreparedStatement |
| **UUID** | A random unique identifier, used for photo file names |
