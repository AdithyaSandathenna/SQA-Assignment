@echo off
setlocal

echo =====================================================================
echo  LinkedIn UI Automation Test Suite - KDU IT3153 SQA Assignment 04
echo =====================================================================

REM Ensure Maven is in PATH
set "MAVEN_HOME=C:\Users\Adithya Himansi\maven\apache-maven-3.9.9"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

echo Checking Java and Maven...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed or not in PATH!
    pause
    exit /b %ERRORLEVEL%
)

mvn -v
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven not found!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Starting LinkedIn Test Suite execution...
echo.

call mvn test -DsuiteXmlFile=src/test/resources/testng.xml %*

echo.
echo =====================================================================
echo  Test Run Completed.
echo  - TestNG HTML Report: target/surefire-reports/index.html
echo  - Screenshots (if any): reports/screenshots/
echo  - Log File: reports/logs/automation.log
echo =====================================================================
pause
