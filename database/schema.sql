-- =====================================================================
--  ReClaim - Campus Lost & Found System
--  Database schema (MySQL 8 / MariaDB 10.4+)
--
--  Safe to re-run: it never drops anything. Tables are created only if
--  they do not already exist.
--
--  Run as an admin user, e.g. with XAMPP:
--      C:\xampp\mysql\bin\mysql.exe -u root < database\schema.sql
-- =====================================================================

CREATE DATABASE IF NOT EXISTS reclaim_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE reclaim_db;

-- ---------------------------------------------------------------------
--  users : everyone who can log in (students, faculty, staff).
--  Passwords are never stored in plain text: password_hash holds a
--  salted PBKDF2-SHA256 hash produced by com.reclaim.util.PasswordUtil.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id              INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    full_name       VARCHAR(100)  NOT NULL,
    email           VARCHAR(150)  NOT NULL,
    password_hash   VARCHAR(255)  NOT NULL,
    user_type       ENUM('STUDENT','FACULTY','STAFF') NOT NULL DEFAULT 'STUDENT',
    university_id   VARCHAR(20)   NULL COMMENT 'Student ID / Employee ID',
    department      VARCHAR(100)  NULL,
    phone           VARCHAR(15)   NULL,
    role            ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER' COMMENT 'ADMIN reserved for the admin module',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at   TIMESTAMP     NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
--  items : one row per LOST or FOUND report.
--  category / colour / location hold the enum constant names from the
--  Java enums com.reclaim.model.Category / Colour / CampusLocation.
--  These five attributes (category, colour, location, incident_date,
--  title+description) drive the matching algorithm.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS items (
    id                INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    user_id           INT UNSIGNED  NOT NULL,
    type              ENUM('LOST','FOUND') NOT NULL,
    title             VARCHAR(100)  NOT NULL,
    category          VARCHAR(40)   NOT NULL,
    description       VARCHAR(1000) NOT NULL,
    colour            VARCHAR(30)   NOT NULL,
    location          VARCHAR(40)   NOT NULL,
    location_details  VARCHAR(150)  NULL,
    incident_date     DATE          NOT NULL COMMENT 'Date the item was lost / found',
    image_path        VARCHAR(255)  NULL COMMENT 'Stored file name of the uploaded photo',
    status            ENUM('OPEN','RESOLVED','CLOSED') NOT NULL DEFAULT 'OPEN',
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_items_type_status (type, status),
    KEY idx_items_user (user_id),
    KEY idx_items_category (category),
    CONSTRAINT fk_items_user FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
