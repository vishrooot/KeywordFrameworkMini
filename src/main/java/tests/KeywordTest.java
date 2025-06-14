// src/main/java/tests/KeywordTest.java

package tests;

import base.DriverManager;
import driver.DriverFactory;
import keywords.KeywordEngine;
import org.testng.annotations.*;
import utils.ReportManager;
import utils.DBManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * TestNG test class that drives each Testcase from the DB. Each TestNG invocation (one per testcaseId)
 * is wrapped with @BeforeMethod/@AfterMethod, and logs to Extent via ReportManager.
 */
public class KeywordTest {

    // 1. Before any @Test runs, initialize ExtentReports once
    @BeforeSuite
    public void beforeSuite() {
        ReportManager.getReporter();  // ensures ExtentReports is set up
    }

    // 2. Before each test method (each testcaseId invocation), create a new ExtentTest and WebDriver
    @BeforeMethod
    public void setup(Method method, Object[] testArgs) {
        // 2.a Derive a unique test name. We’ll append the testcaseId to the method name:
        // e.g., "runTestFromDB[TC001]" so it’s easy to see which DB row we ran.
        String testcaseId = (String) testArgs[0];
        String testName = method.getName() + "[" + testcaseId + "]";

        // 2.b Create an ExtentTest for this thread
        ReportManager.createTest(testName);

        // 2.c Initialize a browser (local Chrome for now)
        DriverFactory.initializeDriver("chrome");
    }

    /**
     * 3. DataProvider that returns every enabled testcase ID from the DB.
     *    TestNG will invoke runTestFromDB(String) once per ID.
     */
    @DataProvider(name = "allTestcases", parallel = true)
    public Object[][] allTestcasesProvider() {
        List<String> ids = DBManager.getAllTestcaseIds();
        Object[][] arr = new Object[ids.size()][1];
        for (int i = 0; i < ids.size(); i++) {
            arr[i][0] = ids.get(i);
        }
        return arr;
    }

    /**
     * 4. The single @Test method—TestNG calls this once per testcaseId in the DataProvider.
     *    We fetch all steps for that testcaseId and loop through them.
     *
     * @param testcaseId the ID of the testcase to run (from DB)
     */
    @Test(dataProvider = "allTestcases")
    public void runTestFromDB(String testcaseId) {
        // 4.a Log into Extent that we’re starting this testcase
        ReportManager.getTest().info("Starting test for Testcase ID: " + testcaseId);

        // 4.b Retrieve the sequence of steps for this testcase
        List<String[]> steps = DBManager.getTestSteps(testcaseId);

        // 4.c Iterate through each step
        for (String[] step : steps) {
            String action  = step[0];
            String locator = step[1];
            String data    = step[2];

            // 4.d Log step details
            ReportManager.getTest().info(
                    String.format("Step - Action: %s, Locator: %s, Data: %s", action, locator, data)
            );

            // 4.e Execute the keyword (this also logs PASS/FAIL inside KeywordEngine)
            KeywordEngine.execute(action, locator, data);
        }

        // 4.f Optionally, you can log that the entire testcase passed if no exception
        ReportManager.getTest().pass("Completed all steps for Testcase ID: " + testcaseId);
    }

    // 5. After each invocation, quit the browser
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    // 6. After all tests in this suite finish, flush the Extent report
    @AfterSuite
    public void afterSuite() {
        ReportManager.flushReports();
    }
}
