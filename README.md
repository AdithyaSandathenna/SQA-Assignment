# LinkedIn Automated UI Test Suite
**Module**: IT3153 – Software Quality Assurance  
**Assignment**: 04 – Automated UI Testing with Selenium & TestNG  
**Institution**: General Sir John Kotelawala Defence University (KDU), Faculty of Computing  

---

## 1. Overview
This project provides a clean, robust, and maintainable automated UI test suite in Java 17 for exercising three core end-to-end user flows on **LinkedIn**:
1. **User Authentication**: Login page validation, positive login with valid credentials, and negative login with invalid password feedback.
2. **Search Automation**: Executing a global keyword search (`"Software Testing"`) and validating the search results URL and visible results.
3. **Search Result Interaction**: Selecting the top search result, clicking through, and asserting the target profile/company detail page navigation.

The automation framework strictly adopts the **Page Object Model (POM)** design pattern, explicit dynamic waits (`WebDriverWait` / `ExpectedConditions`), automated failure screenshot capture via `TakesScreenshot`, and an anti-bot resilient browser configuration.

---

## 2. Technology Stack & Tools
- **Programming Language**: Java 17 (OpenJDK 17)
- **Automation Framework**: Selenium WebDriver 4.29.0
- **Test Runner & Assertions**: TestNG 7.10.2
- **Driver Management**: WebDriverManager 5.9.2
- **Build & Dependency Tool**: Apache Maven 3.9+
- **Screenshot Handling**: Apache Commons IO 2.18.0

---

## 3. Project Directory Structure
```
LinkedInAutomation/
│
├── pom.xml                                   # Maven dependencies and Surefire configuration
├── README.md                                 # Configuration & execution guide
├── testng.xml                                # TestNG test suite runner configuration
├── run_tests.bat                             # One-click execution script for Windows
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── pages/
│   │   │       ├── LoginPage.java            # POM for LinkedIn Login & checkpoint handler
│   │   │       ├── HomePage.java             # POM for LinkedIn Feed & Search trigger
│   │   │       └── SearchPage.java           # POM for Search Results & detail navigation
│   │   │
│   │   └── resources/
│   │       └── config.properties             # Test account credentials and settings
│   │
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java             # Driver lifecycle setup & screenshot handling
│           │
│           └── tests/
│               ├── LoginTest.java            # TC-01 to TC-06: Login & field validation tests
│               ├── SearchTest.java           # TC-07: Search "Software Testing" test
│               └── SearchResultTest.java     # TC-08: Click first search result test
│
├── screenshots/                              # Destination folder for failure screenshots
│
└── reports/
    ├── SQA_Assignment_4_Test_Report.pdf      # Complete 3-page academic PDF report
    ├── SQA_Assignment_4_Test_Report.html     # HTML source for PDF generation
    └── assets/                               # KDU logo & terminal execution figures
```

---

## 4. Test Configuration (`config.properties`)
The test credentials and environment settings are configured in `src/main/resources/config.properties`:
```properties
# Browser settings
browser=chrome
headless=false

# Target URL
baseUrl=https://www.linkedin.com/login

# Test Credentials
valid.username=adithyasandathenna@gmail.com
valid.password=2003Linkedin*bh

invalid.username=adi@gmail.com
invalid.password=56565678

# Search Parameter
search.term=Software Testing
```

You can also override credentials via Maven CLI parameters:
```bash
mvn test -Dvalid.username=your_user@email.com -Dvalid.password=your_password
```

---

## 5. Execution Instructions

### Option A: Using Windows Batch Script (Recommended)
Double-click or run from command line:
```cmd
run_tests.bat
```

### Option B: Using Maven CLI
Execute the entire 8-test suite:
```cmd
mvn test
```

Execute a specific test class:
```cmd
mvn test -Dtest=LoginTest
mvn test -Dtest=SearchTest
mvn test -Dtest=SearchResultTest
```

---

## 6. Test Suite Coverage & Results Matrix

| ID | Test Case | Target Page / Component | Expected Result | Status |
| :--- | :--- | :--- | :--- | :---: |
| **TC-01** | Open LinkedIn login page | `LoginPage` | Login page loads with valid URL. | **Pass** |
| **TC-02** | Verify login page title | `LoginPage` | Page title contains "LinkedIn". | **Pass** |
| **TC-03** | Verify login form fields | `LoginPage` | Email, password, and Sign in button visible. | **Pass** |
| **TC-04** | Login page opens in LoginTest | `LoginPage` | Login page is displayed and interactable. | **Pass** |
| **TC-05** | Valid login | `LoginPage` &rarr; `HomePage` | Successfully authenticates; feed loads. | **Pass** |
| **TC-06** | Invalid-password login | `LoginPage` | Invalid-credential error feedback displayed. | **Pass** |
| **TC-07** | Search "Software Testing" | `HomePage` &rarr; `SearchPage` | `/search/results` URL and results visible. | **Pass** |
| **TC-08** | Click first search result | `SearchPage` | Navigates to target profile/company detail. | **Pass** |

**Summary: 8 Tests Run | 0 Failures | 0 Errors | 0 Skipped (100% Pass Rate)**

---

## 7. Deliverables
1. **Source Code**: Clean Page Object Model structure in `src/`.
2. **README.md**: Setup, configuration, and execution instructions.
3. **Test Report**: [SQA_Assignment_4_Test_Report.pdf](file:///reports/SQA_Assignment_4_Test_Report.pdf) (exact 3 pages matching university rubric).
