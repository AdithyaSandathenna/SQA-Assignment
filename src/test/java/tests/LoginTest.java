package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "TC-01: Open LinkedIn login page")
    public void testOpenLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("linkedin.com"), "Current URL should contain linkedin.com");
    }

    @Test(priority = 2, description = "TC-02: Verify login page title")
    public void testVerifyLoginPageTitle() {
        LoginPage loginPage = new LoginPage(driver);
        String title = loginPage.getTitle();
        Assert.assertTrue(title.toLowerCase().contains("linkedin"), "Page title should contain 'LinkedIn'");
    }

    @Test(priority = 3, description = "TC-03: Verify login form fields")
    public void testVerifyLoginFormFields() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username / Email input should be displayed");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password input should be displayed");
        Assert.assertTrue(loginPage.isSignInButtonDisplayed(), "Sign In button should be displayed");
    }

    @Test(priority = 4, description = "TC-04: Login page opens in LoginTest")
    public void testLoginPageOpens() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.getCurrentUrl().contains("login") || loginPage.isSignInButtonDisplayed(),
                "Login page should be open and display sign in controls");
    }

    @Test(priority = 5, description = "TC-05: Valid login")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(validUsername, validPassword);

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLoaded(), "User should navigate to home/feed after valid credentials");
    }

    @Test(priority = 6, description = "TC-06: Invalid-password login")
    public void testInvalidPasswordLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(invalidUsername, invalidPassword);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error notification or invalid-credential feedback should be displayed");
    }
}
