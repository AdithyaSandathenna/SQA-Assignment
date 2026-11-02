package com.kdu.sqa.pages;

import com.kdu.sqa.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object representing LinkedIn Feed / Home page after successful login.
 * Supports Task 2 verification and Task 3 search initiation.
 */
public class FeedPage extends BasePage {

    // Locators for search box and feed page navigation
    private final By globalSearchInput = By.cssSelector("input.search-global-typeahead__input, input[placeholder*='Search'], input[aria-label*='Search']");
    private final By navBar = By.cssSelector("nav.global-nav, #global-nav, .global-nav__content");
    private final By profileNavIcon = By.cssSelector("button[aria-label*='Me'], img[alt*='Photo of'], .global-nav__me");

    public FeedPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verifies that the feed page has successfully loaded.
     * Evaluates both URL containment and presence of primary navigation elements.
     */
    public boolean isFeedLoaded() {
        boolean urlMatch = waitUtils.waitForUrlContains("/feed", 15);
        boolean navVisible = waitUtils.isElementDisplayed(navBar, 10);
        boolean searchVisible = waitUtils.isElementDisplayed(globalSearchInput, 10);
        return urlMatch || navVisible || searchVisible;
    }

    public void clickSearchBox() {
        logger.info("Clicking global search box");
        click(globalSearchInput);
    }

    public void typeSearchTerm(String term) {
        logger.info("Typing search query: {}", term);
        WebElement searchInput = waitUtils.waitForVisibility(globalSearchInput);
        searchInput.clear();
        searchInput.sendKeys(term);
    }

    public SearchResultsPage submitSearch() {
        logger.info("Submitting search with Enter key");
        WebElement searchInput = waitUtils.waitForVisibility(globalSearchInput);
        searchInput.sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }

    /**
     * Convenience method combining click, type, and enter.
     */
    public SearchResultsPage searchFor(String query) {
        clickSearchBox();
        typeSearchTerm(query);
        return submitSearch();
    }
}
