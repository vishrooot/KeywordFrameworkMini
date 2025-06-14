// src/main/java/reports/ReportManager.java

package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
// Replace the old import with Spark
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Manages one global ExtentReports instance and a ThreadLocal<ExtentTest> so that
 * each TestNG thread has its own ExtentTest (its own "test" node in the HTML report).
 */
public class ReportManager {

    // 1. The shared ExtentReports object (one per JVM)
    private static ExtentReports extent;

    // 2. A ThreadLocal to store each thread’s ExtentTest
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    /**
     * Initialize and return the singleton ExtentReports instance.
     * This is synchronized so that even if multiple threads call getReporter(),
     * we only set up ExtentReports once.
     */
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            // 2.a Where to write the HTML file
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

            // 2.b Create and configure the Spark reporter (replaces ExtentHtmlReporter)
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Execution Report");
            sparkReporter.config().setReportName("Keyword-Driven Tests");
            sparkReporter.config().setTheme(Theme.STANDARD);

            // 2.c Create ExtentReports and attach the Spark reporter
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // 2.d Add system info (optional but useful)
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
        return extent;
    }

    /**
     * Create a new ExtentTest under the shared ExtentReports and store it in ThreadLocal.
     * Call this in @BeforeMethod (or @BeforeClass) to initialize the test node for this thread.
     *
     * @param testName the name to display in the report for this test invocation (e.g., testcaseId)
     */
    public static void createTest(String testName) {
        // 3.a Get the singleton ExtentReports
        ExtentReports reporter = getReporter();

        // 3.b Create a new test node (ExtentTest) for this testName
        ExtentTest test = reporter.createTest(testName);

        // 3.c Store it in ThreadLocal so this thread’s code can fetch it via getTest()
        testThread.set(test);
    }

    /**
     * Return the ExtentTest instance associated with the current thread.
     * This is the object on which you call .info(), .pass(), .fail(), etc.
     */
    public static ExtentTest getTest() {
        return testThread.get();
    }

    /**
     * Flush (write out) the entire report to disk. Call once at @AfterSuite.
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
