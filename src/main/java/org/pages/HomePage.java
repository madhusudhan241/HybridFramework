package org.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver){
        super(driver);
    }

    private By linkChangePassword = By.xpath("//a[text()='Change your password']");

    public boolean isChangePasswordLinkDisplayed() {
        return elementUtils.isElementPresent(linkChangePassword);
    }
}
