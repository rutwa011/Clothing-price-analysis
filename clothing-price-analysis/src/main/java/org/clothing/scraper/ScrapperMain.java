package org.clothing.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ScrapperMain {

    // Method to get WebDriver object
    public WebDriver getDrive() {
        WebDriver drvr = null; // Initialize WebDriver object as null
        String chrme_drvr_pth = "C:\\selenium webdriver\\ChromeDriver\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe"; // Path to ChromeDriver executable
        String user_pth = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36"; // Custom user agent string

        try {
            // Set system property for Chrome_Driver
            System.setProperty("webdriver.chrome.driver", chrme_drvr_pth);

            // Initialize ChromeOptions to configure ChromeDriver
            ChromeOptions options = new ChromeOptions();

            // Maximize the browser window
            options.addArguments("--start-maximized");

            // Set a custom user agent for Chrome
            options.addArguments("user-agent=" + user_pth);

            // Initialize ChromeDriver with configured options
            drvr = new ChromeDriver(options);

        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions that occur
        }
        return drvr; // Return the WebDriver object
    }
}
