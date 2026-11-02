package com.kdu.sqa.pages;

import com.kdu.sqa.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object representing the target Profile, Company, or Result Detail page.
 * Supports Task 4 verification.
 */
public class DetailPage extends BasePage {

    private final String expectedResultName;

    // Headings and containers on profile or company pages
    private final By mainHeading = By.cssSelector("h1, h2.top-card-layout__title, h1.text-heading-xlarge, .org-top-card-summary__title");
    private final By detailContainer = By.cssSelector("main.scaffold-layout__main, section.top-card-layout, div.profile-background-image");

    public DetailPage(WebDriver driver, String expectedResultName) {
        super(driver);
        this.expectedResultName = expectedResultName;
    }

    public DetailPage(WebDriver driver) {
        super(driver);
        this.expectedResultName = "";
    }

    /**
     * Verifies that the detail page opened successfully.
     * Checks if URL contains an expected path (/in/, /company/, /school/, etc.)
     * or if a prominent heading/container is visible.
     */
    public boolean isDetailPageLoaded() {
        String currentUrl = driver.getCurrentUrl();
        boolean hasExpectedPath = currentUrl.contains("/in/") || 
                                  currentUrl.contains("/company/") || 
                                  currentUrl.contains("/school/") ||
                                  currentUrl.contains("/posts/");

        boolean headingVisible = waitUtils.isElementDisplayed(mainHeading, 10);
        boolean containerVisible = waitUtils.isElementDisplayed(detailContainer, 10);

        return hasExpectedPath || headingVisible || containerVisible;
    }

    /**
     * Retrieves the text of the primary heading on the target page.
     */
    public String getHeadingText() {
        if (waitUtils.isElementDisplayed(mainHeading, 10)) {
            return getElementText(mainHeading);
        }
        return driver.getTitle();
    }

    public String getExpectedResultName() {
        return expectedResultName;
    }
}
