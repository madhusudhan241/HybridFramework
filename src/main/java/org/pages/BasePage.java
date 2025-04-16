package org.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.utils.ElementUtils;

public class BasePage {

    WebDriver driver;
    ElementUtils elementUtils;

    public BasePage(WebDriver driver){
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
    }

}
