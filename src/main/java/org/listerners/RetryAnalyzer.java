package org.listerners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 2;
    @Override
    public boolean retry(ITestResult result) {
        if(retryCount<maxRetryCount){
            retryCount++;
            return true;
        }
        if (result.getStatus() == ITestResult.SUCCESS && retryCount > 0) {
            result.setAttribute("flaky", true);
        }
        return false;
    }
}
