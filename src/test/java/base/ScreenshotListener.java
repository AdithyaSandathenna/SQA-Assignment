package base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener for automated Failure Screenshot Handling.
 * Captures a browser screenshot upon test failure and saves it to the screenshots/ directory.
 */
public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();

        if (!(testInstance instanceof BaseTest)) {
            System.out.println("[WARN] Screenshot skipped: test class does not extend BaseTest.");
            return;
        }

        BaseTest baseTest = (BaseTest) testInstance;
        WebDriver driver = baseTest.getDriver();

        if (driver == null) {
            System.out.println("[WARN] Screenshot skipped: WebDriver instance is null.");
            return;
        }

        try {
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String testName = result.getMethod().getMethodName();
            File destination = new File(screenshotsDir, testName + "_FAILURE_" + timestamp + ".png");

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);

            System.out.println("[INFO] Failure screenshot captured by ScreenshotListener: " + destination.getAbsolutePath());
        } catch (IOException | RuntimeException e) {
            System.err.println("[ERROR] ScreenshotListener could not capture failure screenshot: " + e.getMessage());
        }
    }
}

