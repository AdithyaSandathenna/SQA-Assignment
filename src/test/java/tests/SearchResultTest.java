package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchPage;

public class SearchResultTest extends BaseTest {

    @Test(priority = 8, description = "TC-08: Click first search result")
    public void testClickFirstSearchResult() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(validUsername, validPassword);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLoaded(), "Feed page should load before executing search");

        SearchPage searchPage = homePage.search(searchTerm);
        Assert.assertTrue(searchPage.isSearchResultsUrl(), "URL should navigate to search results");

        String destinationUrl = searchPage.clickFirstResult();
        System.out.println("[INFO] Navigated to search result destination: " + destinationUrl);

        Assert.assertTrue(searchPage.isDetailPageLoaded(),
                "Navigation to result destination (profile, company, or post) should be verified");
    }
}
