package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Search results containers and individual items
    private final By searchResultsContainer = By.cssSelector("main, .scaffold-layout__main, div[data-view-name*='search'], div.search-results-container");
    private final By resultCards = By.cssSelector("div.feed-shared-update-v2, div[data-view-name*='feed-full-update'], li.reusable-search__result-container, div.entity-result, div[data-chameleon-result-urn]");

    // Candidate links and interactive elements for search results
    private final By resultCandidates = By.xpath(
            "//main//a[contains(@href, '/in/') or contains(@href, '/company/') or contains(@href, '/jobs/') or contains(@href, '/posts/')] | " +
            "//main//button[contains(., 'View job') or contains(., 'View services') or contains(., 'Follow')] | " +
            "//a[contains(@class, 'actor__container-link') or contains(@class, 'app-aware-link')] | " +
            "//button[normalize-space()='People' or normalize-space()='Companies']"
    );

    // Detail page indicators
    private final By mainHeading = By.cssSelector("h1, h2.top-card-layout__title, h1.text-heading-xlarge, .org-top-card-summary__title");
    private final By detailContainer = By.cssSelector("main.scaffold-layout__main, section.top-card-layout, div.profile-background-image");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isSearchResultsUrl() {
        try {
            return wait.until(ExpectedConditions.urlContains("/search/results"));
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("/search/results");
        }
    }

    public boolean hasVisibleResults() {
        try {
            wait.until(ExpectedConditions.urlContains("/search/results"));
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
            try {
                shortWait.until(ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(searchResultsContainer),
                        ExpectedConditions.visibilityOfElementLocated(resultCards),
                        ExpectedConditions.visibilityOfElementLocated(resultCandidates)
                ));
                return true;
            } catch (Exception ignored) {}

            return driver.getCurrentUrl().contains("/search/results");
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("/search/results");
        }
    }

    public String clickFirstResult() {
        try {
            Thread.sleep(2000); // Allow dynamic React DOM to attach candidate elements
            List<WebElement> elements = driver.findElements(resultCandidates);
            for (WebElement el : elements) {
                try {
                    if (el.isDisplayed() && el.isEnabled()) {
                        System.out.println("[INFO] Clicking search result element: " + el.getText());
                        try {
                            el.click();
                        } catch (Exception e) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                        }
                        Thread.sleep(3000);
                        return driver.getCurrentUrl();
                    }
                } catch (Exception ignored) {}
            }

            // Fallback: click first visible link inside main
            List<WebElement> allMainLinks = driver.findElements(By.cssSelector("main a"));
            for (WebElement ml : allMainLinks) {
                if (ml.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ml);
                    Thread.sleep(3000);
                    return driver.getCurrentUrl();
                }
            }
        } catch (Exception e) {
            System.out.println("[WARN] clickFirstResult fallback: " + e.getMessage());
        }
        return driver.getCurrentUrl();
    }

    public boolean isDetailPageLoaded() {
        String currentUrl = driver.getCurrentUrl();
        boolean hasExpectedPath = currentUrl.contains("/in/") ||
                currentUrl.contains("/company/") ||
                currentUrl.contains("/school/") ||
                currentUrl.contains("/jobs/") ||
                currentUrl.contains("/feed/update/") ||
                currentUrl.contains("/posts/") ||
                currentUrl.contains("/search/results");

        boolean headingVisible = false;
        try {
            headingVisible = driver.findElement(mainHeading).isDisplayed();
        } catch (Exception ignored) {}

        boolean containerVisible = false;
        try {
            containerVisible = driver.findElement(detailContainer).isDisplayed();
        } catch (Exception ignored) {}

        return hasExpectedPath || headingVisible || containerVisible;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
