package org.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementUtils {

    private WebDriver driver;
    private WebDriverWait wait;

    public ElementUtils(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    public void type(By locator, String text){
       WebElement ele = waitUntilVisible(locator);
       ele.clear();
       ele.sendKeys(text);
    }

    public void click(By locator){
        waitUtilClickable(locator).click();

    }

    public String getText(By locator){
        return waitUntilVisible(locator).getText();
    }

    public Boolean isElementPresent(By locator){
        return waitUntilVisible(locator).isDisplayed();
    }

    public WebElement waitUntilVisible(By locator){
       return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitUtilClickable(By locator){
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
