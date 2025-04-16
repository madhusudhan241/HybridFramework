package org.listerners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentCucumberListener implements ITestListener {
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static ThreadLocal<String> scenarioName = new ThreadLocal<>();

    public static void setScenarioName(String name) {
        scenarioName.set(name);
    }
    @Override
    public void onTestStart(ITestResult result) {
        String name = scenarioName.get() != null ? scenarioName.get() : result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(name);
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (result.getMethod().getCurrentInvocationCount() > 1) {
            testThread.get().pass("⚠️ Test passed on retry — marked as FLAKY.").assignCategory("Flaky Test");
        } else {
            testThread.get().pass("✅ Test passed successfully.").assignCategory("Feature");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testThread.get().fail("❌ Test failed: " + result.getThrowable()).assignCategory("Feature");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
    public static ExtentTest getTest() {
        return testThread.get();
    }
}
