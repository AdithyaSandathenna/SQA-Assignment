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
    private final By searchResultsContainer = By.cssSelector(".search-results-container, main.scaffold-layout__main, div[data-view-name*='search']");
    private final By resultCards = By.cssSelector("li.reusable-search__result-container, div.entity-result, div[data-chameleon-result-urn]");
    
    // Locators for result titles and clickable links
    private final By resultTitleLinks = By.cssSelector("span.entity-result__title-text a, a.app-aware-link[href*='/in/'], a.app-aware-link[href*='/company/'], li.reusable-search__result-container a");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verifies that the search results page is loaded.
     * Asserts that URL contains '/search/results' and at least one result element is visible.
     */
    public boolean isResultsPageLoaded() {
        boolean urlMatches = waitUtils.waitForUrlContains("/search/results", 15);
        boolean containerVisible = waitUtils.isElementDisplayed(searchResultsContainer, 10);
        boolean cardVisible = waitUtils.isElementDisplayed(resultCards, 10) || waitUtils.isElementDisplayed(resultTitleLinks, 10);
        return (urlMatches && (containerVisible || cardVisible));
    }

    /**
     * Retrieves the text of the first visible search result.
     */
    public String getFirstResultTitle() {
        WebElement firstLink = waitUtils.waitForVisibility(resultTitleLinks, 15);
        String title = firstLink.getText().trim();
        logger.info("First search result title captured: '{}'", title);
        return title;
    }

    /**
     * Clicks the first search result link to open the detail (profile or company) page.
     * Implements explicit wait for clickability.
     */
    public DetailPage clickFirstResult() {
        logger.info("Attempting to click first search result...");
        WebElement firstLink = waitUtils.waitForClickability(resultTitleLinks);
        
        // Scroll into view to ensure element is within viewport before clicking
        scrollIntoView(resultTitleLinks);
        
        String linkText = firstLink.getText().trim();
        logger.info("Clicking result link: '{}'", linkText);
        firstLink.click();
        
        return new DetailPage(driver, linkText);
    }

    /**
     * Returns total count of visible search result items currently rendered.
     */
    public int getResultCount() {
        List<WebElement> items = driver.findElements(resultCards);
        return items.size();
    }
}
