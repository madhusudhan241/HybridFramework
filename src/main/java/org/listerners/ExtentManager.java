package org.listerners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static final ExtentReports extent = new ExtentReports();

    public static ExtentReports getInstance() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", System.getProperty("env","config"));
        return extent;
    }
}
