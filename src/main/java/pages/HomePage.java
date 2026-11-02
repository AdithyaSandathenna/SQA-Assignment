package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for LinkedIn Home / Feed
    private final By searchBox = By.cssSelector("input[placeholder*='Search'], input.search-global-typeahead__input, input[aria-label*='Search']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

    public boolean isLoaded() {
        try {
            String url = driver.getCurrentUrl();
            if (url.contains("/feed") || url.contains("/search") || url.contains("/mynetwork")) {
                pause(1500); // Allow feed to display clearly in video
                return true;
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(searchBox));
            pause(1500);
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("linkedin.com");
        }
    }

    public boolean isSearchBoxDisplayed() {
        try {
            return getVisibleSearchBox().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement getVisibleSearchBox() {
        List<WebElement> boxes = driver.findElements(searchBox);
        for (WebElement b : boxes) {
            try {
                if (b.isDisplayed()) {
                    return b;
                }
            } catch (Exception ignored) {}
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
    }

    public SearchPage search(String query) {
        WebElement box = getVisibleSearchBox();
        pause(800);
        box.click();
        box.clear();
        pause(600);
        box.sendKeys(query);
        pause(1000); // Visual demonstration pause showing typed search term
        box.sendKeys(Keys.ENTER);

        try {
            Thread.sleep(2500);
            wait.until(ExpectedConditions.urlContains("/search/"));
        } catch (Exception ignored) {}

        return new SearchPage(driver);
    }
}
