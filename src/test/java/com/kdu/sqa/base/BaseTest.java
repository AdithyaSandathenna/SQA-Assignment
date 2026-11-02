package com.kdu.sqa.base;

import com.kdu.sqa.utils.ConfigReader;
import com.kdu.sqa.utils.ScreenshotUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.Collections;

/**
 * Base test class managing WebDriver lifecycle and browser configuration.
 * Satisfies Task 1:
 * - Create a java project with required dependencies
 * - Configure the WebDriver (Selenium Manager / WebDriverManager)
 * - Create a base test class that starts the browser before each test and closes it afterwards.
 */
public abstract class BaseTest {

    // ThreadLocal ensures thread-safety if tests are executed in parallel
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBooleanProperty("headless", false);
        logger.info("Initializing WebDriver for browser: {} (headless={})", browser, headless);

        WebDriver driver;
        if ("firefox".equals(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            driver = new FirefoxDriver(options);
        } else {
            // Default to Google Chrome
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            
            // Standard options for stability and realistic browser fingerprint
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension", false);
            
            if (headless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            driver = new ChromeDriver(options);
        }

        int implicitWait = ConfigReader.getIntProperty("implicit.wait", 10);
        int pageLoadTimeout = ConfigReader.getIntProperty("page.load.timeout", 30);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        logger.info("Browser initialized successfully.");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();
        if (driver != null) {
            // Automatic screenshot capture if the test failed and listener did not already capture it
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.warn("Test failed: {}. Capturing post-test teardown screenshot...", result.getName());
                ScreenshotUtils.captureScreenshot(driver, result.getName() + "_teardown_failure");
            }
            logger.info("Quitting WebDriver instance for test: {}", result.getName());
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error occurred while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
