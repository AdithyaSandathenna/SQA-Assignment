package com.kdu.sqa.tests;

import com.kdu.sqa.base.BaseTest;
import com.kdu.sqa.pages.FeedPage;
import com.kdu.sqa.pages.LoginPage;
import com.kdu.sqa.pages.SearchResultsPage;
import com.kdu.sqa.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Task 3: Automate Search (20 marks)
 * Write a test that, after logging in:
 * 1. Clicks the global search box.
 * 2. Types a search term of your choice (e.g., "Software Testing").
 * 3. Presses Enter or clicks the search suggestion.
 * 4. Verifies that a results page is displayed (assert on URL and at least one visible result element).
 */
public class SearchTest extends BaseTest {

    @Test(priority = 1, description = "Verify search execution and results page display after login")
    public void testAutomateSearch() {
        String baseUrl = ConfigReader.getProperty("baseUrl", "https://www.linkedin.com/login");
        String username = ConfigReader.getProperty("valid.username");
        String password = ConfigReader.getProperty("valid.password");
        String searchTerm = ConfigReader.getProperty("search.term", "Software Testing");

        logger.info("Executing Search Test with query: '{}'", searchTerm);

        // Step 1: Login to reach Feed Page
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open(baseUrl);
        FeedPage feedPage = loginPage.login(username, password);

        Assert.assertTrue(feedPage.isFeedLoaded(), "Login failed or Feed page did not load prior to search.");

        // Step 2 & 3: Click search box, enter term, submit via Enter
        SearchResultsPage resultsPage = feedPage.searchFor(searchTerm);

        // Step 4: Verify results page is displayed (URL containing '/search/results' and visible result element)
        boolean isResultsLoaded = resultsPage.isResultsPageLoaded();
        String currentUrl = resultsPage.getCurrentUrl();
        int resultCount = resultsPage.getResultCount();

        logger.info("Search Results Loaded: {}, URL: {}, Visible items: {}", isResultsLoaded, currentUrl, resultCount);

        Assert.assertTrue(isResultsLoaded,
                "Expected LinkedIn search results page to load with visible items, but validation failed. Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/search/results"),
                "Expected URL to contain '/search/results', but was: " + currentUrl);
    }
}
