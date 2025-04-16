package org.stepdefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.factory.DriverFactory;
import org.pages.LoginPage;
import org.testng.Assert;
import org.utils.ConfigReader;

public class LoginPageStepDefinition {

    private static String title;
    LoginPage loginPage = new LoginPage(DriverFactory.getDriver());

    @Given("user is on login page")
    public void user_browse_the_following_url() {
        loginPage.browsetheURL(ConfigReader.getProperty("baseURL"));

    }
    @When("User login with valid credentials {string} {string}")
    public void user_login_with_valid_credentials(String email,String pwd) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(pwd);
        loginPage.clickonLogin();
    }

    @When("user gets the title of the page")
    public void user_gets_the_title_of_the_page() {
        title = loginPage.getLoginPageTitle();
        System.out.println("Page title is: " + title);
    }

    @Then("page title should be {string}")
    public void page_title_should_be(String expectedTitleName) {
        Assert.assertTrue(title.contains(expectedTitleName));
    }

    @Then("forgot your password link should be displayed")
    public void forgotPassword() {
        Assert.assertTrue(loginPage.isForgotPwdPresent());
    }

}
