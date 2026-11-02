# LinkedIn Automated UI Test Suite
**Module**: IT3153 – Software Quality Assurance  
**Assignment**: 04 – Automated UI Testing with Selenium & TestNG  
**Institution**: General Sir John Kotelawala Defence University (KDU), Faculty of Computing  

---

## 1. Overview
This project provides a robust, enterprise-grade automated UI test suite in Java 17 for exercising three core end-to-end user flows on **LinkedIn**:
1. **User Authentication**: Positive login with valid credentials and negative login with invalid password.
2. **Search Automation**: Executing a global keyword search (e.g., `"Software Testing"`) and validating the search results page.
3. **Search Result Interaction**: Selecting the top search result, clicking through, and asserting the target profile/company detail page.

The automation framework strictly adopts the **Page Object Model (POM)** design pattern, explicit dynamic waits (`WebDriverWait` / `ExpectedConditions`), automated failure screenshot capture via TestNG listeners, and an anti-bot resilient browser configuration.

---

## 2. Technology Stack & Tools
- **Programming Language**: Java 17 (OpenJDK 17)
- **Automation Framework**: Selenium WebDriver 4.29.0
- **Test Runner & Assertions**: TestNG 7.10.2
- **Driver Management**: WebDriverManager 5.9.2 / Selenium Manager
- **Build & Dependency Tool**: Apache Maven 3.9+
- **Logging**: Apache Log4j2
- **File & Screenshot Handling**: Apache Commons IO

---

## 3. Project Directory Structure
```
sqa assignment/
├── pom.xml                                   # Maven dependencies and Surefire configuration
├── testng.xml                                # TestNG test suite runner configuration
├── run_tests.bat                             # One-click execution script for Windows
├── README.md                                 # Configuration & execution guide
├── .gitignore                                # Git ignore file
│
├── src/
│   ├── main/
│   │   ├── java/com/kdu/sqa/
│   │   │   ├── base/
│   │   │   │   └── BasePage.java             # Common reusable web interactions & explicit waits
│   │   │   ├── pages/
│   │   │   │   ├── LoginPage.java            # POM for LinkedIn Login & checkpoint handler
│   │   │   │   ├── FeedPage.java             # POM for LinkedIn Feed & Search trigger
│   │   │   │   ├── SearchResultsPage.java    # POM for Search Results listing
│   │   │   │   └── DetailPage.java           # POM for Profile / Company Detail view
│   │   │   └── utils/
│   │   │       ├── ConfigReader.java         # Centralized properties loader with CLI overrides
│   │   │       ├── WaitUtils.java            # Explicit wait utility methods (WebDriverWait)
│   │   │       └── ScreenshotUtils.java      # Timestamped screenshot capture utility
│   │   └── resources/
│   │       ├── config.properties             # Environment & credentials configuration
│   │       └── log4j2.xml                    # Logging framework configuration
│   │
│   └── test/
│       ├── java/com/kdu/sqa/
│       │   ├── base/
│       │   │   └── BaseTest.java             # Test lifecycle setup (@BeforeMethod/@AfterMethod)
│       │   ├── listeners/
│       │   │   └── TestListener.java         # TestNG listener for logging & auto-screenshots
│       │   └── tests/
│       │       ├── LoginTest.java            # Positive & negative login tests (Task 2)
│       │       ├── SearchTest.java           # Search flow tests (Task 3)
│       │       └── ClickSearchResultTest.java# Result click & verification tests (Task 4)
│       └── resources/
│           └── testng.xml                    # Resource test suite definition
│
└── reports/
    ├── SQA_Assignment_4_Test_Report.pdf      # Complete PDF test report (Deliverable 3)
    ├── SQA_Assignment_4_Test_Report.md       # Markdown version of test report
    ├── SQA_Assignment_4_Test_Report.html     # Styled printable HTML version of report
    ├── screenshots/                          # Automatically captured test failure screenshots
    └── logs/
        └── automation.log                    # Execution logs
```

---

## 4. How to Configure Credentials

As required by assignment ethics guidelines, **never commit real credentials to public repositories**. You have two secure options to supply your LinkedIn test credentials:

### Option A: Edit `src/main/resources/config.properties`
Open [config.properties](file:///c:/Users/Adithya%20Himansi/Desktop/sqa%20assignment/src/main/resources/config.properties) and update the credentials:
```properties
valid.username=your_dedicated_account@example.com
valid.password=YourActualPassword123!

invalid.password=IncorrectPassword999!
search.term=Software Testing
browser=chrome
headless=false
```

### Option B: Pass via Command Line (Recommended for CI / Privacy)
You can supply credentials directly when invoking Maven without altering source files:
```powershell
mvn test -Dvalid.username="your_email@example.com" -Dvalid.password="your_password"
```

---

## 5. How to Run the Tests

### Option 1: Using the Windows Batch Script (Fastest)
Double-click `run_tests.bat` or execute in PowerShell/Command Prompt:
```powershell
.\run_tests.bat
```

### Option 2: Using Maven Command Line
Run the entire TestNG suite:
```powershell
mvn clean test
```

To run a specific test class:
```powershell
# Run only Login tests
mvn test -Dtest=LoginTest

# Run only Search test
mvn test -Dtest=SearchTest

# Run only Click Result test
mvn test -Dtest=ClickSearchResultTest
```

### Option 3: From IDE (IntelliJ IDEA / Eclipse / VS Code)
1. Open this directory as a Maven project in your IDE.
2. Right-click [testng.xml](file:///c:/Users/Adithya%20Himansi/Desktop/sqa%20assignment/testng.xml) or any class in `src/test/java/com/kdu/sqa/tests/`.
3. Select **Run 'testng.xml'** or **Run 'LoginTest'**.

---

## 6. Anti-Bot and Security Checkpoint Handling (Assignment Rule 2)

LinkedIn incorporates bot-detection mechanisms that occasionally present a verification puzzle, PIN code, or CAPTCHA challenge.
- **Automated Anti-Detection Settings**:
  - The framework launches Chrome with flags `--disable-blink-features=AutomationControlled` and removes the `enable-automation` switch to present a standard browser fingerprint.
- **Manual Intervention Pause**:
  - Per Assignment Rule 2 (*"If LinkedIn presents a CAPTCHA or verification step, do not attempt to bypass it. Document it in your report and, if needed, pause the test and complete the step manually"*), `LoginPage.handleSecurityCheckpointIfPresent()` automatically detects checkpoint dialogs and pauses execution for `checkpoint.timeout=20` seconds, allowing the tester to complete the challenge manually in the open browser window.

---

## 7. Deliverables Checklist
- [x] **1. Complete project source code**: Organized with clean Page Object Model (POM), explicit waits, and TestNG test runner.
- [x] **2. README.md**: Complete documentation for installation, configuration, and execution.
- [x] **3. Test Report**: Available as [SQA_Assignment_4_Test_Report.pdf](file:///c:/Users/Adithya%20Himansi/Desktop/sqa%20assignment/reports/SQA_Assignment_4_Test_Report.pdf) and HTML in the `reports/` folder.
- [x] **4. Screen Recording**: Instructions provided in the report for creating a <3 minute video demonstration with credentials hidden.
