// src/main/java/keywords/KeywordEngine.java

package keywords;

import base.DriverManager;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ReportManager;

/**
 * Keyword engine that executes low-level actions (click, type, etc.) and logs each step to Extent.
 */
public class KeywordEngine {

    /**
     * Execute one keyword action by sending the appropriate Selenium command,
     * and log INFO/PASS/FAIL into Extent.
     *
     * @param action  the keyword (e.g., "open", "click", "type")
     * @param locator the XPath or other locator string
     * @param value   associated data for the step (URL to open or text to type)
     */
    public static void execute(String action, String locator, String value) {
        WebDriver driver = DriverManager.getDriver();

        // 1. Log start of this keyword at INFO level
        ReportManager.getTest().log(Status.INFO,
                "Executing action: " + action + ", Locator: " + locator + ", Value: " + value);

        try {
            switch (action.toLowerCase()) {
                case "open":
                    driver.get(value);
                    break;

                case "click":
                    driver.findElement(By.xpath(locator)).click();
                    break;

                case "type":
                    driver.findElement(By.xpath(locator)).sendKeys(value);
                    break;

                default:
                    // 2.a If the action is unknown, log a WARNING
                    ReportManager.getTest().log(Status.WARNING, "Unknown action: " + action);
                    throw new IllegalArgumentException("Unknown action: " + action);
            }
            // 2.b If no exception, log a PASS
            ReportManager.getTest().log(Status.PASS, "Successfully performed: " + action);
        } catch (Exception e) {
            // 3. On exception, log FAIL (with the exception message), then re-throw
            ReportManager.getTest().log(Status.FAIL,
                    "Failed on action: " + action + " – " + e.getMessage());
            throw e;  // Let TestNG catch and mark the test as failed
        }
    }
}
