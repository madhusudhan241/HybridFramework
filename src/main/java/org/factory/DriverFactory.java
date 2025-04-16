package org.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    public static ThreadLocal<WebDriver> tldriver = new ThreadLocal<>();

    public static WebDriver initDriver(String browser, boolean isCloud, String cloudPlatform) throws MalformedURLException {
        WebDriver driver;

        if(isCloud){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browserName",browser);

            switch (cloudPlatform.toUpperCase()){
                case "BROWSERSTACK":
                    caps.setCapability("bstack:options", getBrowserstackOptions());
                    tldriver.set(new RemoteWebDriver(new URL(getBrowserstackUrl()), caps));
                    break;

                case "SAUCELABS":
                    caps.setCapability("sauce:options", getSauceLabsOptions());
                    tldriver.set(new RemoteWebDriver(new URL(getSauceLabsUrl()), caps));

                    break;

                case "AWS":
                    URL awsGridUrl = new URL(getAWSGridUrl());
                    tldriver.set(new RemoteWebDriver(awsGridUrl, caps));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported cloud platform: " + cloudPlatform);


            }
        }else{
            switch(browser.toUpperCase()) {
                case "CHROME": {
                    tldriver.set(new ChromeDriver());
                    break;
                }
                case "FIREFOX": {
                    tldriver.set(new FirefoxDriver());
                    break;
                }
                case "EDGE": {
                    tldriver.set(new EdgeDriver());
                    break;
                }
                default:
                    throw new IllegalStateException("INVALID BROWSER: " + browser);
            }
        }
        getDriver().manage().window().maximize();
        return getDriver();
    }

    public static synchronized WebDriver getDriver() {
            return tldriver.get();
    }

    private static String getBrowserstackUrl() {
        String username = System.getenv("BS_USERNAME");
        String accessKey = System.getenv("BS_ACCESS_KEY");
        return "https://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub";
    }

    private static String getSauceLabsUrl() {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        return "https://" + username + ":" + accessKey + "@ondemand.saucelabs.com/wd/hub";
    }

    private static String getAWSGridUrl() {
        // Replace this with your actual AWS Selenium Grid URL (public DNS or IP)
        // Example: "http://ec2-3-89-123-45.compute-1.amazonaws.com:4444/wd/hub"
        return System.getenv("AWS_GRID_URL");  // Can be set in Jenkins/env or properties
    }

    private static DesiredCapabilities getBrowserstackOptions() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", "Windows");
        caps.setCapability("osVersion", "11");
        caps.setCapability("project", "QA Automation");
        caps.setCapability("build", "Build_1.0");
        caps.setCapability("name", "BrowserStack Test");
        return caps;
    }

    private static DesiredCapabilities getSauceLabsOptions() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("build", "Build_1.0");
        caps.setCapability("name", "SauceLabs Test");
        return caps;
    }
}