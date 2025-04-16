package org.hooks;

import com.aventstack.extentreports.MediaEntityBuilder;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.factory.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.utils.ConfigReader;
import org.listerners.ExtentCucumberListener;

import java.net.MalformedURLException;

public class Hooks {

    WebDriver driver;

    @Before
    public void setup(Scenario scenario) throws MalformedURLException {
        System.out.println("************ BEFORE HOOK ************");
        ExtentCucumberListener.setScenarioName(scenario.getName());
        ExtentCucumberListener.getTest().info("Scenario started: " + scenario.getName());
        String env = System.getProperty("env","qa");
        ConfigReader.laodProperties(env);
        driver = DriverFactory.initDriver(ConfigReader.getProperty("browser"),Boolean.parseBoolean(ConfigReader.getProperty("isCloud")),ConfigReader.getProperty("cloudPlatform"));

    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failed Step Screenshot");

            ExtentCucumberListener.getTest().fail("Step failed",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(
                            ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64)).build());
        }
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
