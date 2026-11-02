package com.kdu.sqa.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class providing explicit wait mechanisms.
 * Addresses Assignment Rubric requirement: "Try to include wait() -> Search, Learn and Implement".
 */
public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait defaultWait;

    public WaitUtils(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.defaultWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        this.defaultWait.ignoring(StaleElementReferenceException.class);
    }

    /**
     * Waits until the element is visible on the DOM and displayed.
     */
    public WebElement waitForVisibility(By locator) {
        return defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is visible with a custom timeout.
     */
    public WebElement waitForVisibility(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is present in the DOM (not necessarily visible).
     */
    public WebElement waitForPresence(By locator) {
        return defaultWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until the element is both visible and clickable.
     */
    public WebElement waitForClickability(By locator) {
        return defaultWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until the current URL contains the expected partial URL.
     */
    public boolean waitForUrlContains(String partialUrl) {
        return defaultWait.until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Waits until the current URL contains the expected partial URL with custom timeout.
     */
    public boolean waitForUrlContains(String partialUrl, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.urlContains(partialUrl));
    }

    /**
     * Waits for all elements located by locator to be visible.
     */
    public List<WebElement> waitForAllVisible(By locator) {
        return defaultWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Checks if an element is displayed within a short timeout without throwing an exception.
     */
    public boolean isElementDisplayed(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }
}
