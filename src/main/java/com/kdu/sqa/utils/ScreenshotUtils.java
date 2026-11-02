package com.kdu.sqa.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for capturing screenshots automatically on test failure or manual checkpoints.
 * Addresses Assignment requirement: "Screenshots of the test run results and at least one screenshot captured automatically by your code".
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            logger.warn("Driver is null. Cannot capture screenshot for {}", screenshotName);
            return null;
        }

        try {
            File dir = new File(SCREENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String sanitizedName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = sanitizedName + "_" + timestamp + ".png";
            File destinationFile = new File(dir, fileName);

            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, destinationFile);

            String absolutePath = destinationFile.getAbsolutePath();
            logger.info("Screenshot successfully captured: {}", absolutePath);
            return absolutePath;
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error capturing screenshot: {}", e.getMessage(), e);
            return null;
        }
    }
}
