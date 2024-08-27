package org.clothing.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Myntra {

    public static void handleCrawling(String category) {
        WebDriver chromeDrive = null;
        String url = "https://www.myntra.com/";
        try {
            ScrapperMain drive = new ScrapperMain();
            chromeDrive = drive.getDrive();

            // Open the website (target URL)
            chromeDrive.get(url);

            WebDriverWait wait = new WebDriverWait(chromeDrive, Duration.ofSeconds(10));
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.desktop-searchBar")));
            searchBox.sendKeys(category);
            searchBox.sendKeys(Keys.ENTER);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".results-base")));
            } catch (Exception e) {
                // If search category not found then display not found message
                System.out.println("Product not found for '" + category + "' in " + url + "\n");
                return;
            }

            // get all item elements
            List<WebElement> productItem = chromeDrive.findElements(By.cssSelector(".product-base"));
            String productTitle = "";
            String productPrice = "";
            String productBrand = "";
            String productCategory = chromeDrive.findElement(By.cssSelector("ul.breadcrumbs-list li:last-child > span")).getText();

            String priceRegex = "Rs\\.\\s*(\\d+)";
            Pattern pattern = Pattern.compile(priceRegex);
            String replacement = "₹$1";

            if (productItem.isEmpty()) {
                System.out.println("Products not found");
            } else {
                String filePath = StoreDataInFile.getFilePath("Myntra.csv");
                StoreDataInFile.deleteIfFileExist(filePath);

                for (WebElement element : productItem) {
                    try {
                        // Extract property details such as title, address, price, link, and image source.
                        productTitle = element.findElement(By.cssSelector(".product-product")).getText();
                        productBrand = element.findElement(By.cssSelector(".product-brand")).getText();

                        try {
                            productPrice = element.findElement(By.cssSelector("div.product-price > span > span.product-discountedPrice")).getText();
                        } catch (Exception e) {
                            productPrice = element.findElement(By.cssSelector("div.product-price > span")).getText();
                        }

                        Matcher matcher = pattern.matcher(productPrice);
                        String replacedPrice = matcher.replaceAll(replacement);

                        // Print property details to the console.
                        StoreDataInFile storeObject = new StoreDataInFile(productTitle, replacedPrice, productBrand, productCategory);

                        storeObject.saveDataToCsv(filePath);
                    } catch (NoSuchElementException e) {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert chromeDrive != null;
            chromeDrive.quit();
        }
    }

    public static void main(String[] args) {
        handleCrawling("Jeans");
    }
}