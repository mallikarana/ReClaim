@echo off
rem ---------------------------------------------------------------------
rem  Shared settings for the ReClaim helper scripts.
rem  Edit these paths if your tools are installed somewhere else.
rem ---------------------------------------------------------------------

rem Project root (folder above this scripts folder)
for %%I in ("%~dp0..") do set "RECLAIM_ROOT=%%~fI"

rem Apache Tomcat 10.1 (portable copy inside the project, git-ignored)
set "CATALINA_HOME=%RECLAIM_ROOT%\.tools\apache-tomcat-10.1.60"
rem Keep the JDK's internal NIO socket file in Tomcat's temp folder. Without this,
rem restricted Windows temp folders can stop Tomcat's HTTP connector from starting.
set "CATALINA_OPTS=-Djdk.net.unixdomain.tmpdir=%CATALINA_HOME%\temp"

rem Maven: use "mvn" from PATH if available, otherwise IntelliJ's bundled Maven
set "MVN=mvn"
where mvn >nul 2>nul
if errorlevel 1 set "MVN=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2023.3.2\plugins\maven\lib\maven3\bin\mvn.cmd"

rem MySQL / MariaDB (XAMPP)
set "MYSQL_BIN=C:\xampp\mysql\bin"
