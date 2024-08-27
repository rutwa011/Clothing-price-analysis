package org.clothing.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Flipkart {
    public static void handleCrawling(String category) {
        WebDriver chromeDrive = null;
        String url = "https://www.flipkart.com/";

        try {
            ScrapperMain drive = new ScrapperMain();
            chromeDrive = drive.getDrive();

            chromeDrive.get(url);

            String xPathOfInputBox = "/html/body/div[1]/div/div[1]/div/div/div/div/div[1]/div/div[1]/div/div[1]/div[1]/header/div[1]/div[2]/form/div/div/input";

            // Retrieve recurring element from Doc
            WebDriverWait wait = new WebDriverWait(chromeDrive, Duration.ofSeconds(10));
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPathOfInputBox)));
            searchBox.sendKeys(category);
            searchBox.sendKeys(Keys.ENTER);

            String cssPathForProductCard = "._2B099V";
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssPathForProductCard)));
            } catch (Exception e) {
                // If search category not found then display not found message
                System.out.println("Product not found for '" + category + "' in " + url + "\n");
                return;
            }

            // get all item elements
            List<WebElement> productItems = chromeDrive.findElements(By.cssSelector(cssPathForProductCard));

            String productTitle = "";
            String productPrice = "";
            String productBrand = "";
            String productCategory = "";
            try {
                productCategory = chromeDrive.findElement(By.cssSelector("div._1MR4o5 > div:last-child > a._2whKao")).getText();
            } catch (Exception e) {
                productCategory = category;
            }

            if (productItems.isEmpty()) {
                System.out.println("Products not found");
            } else {
                String filePath = StoreDataInFile.getFilePath("Flipkart.csv");
                StoreDataInFile.deleteIfFileExist(filePath);

                for (WebElement element : productItems) {
                    try {
                        // Extract property details such as title, address, price, link, and image source.
                        productTitle = element.findElement(By.cssSelector("a.IRpwTa")).getText();
                        productPrice = element.findElement(By.cssSelector("div._30jeq3")).getText();
                        productBrand = element.findElement(By.cssSelector("div._2WkVRV")).getText();

                        // Print property details to the console.
                        StoreDataInFile storeObject = new StoreDataInFile(productTitle, productPrice, productBrand, productCategory);
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

    // MAIN FUNCTION
    public static void main(String[] args) {
        handleCrawling("Casual");
    }
}
