package org.stepdefinition;

import io.cucumber.java.en.Then;
import org.factory.DriverFactory;
import org.openqa.selenium.By;
import org.pages.HomePage;
import org.testng.Assert;

public class HomeScreenStepDefinition {

    HomePage homePage = new HomePage(DriverFactory.getDriver());

    @Then("Change Password link should present")
    public void change_Password_link() {
        Assert.assertTrue(homePage.isChangePasswordLinkDisplayed());
    }

}


