package org.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    WebDriver driver;

    private By txtEmail= By.xpath("//input[@name='email']");
    private By txtPassword = By.xpath("//input[@name='password']");
    private By btnLogin = By.xpath("//input[@type='submit']");
    private By forgotpwdLink =By.xpath("//a[text()='Forgotten Password']");

    public LoginPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }

    public void enterEmail(String email){
        elementUtils.type(txtEmail,email);
    }

    public void enterPassword(String password){
        elementUtils.type(txtPassword,password);
    }

    public void clickonLogin(){
        elementUtils.click(btnLogin);
    }

    public void browsetheURL(String url){
        driver.get(url);
    }

    public String getLoginPageTitle() {
        return driver.getTitle();
    }

    public boolean isForgotPwdPresent(){
        return elementUtils.isElementPresent(forgotpwdLink);
    }
}
