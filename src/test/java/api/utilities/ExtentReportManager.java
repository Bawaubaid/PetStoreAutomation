package api.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener {

    private ExtentReports extReports;
    private static ThreadLocal <ExtentTest> test = new ThreadLocal<>();
    private String realtime;

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest Test) {
        test.set(Test);
    }

    @Override
    public void onStart(ITestContext context) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss a");
        realtime = "Test-Report-" + time.format(customFormat) + ".html"; // Ensure .html extension

        extReports = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("./reports/" + realtime);

        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("RestAssured Petstore Automation");
        sparkReporter.config().setReportName("Pet Store API Test Report");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss a");

        extReports.attachReporter(sparkReporter);
        extReports.setSystemInfo("Application", "Pet Store Users API");
        extReports.setSystemInfo("User", "Ubaid");
        extReports.setSystemInfo("Environment", "QA");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extReports.createTest(
            result.getTestClass().getName() + " - " +
            result.getMethod().getPriority() + ". " +
            result.getMethod().getMethodName(),
            result.getMethod().getDescription()
        );
        setTest(extentTest);
        getTest().log(Status.INFO, result.getName() + " started executing.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            test.createNode(result.getName());
            test.assignCategory(result.getMethod().getGroups());
            test.log(Status.PASS, result.getName() + " got successfully executed.");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            test.createNode(result.getName());
            test.assignCategory(result.getMethod().getGroups());
            test.log(Status.INFO, result.getThrowable());
            test.log(Status.FAIL, result.getName() + " got failed.");
        } else {
            System.out.println("ExtentTest is null in onTestFailure for: " + result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            test.createNode(result.getName());
            test.assignCategory(result.getMethod().getGroups());
            test.log(Status.INFO, result.getThrowable());
            test.log(Status.SKIP, result.getName() + " got skipped.");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extReports.flush();

        try {
            Thread.sleep(2000); // Optional delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String pathOfExtentReport = System.getProperty("user.dir") + "/reports/" + realtime;
        File extentReport = new File(pathOfExtentReport);

        if (extentReport.exists()) {
            try {
                Desktop.getDesktop().browse(extentReport.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Report file not found: " + pathOfExtentReport);
        }
    }
}
