@echo off
rem Compiles, runs the unit tests and packages target\reclaim.war
call "%~dp0config.cmd"
pushd "%RECLAIM_ROOT%"
call "%MVN%" -B clean package
set "RESULT=%ERRORLEVEL%"
popd
exit /b %RESULT%
