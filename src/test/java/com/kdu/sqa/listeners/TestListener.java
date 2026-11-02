package com.kdu.sqa.listeners;

import com.kdu.sqa.base.BaseTest;
import com.kdu.sqa.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener providing automatic screenshot capture on test failure,
 * along with lifecycle logging.
 * Satisfies Task 6 requirement:
 * "Screenshots of the test run results and at least one screenshot captured automatically by your code (for example, on failure)."
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("================================================================================");
        logger.info("TEST SUITE STARTED: {}", context.getName());
        logger.info("================================================================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("================================================================================");
        logger.info("TEST SUITE FINISHED: {}", context.getName());
        logger.info("Passed: {}, Failed: {}, Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        logger.info("================================================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("--> STARTING TEST: {}.{}()", result.getTestClass().getName(), result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("--> TEST PASSED: {}.{}()", result.getTestClass().getName(), result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("--> TEST FAILED: {}.{}()", result.getTestClass().getName(), result.getName());
        logger.error("Failure Reason: {}", result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");

        WebDriver driver = BaseTest.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName() + "_FAILURE");
            logger.info("Failure screenshot captured at: {}", screenshotPath);
        } else {
            logger.warn("WebDriver instance not found. Screenshot could not be taken.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("--> TEST SKIPPED: {}.{}()", result.getTestClass().getName(), result.getName());
    }
}
