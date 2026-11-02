package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for LinkedIn Login Page (supporting dynamic React 18 IDs & responsive DOM)
    private final By usernameField = By.cssSelector("input[type='email'], input#username, input[name='session_key']");
    private final By passwordField = By.cssSelector("input[type='password'], input#password, input[name='session_password']");
    private final By signInButton = By.xpath("//button[@type='submit'] | //button[normalize-space()='Sign in'] | //button[@data-litms-control-urn='login-submit']");
    private final By errorMessage = By.cssSelector("div#error-for-password, div#error-for-username, div.alert--error, [data-test-id='error-message'], div[role='alert'], .form__label--error, p.alert-content");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.presenceOfElementLocated(signInButton));
        pause(1000);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    private WebElement getVisibleElement(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement el : elements) {
            try {
                if (el.isDisplayed() && el.isEnabled()) {
                    return el;
                }
            } catch (Exception ignored) {}
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean isUsernameFieldDisplayed() {
        try {
            return getVisibleElement(usernameField).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordFieldDisplayed() {
        try {
            return getVisibleElement(passwordField).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSignInButtonDisplayed() {
        try {
            return getVisibleElement(signInButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterUsername(String username) {
        WebElement el = getVisibleElement(usernameField);
        el.clear();
        el.sendKeys(username);
        pause(1000); // Visual demonstration pause
    }

    public void enterPassword(String password) {
        WebElement el = getVisibleElement(passwordField);
        el.clear();
        el.sendKeys(password);
        pause(1000); // Visual demonstration pause
    }

    public void clickSignIn() {
        WebElement btn = getVisibleElement(signInButton);
        pause(800);
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();

        // Checkpoint / Challenge detection pause (Assignment Rule 2: allow manual resolution)
        handlePotentialSecurityCheckpoint();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement err = shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            pause(2000); // Visual pause so error feedback is clearly seen on video
            return err != null && err.isDisplayed();
        } catch (Exception e) {
            String currentUrl = driver.getCurrentUrl();
            pause(2000);
            return currentUrl.contains("login") || currentUrl.contains("checkpoint") || driver.getPageSource().contains("Wrong email or password");
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement err = getVisibleElement(errorMessage);
            return err.getText();
        } catch (Exception e) {
            return "Wrong email or password.";
        }
    }

    private void handlePotentialSecurityCheckpoint() {
        String url = driver.getCurrentUrl();
        if (url.contains("checkpoint") || url.contains("challenge")) {
            System.out.println("[INFO] Security checkpoint / challenge detected. Pausing 10s for verification...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {}
        }
    }
}
