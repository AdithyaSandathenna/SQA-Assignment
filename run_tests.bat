@echo off
setlocal

echo =====================================================================
echo  LinkedIn UI Automation Test Suite - KDU IT3153 SQA Assignment
echo =====================================================================

REM Ensure Maven is in PATH
set "MAVEN_HOME=C:\Users\Adithya Himansi\maven\apache-maven-3.9.9"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

echo.
echo Starting LinkedIn Test Suite execution...
echo.

call mvn test %*

echo.
echo =====================================================================
echo  Test Run Completed.
echo  - TestNG HTML Report: target/surefire-reports/index.html
echo  - Screenshots (on failure): screenshots/
echo =====================================================================
echo.
pause
