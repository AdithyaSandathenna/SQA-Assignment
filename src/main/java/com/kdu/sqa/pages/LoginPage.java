package com.kdu.sqa.pages;

import com.kdu.sqa.base.BasePage;
import com.kdu.sqa.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for LinkedIn Login Page (https://www.linkedin.com/login).
 * Supports Task 2: Positive and Negative Login Automation, plus Security Checkpoint handling.
 */
public class LoginPage extends BasePage {

    // Resilient composite locators matching standard and updated LinkedIn login DOM
    private final By usernameInput = By.cssSelector("input[type='email'], input#username, input[name='session_key'], input[autocomplete='username']");
    private final By passwordInput = By.cssSelector("input[type='password'], input#password, input[name='session_password']");
    private final By signInButton = By.xpath("//button[normalize-space()='Sign in'] | //button[@type='submit']");

    // Potential error locators for negative test validation
    private final By passwordError = By.id("error-for-password");
    private final By usernameError = By.id("error-for-username");
    private final By generalAlertBanner = By.xpath("//*[contains(text(), 'Wrong email') or contains(text(), 'Wrong password') or contains(text(), 'check your email') or @id='signal-error-small' or @role='alert']");

    // Security checkpoint / CAPTCHA challenge indicators
    private final By checkpointContainer = By.cssSelector("#checkpoint-container, #captcha-internal, iframe[src*='checkpoint'], div[data-view-name*='challenge']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open(String url) {
        logger.info("Opening LinkedIn login page: {}", url);
        navigateTo(url);
    }

    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        enterText(usernameInput, username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password: [PROTECTED]");
        enterText(passwordInput, password);
    }

    public void clickSignIn() {
        logger.info("Clicking 'Sign in' button");
        click(signInButton);
    }

    public FeedPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
        handleSecurityCheckpointIfPresent();
        return new FeedPage(driver);
    }

    /**
     * Checks if an error message or error indicator is displayed.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            List<WebElement> alerts = driver.findElements(generalAlertBanner);
            for (WebElement alert : alerts) {
                if (alert.isDisplayed()) {
                    return true;
                }
            }
            if (waitUtils.isElementDisplayed(passwordError, 2)) {
                return true;
            }
            return waitUtils.isElementDisplayed(usernameError, 2);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the text of the displayed error message for assertions.
     */
    public String getErrorMessageText() {
        if (waitUtils.isElementDisplayed(passwordError, 3)) {
            return getElementText(passwordError);
        }
        if (waitUtils.isElementDisplayed(usernameError, 3)) {
            return getElementText(usernameError);
        }
        List<WebElement> alerts = driver.findElements(generalAlertBanner);
        for (WebElement alert : alerts) {
            if (alert.isDisplayed() && !alert.getText().trim().isEmpty()) {
                return alert.getText().trim();
            }
        }
        return "Error indicator displayed";
    }

    /**
     * Implements Assignment Rule 2:
     * "If LinkedIn presents a CAPTCHA or verification step, do not attempt to bypass it.
     * Document it in your report and, if needed, pause the test and complete the step manually."
     */
    public void handleSecurityCheckpointIfPresent() {
        if (driver.getCurrentUrl().contains("/checkpoint") || waitUtils.isElementDisplayed(checkpointContainer, 3)) {
            int pauseSeconds = ConfigReader.getIntProperty("checkpoint.timeout", 20);
            logger.warn("================================================================================");
            logger.warn("SECURITY CHECKPOINT / CAPTCHA DETECTED!");
            logger.warn("Pausing execution for {} seconds for manual intervention (Assignment Rule 2)...", pauseSeconds);
            logger.warn("Please solve the checkpoint in the open browser window if required.");
            logger.warn("================================================================================");

            try {
                Thread.sleep(pauseSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Interrupted during manual checkpoint wait: {}", e.getMessage());
            }
        }
    }
}
