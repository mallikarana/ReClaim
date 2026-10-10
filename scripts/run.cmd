@echo off
rem ---------------------------------------------------------------------
rem  One-click demo start:
rem    1. starts MySQL (XAMPP) if it is not already running
rem    2. builds the WAR with Maven
rem    3. deploys it to Tomcat and starts Tomcat
rem  Then open http://localhost:8080/reclaim/
rem ---------------------------------------------------------------------
setlocal
call "%~dp0config.cmd"
rem Full path so the Windows tool is used even if Git's Unix "find" is on PATH
set "FIND=%SystemRoot%\System32\find.exe"

echo [1/3] Checking MySQL on port 3306...
netstat -an | "%FIND%" ":3306" | "%FIND%" "LISTENING" >nul
if errorlevel 1 (
    echo       Starting XAMPP MySQL...
    start "ReClaim MySQL" /min "%MYSQL_BIN%\mysqld.exe" --defaults-file="%MYSQL_BIN%\my.ini" --standalone
    timeout /t 5 /nobreak >nul
) else (
    echo       MySQL is already running.
)

echo [2/3] Building ReClaim...
pushd "%RECLAIM_ROOT%"
call "%MVN%" -B -q clean package
if errorlevel 1 (
    popd
    echo BUILD FAILED - see the messages above.
    exit /b 1
)
popd

echo [3/3] Deploying to Tomcat...
copy /y "%RECLAIM_ROOT%\target\reclaim.war" "%CATALINA_HOME%\webapps\reclaim.war" >nul

netstat -an | "%FIND%" ":8080" | "%FIND%" "LISTENING" >nul
if errorlevel 1 (
    call "%CATALINA_HOME%\bin\startup.bat"
) else (
    echo       Tomcat is already running - it will redeploy the new WAR automatically.
)

echo.
echo ReClaim will be available in a few seconds at: http://localhost:8080/reclaim/
endlocal
