@echo off
setlocal

echo =====================================================================
echo  LinkedIn UI Automation Test Suite - KDU IT3153 SQA Assignment
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

call mvn test %*

echo.
echo =====================================================================
echo  Test Run Completed.
echo  - TestNG HTML Report: target/surefire-reports/index.html
echo  - Screenshots (on failure): screenshots/
echo =====================================================================
