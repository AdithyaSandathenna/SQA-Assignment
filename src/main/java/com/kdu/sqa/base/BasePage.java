package com.kdu.sqa.base;

import com.kdu.sqa.utils.ConfigReader;
import com.kdu.sqa.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * BasePage encapsulates common WebDriver operations and provides explicit wait access.
 * Follows Page Object Model (POM) architectural best practices.
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WaitUtils waitUtils;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int explicitWaitSeconds = ConfigReader.getIntProperty("explicit.wait", 15);
        this.waitUtils = new WaitUtils(driver, explicitWaitSeconds);
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    protected WebElement findVisibleElement(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement el : elements) {
            try {
                if (el.isDisplayed()) {
                    return el;
                }
            } catch (Exception ignored) {}
        }
        return waitUtils.waitForVisibility(locator);
    }

    protected void click(By locator) {
        logger.info("Clicking element: {}", locator);
        WebElement element = findVisibleElement(locator);
        element.click();
    }

    protected void enterText(By locator, String text) {
        logger.info("Entering text into element: {}", locator);
        WebElement element = findVisibleElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getElementText(By locator) {
        WebElement element = waitUtils.waitForVisibility(locator);
        return element.getText().trim();
    }

    protected boolean isElementVisible(By locator) {
        try {
            return waitUtils.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void scrollIntoView(By locator) {
        WebElement element = waitUtils.waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
