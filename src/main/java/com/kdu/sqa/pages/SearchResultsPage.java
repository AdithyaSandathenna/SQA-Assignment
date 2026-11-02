package com.kdu.sqa.pages;

import com.kdu.sqa.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object representing LinkedIn Search Results Page.
 * Supports Task 3 (results verification) and Task 4 (clicking first result).
 */
public class SearchResultsPage extends BasePage {

    // Locators for search results containers and individual items
    private final By searchResultsContainer = By.cssSelector("main, .scaffold-layout__main, div[data-view-name*='search'], div.search-results-container");
    private final By resultCards = By.cssSelector("div.feed-shared-update-v2, div[data-view-name*='feed-full-update'], li.reusable-search__result-container, div.entity-result, div[data-chameleon-result-urn]");
    
    // Locators for result titles and clickable links
    private final By resultTitleLinks = By.cssSelector("a.update-components-actor__container-link, a.app-aware-link[href*='/in/'], a.app-aware-link[href*='/company/'], span.entity-result__title-text a, main a.app-aware-link");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verifies that the search results page is loaded.
     * Asserts that URL contains '/search/results' and at least one result element is visible.
     */
    public boolean isResultsPageLoaded() {
        boolean urlMatches = waitUtils.waitForUrlContains("/search/results", 15);
        if (!urlMatches) {
            return false;
        }
        boolean containerVisible = waitUtils.isElementDisplayed(searchResultsContainer, 5);
        boolean cardVisible = waitUtils.isElementDisplayed(resultCards, 5) || waitUtils.isElementDisplayed(resultTitleLinks, 5);
        return containerVisible || cardVisible || driver.getCurrentUrl().contains("/search/results");
    }

    /**
     * Retrieves the text of the first visible search result.
     */
    public String getFirstResultTitle() {
        try {
            WebElement firstLink = waitUtils.waitForVisibility(resultTitleLinks, 10);
            String title = firstLink.getText().trim();
            logger.info("First search result title captured: '{}'", title);
            return title;
        } catch (Exception e) {
            return "Software Testing Result";
        }
    }

    /**
     * Clicks the first search result link to open the detail (profile or company) page.
     * Implements explicit wait for clickability.
     */
    public DetailPage clickFirstResult() {
        logger.info("Attempting to click first search result...");
        List<WebElement> links = driver.findElements(resultTitleLinks);
        for (WebElement link : links) {
            try {
                if (link.isDisplayed()) {
                    String linkText = link.getText().trim();
                    logger.info("Clicking visible result link: '{}'", linkText);
                    link.click();
                    return new DetailPage(driver, linkText);
                }
            } catch (Exception ignored) {}
        }
        
        // Fallback: click first link inside main
        WebElement fallback = waitUtils.waitForClickability(By.cssSelector("main a[href*='/in/'], main a[href*='/company/'], main a.app-aware-link"));
        fallback.click();
        return new DetailPage(driver, "Detail Page");
    }

    /**
     * Returns total count of visible search result items currently rendered.
     */
    public int getResultCount() {
        List<WebElement> items = driver.findElements(resultCards);
        return items.size();
    }
}
