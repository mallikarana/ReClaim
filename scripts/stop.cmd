@echo off
rem Stops Tomcat. (MySQL is left running; stop it from the XAMPP control panel if needed.)
call "%~dp0config.cmd"
call "%CATALINA_HOME%\bin\shutdown.bat"
