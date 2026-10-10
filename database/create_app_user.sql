-- =====================================================================
--  ReClaim - dedicated, least-privilege database account for the web app.
--
--  The application never connects as root. It only needs to read and
--  write rows in reclaim_db, so that is all this account is granted.
--
--  1. Replace CHANGE_ME_PASSWORD below with a password of your choice
--     (do NOT commit the real password).
--  2. Run as an admin user:
--        C:\xampp\mysql\bin\mysql.exe -u root < database\create_app_user.sql
--  3. Put the same password in src/main/resources/db.properties
-- =====================================================================

CREATE USER IF NOT EXISTS 'reclaim_app'@'localhost' IDENTIFIED BY 'CHANGE_ME_PASSWORD';
CREATE USER IF NOT EXISTS 'reclaim_app'@'127.0.0.1' IDENTIFIED BY 'CHANGE_ME_PASSWORD';

GRANT SELECT, INSERT, UPDATE, DELETE ON reclaim_db.* TO 'reclaim_app'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON reclaim_db.* TO 'reclaim_app'@'127.0.0.1';

FLUSH PRIVILEGES;
