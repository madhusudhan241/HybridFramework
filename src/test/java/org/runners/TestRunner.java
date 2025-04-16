package org.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.listerners.ExtentCucumberListener;
import org.listerners.RetryTransformer;

@CucumberOptions(
        features = "src/test/java/org/features",
        glue = {"org.stepdefinition", "org.hooks"},
        monochrome = true,
        plugin = {"pretty",
                "html:target/cucumber-reports.html"
               }
)
@Listeners({ExtentCucumberListener.class, RetryTransformer.class})
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
