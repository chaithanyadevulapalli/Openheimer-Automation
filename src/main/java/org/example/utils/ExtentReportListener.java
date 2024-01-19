package org.example.utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class ExtentReportListener extends TestListenerAdapter {
    private ExtentReports extent;
    private ExtentTest test;

    @Override
    public void onStart(ITestContext testContext) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test-output/extent-report.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        extent.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, MarkupHelper.createLabel(result.getMethod().getMethodName(), ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName(), ExtentColor.RED));

        // Add exception details to the report
        test.fail(result.getThrowable());
    }
}
