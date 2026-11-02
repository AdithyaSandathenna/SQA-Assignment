package com.kdu.sqa.tests;

import com.kdu.sqa.base.BaseTest;
import com.kdu.sqa.pages.FeedPage;
import com.kdu.sqa.pages.LoginPage;
import com.kdu.sqa.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Task 2: Automate Login (20 marks)
 * 1. Opens the LinkedIn login page.
 * 2. Enters a valid username and password.
 * 3. Clicks Sign in.
 * 4. Verifies a successful login (feed page URL or presence of nav bar / search box).
 * 5. One negative test: login with an invalid password and assert error message is displayed.
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testLoginWithValidCredentials() {
        String baseUrl = ConfigReader.getProperty("baseUrl", "https://www.linkedin.com/login");
        String username = ConfigReader.getProperty("valid.username");
        String password = ConfigReader.getProperty("valid.password");

        logger.info("Executing Positive Login Test: testLoginWithValidCredentials");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open(baseUrl);

        FeedPage feedPage = loginPage.login(username, password);

        // Verify successful login
        Assert.assertTrue(feedPage.isFeedLoaded(),
                "Expected Feed page to load (URL containing '/feed' or search/nav bar visible), but verification failed.");
        logger.info("Positive login verified successfully. Feed page is displayed.");
    }

    @Test(priority = 2, description = "Verify error message appears when logging in with invalid password")
    public void testLoginWithInvalidPassword() {
        String baseUrl = ConfigReader.getProperty("baseUrl", "https://www.linkedin.com/login");
        String username = ConfigReader.getProperty("valid.username");
        String invalidPassword = ConfigReader.getProperty("invalid.password", "WrongPasswordXYZ999!");

        logger.info("Executing Negative Login Test: testLoginWithInvalidPassword");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.open(baseUrl);

        loginPage.enterUsername(username);
        loginPage.enterPassword(invalidPassword);
        loginPage.clickSignIn();

        // Assert error message is displayed
        boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
        String errorText = loginPage.getErrorMessageText();
        logger.info("Negative test evaluation - Error message displayed: {}, Text: '{}'", errorDisplayed, errorText);

        Assert.assertTrue(errorDisplayed,
                "Expected error message for invalid password was not displayed on the login page.");
    }
}
