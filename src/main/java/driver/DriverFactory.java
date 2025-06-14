package driver;

import base.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    public static void initializeDriver(String browser) {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "firefox":
                // (you can still use WDM for Firefox, or let Selenium Manager do it)
                driver = new org.openqa.selenium.firefox.FirefoxDriver();
                break;
            case "chrome":
            default:
                ChromeOptions options = new ChromeOptions();
                // Explicitly point to your Chrome if it’s in a non-standard place:
                options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
                // **No WebDriverManager needed** below—Selenium Manager handles it:
                driver = new ChromeDriver(options);
                break;
        }
        driver.manage().window().maximize();
        DriverManager.setDriver(driver);
    }
}
