# ReClaim: Campus Lost & Found

A Java web application that helps students and staff of **Graphic Era University** recover lost
belongings. Users report lost or found items, browse all reports and get **automatically scored
matches** between lost and found reports.

MCA Major Project · Team Vision: Kartik Maithani (lead), Mallika Rana, Dev Parashar

**Stack:** Java · JSP · Jakarta Servlets · JSTL · JDBC · MySQL · Apache Tomcat 10.1 · MVC · JavaBeans · Maven

## Project Objective

The objective of ReClaim is to provide a centralized platform for reporting, searching, and recovering lost belongings within the university campus. The application uses rule-based matching to help users identify potentially related lost and found reports, making the recovery process more organized and efficient.

## Features (current milestone)
| Module | What works |
|---|---|
| 1. Authentication | Registration with validation, login/logout, session-based access control, profile editing, password change, "remember my email" cookie |
| 2. Reporting | Report LOST or FOUND items (category, colour, location, date, description, optional photo), My Reports, status tracking |
| 3. Browse + Matching | Keyword search and filters, rule-based matching with a score out of 100 and a per-attribute breakdown |

Security: salted PBKDF2 password hashing, prepared statements only, CSRF tokens on every form,
session-fixation protection, output escaping, photo type checked from file content, least-privilege DB user.

## Architecture
```
Browser ──► Filters (security headers → login check → CSRF)
        ──► Servlet controllers ──► Services (business rules, MatchScorer)
        ──► DAOs (JDBC, PreparedStatement) ──► MySQL
JSP views + JSTL + tag files render JavaBeans passed by the controllers.
```
See [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) for the package layout, conventions and the matching rules.

## Prerequisites
- JDK 17 or newer
- MySQL 8 or MariaDB 10.4+ (XAMPP works)
- Apache Tomcat 10.1 (the scripts expect a portable copy in `.tools/apache-tomcat-10.1.60`)
- Maven 3.9 (standalone or IntelliJ's bundled copy)

## Setup (first time)
1. Start MySQL (XAMPP Control Panel → MySQL → Start).
2. Create the schema and load the demo data:
   ```bash
   scripts\setup-database.cmd sample
   ```
3. Create the application's database account:
   - Open `database/create_app_user.sql` and replace both occurrences of `CHANGE_ME_PASSWORD` with a password of your choice.
   - Run the script as the MySQL root user.

4. Configure the application:
   - Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`.
   - Enter the same database username and password in `db.properties`.
   - Do not commit `db.properties`; it contains local credentials and is excluded by `.gitignore`.

## Run
```bash
scripts\run.cmd
```
Then open **http://localhost:8080/reclaim/**. Stop Tomcat with `scripts\stop.cmd`.

Run the unit tests only:
```bash
scripts\build.cmd
```

## Demo accounts (sample data)
Password for all: `Demo@2026`
- `dev.demo@example.com`: has a 97% match (lost wallet ↔ wallet found by Security)
- `mallika.demo@example.com`
- `security.demo@example.com`

## Suggested demo flow (about 5 minutes)
1. **Register** a new student account (show a validation error first, e.g. mismatched passwords).
2. **Log in** and land on the dashboard.
3. **Report Lost**: "Samsung phone", Mobile Phone, Black, Library, yesterday,
   "Black Samsung Galaxy phone with a transparent cover".
4. You're taken to the report page: it shows a **~100% match** with the phone Dev found, with the
   score breakdown for category, colour, location, date and description.
5. **Report Found**: "HP laptop charger", category *Laptop / Tablet*, Grey, Academic Block, today.
   It matches the charger Mallika lost.
6. Show **My Reports** (match counts, status buttons), **Browse** (filters and search) and **Matches**.
7. Show the rows in MySQL (phpMyAdmin → `reclaim_db` → `items`, `users`: passwords are hashed).
8. **Log out**, try opening `/dashboard` (you're redirected to login), then **log in** again.

## Project structure
```
database/            schema.sql, create_app_user.sql, sample_data.sql
scripts/             build / run / stop / setup-database helpers (Windows)
src/main/java/com/reclaim/
  controller/  service/  dao/  model/  filter/  listener/  util/  exception/
src/main/webapp/
  WEB-INF/web.xml  WEB-INF/views/*.jsp  WEB-INF/tags/*.tag
  assets/css  assets/js  assets/img
src/test/java/       JUnit 5 tests (matching, text similarity, passwords, validation, image detection)
```
