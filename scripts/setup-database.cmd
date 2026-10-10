@echo off
rem ---------------------------------------------------------------------
rem  Creates the reclaim_db database and its tables (safe to re-run).
rem  Usage:  scripts\setup-database.cmd            (creates schema only)
rem          scripts\setup-database.cmd sample     (also loads demo data)
rem  Uses the MySQL root account (XAMPP default: no password).
rem ---------------------------------------------------------------------
call "%~dp0config.cmd"
"%MYSQL_BIN%\mysql.exe" -u root < "%RECLAIM_ROOT%\database\schema.sql"
if errorlevel 1 (
    echo Could not create the schema. Is MySQL running? Does root need a password? Then add -p.
    exit /b 1
)
echo Schema ready.
if /i "%~1"=="sample" (
    "%MYSQL_BIN%\mysql.exe" -u root < "%RECLAIM_ROOT%\database\sample_data.sql"
    echo Sample data loaded.
)
