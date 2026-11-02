package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchPage;

public class SearchTest extends BaseTest {

    @Test(priority = 7, description = "TC-07: Search 'software testing'")
    public void testSearchSoftwareTesting() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(validUsername, validPassword);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLoaded(), "Feed page should load before executing search");

        SearchPage searchPage = homePage.search(searchTerm);
        Assert.assertTrue(searchPage.isSearchResultsUrl(), "URL should contain '/search/results'");
        Assert.assertTrue(searchPage.hasVisibleResults(), "Search results should be visible on the page");
    }
}
