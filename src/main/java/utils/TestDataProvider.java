package utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
    @DataProvider(name = "testData")
    public static Object[][] getData() {
        return new Object[][] {
            {"open", "", "https://example.com"},
            {"type", "//input[@name='q']", "Selenium"},
            {"click", "//input[@type='submit']", ""}
        };
    }
}