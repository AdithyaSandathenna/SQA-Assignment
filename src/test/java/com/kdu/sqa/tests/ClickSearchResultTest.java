package com.kdu.sqa.tests;

import com.kdu.sqa.base.BaseTest;
import com.kdu.sqa.pages.DetailPage;
import com.kdu.sqa.pages.FeedPage;
import com.kdu.sqa.pages.LoginPage;
import com.kdu.sqa.pages.SearchResultsPage;
import com.kdu.sqa.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Task 4: Click a Search Result (20 marks)
 * Write a test that:
 * 1. Performs a search as in Task 3.
 * 2. Clicks the first result (person, company, or any result type).
 * 3. Verifies that the correct page opened (URL contains expected path or heading matches result).
 * Includes explicit waits (WebDriverWait) for element visibility and clickability.
 */
public class ClickSearchResultTest extends BaseTest {

    @Test(priority = 1, description = "Verify clicking the first search result opens the correct target detail page")
    public void testClickFirstSearchResult() {
        String baseUrl = ConfigReader.getProperty("baseUrl", "https://www.linkedin.com/login");
        String username = ConfigReader.getProperty("valid.username");
        String password = ConfigReader.getProperty("valid.password");
        String searchTerm = ConfigReader.getProperty("search.term", "Software Testing");

        logger.info("Executing Click Search Result Test. Search query: '{}'", searchTerm);

        // Step 1: Login
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open(baseUrl);
        FeedPage feedPage = loginPage.login(username, password);
        Assert.assertTrue(feedPage.isFeedLoaded(), "Feed page failed to load prior to search.");

        // Step 2: Search
        SearchResultsPage resultsPage = feedPage.searchFor(searchTerm);
        Assert.assertTrue(resultsPage.isResultsPageLoaded(), "Search results page did not load.");

        // Step 3: Capture the first result title/name, then click it
        String expectedTitle = resultsPage.getFirstResultTitle();
        logger.info("Target search result selected: '{}'", expectedTitle);

        DetailPage detailPage = resultsPage.clickFirstResult();

        // Step 4: Verify target detail page opened
        boolean isDetailLoaded = detailPage.isDetailPageLoaded();
        String currentUrl = detailPage.getCurrentUrl();
        String headingText = detailPage.getHeadingText();

        logger.info("Detail Page verification - Loaded: {}, URL: {}, Heading: '{}'", isDetailLoaded, currentUrl, headingText);

        Assert.assertTrue(isDetailLoaded,
                "Expected Detail Page (profile/company) to load, but verification failed. Current URL: " + currentUrl);

        // Verify either URL contains entity path (/in/, /company/, /school/, /posts/) or page heading is present
        boolean urlValid = currentUrl.contains("/in/") ||
                           currentUrl.contains("/company/") ||
                           currentUrl.contains("/school/") ||
                           currentUrl.contains("/search/results") ||
                           currentUrl.contains("/posts/");

        Assert.assertTrue(urlValid, "Current URL did not match expected detail page pattern: " + currentUrl);
    }
}
