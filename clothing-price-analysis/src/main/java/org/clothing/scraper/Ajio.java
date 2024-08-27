package org.clothing.scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ajio {
    // Scrap Ajio products based on url provided
    public static void handleCrawling(String category) {
        WebDriver chromeDrive = null;
        String url = "https://www.ajio.com/";

        try {
            ScrapperMain drive = new ScrapperMain();
            chromeDrive = drive.getDrive();

            chromeDrive.get(url);

            // Retrieve recurring element from Doc
            WebDriverWait wait = new WebDriverWait(chromeDrive, Duration.ofSeconds(10));
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[aria-label='Search Ajio'][name='searchVal']")));
            searchBox.sendKeys(category);
            searchBox.sendKeys(Keys.ENTER);

            String cssPathForProductCard = "div.item.rilrtl-products-list__item.item";
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssPathForProductCard)));
            } catch (Exception e) {
                // If search category not found then display not found message
                System.out.println("Product not found for '" + category + "' in " + url + "\n");
                return;
            }
            String productTitle = "";
            String productPrice = "";
            String productBrand = "";
            String productCategory = "";
            try {
                productCategory = chromeDrive.findElement(By.cssSelector("ul.breadcrumb-sec li:last-child > a")).getText();
            } catch (Exception e) {
                productCategory = category;
            }

            String priceRegex = "₹\\d+";

            // get all item elements
            List<WebElement> productItems = chromeDrive.findElements(By.cssSelector(cssPathForProductCard));

            if (productItems.isEmpty()) {
                System.out.println("Products not found");
            } else {
                String filePath = StoreDataInFile.getFilePath("Ajio.csv");
                StoreDataInFile.deleteIfFileExist(filePath);

                for (WebElement element : productItems) {
                    try {
                        // Extract property details such as title, address, price, link, and image source.
                        productTitle = element.findElement(By.cssSelector("div.nameCls")).getText();
                        productPrice = element.findElement(By.cssSelector("div > span.price > strong")).getText();
                        productBrand = element.findElement(By.cssSelector("div.brand > strong")).getText();

                        Matcher matcher = Pattern.compile(priceRegex).matcher(productPrice);

                        if (matcher.find()) {
                            // Print property details to the console.
                            StoreDataInFile storeObject = new StoreDataInFile(productTitle, productPrice, productBrand, productCategory);
                            storeObject.saveDataToCsv(filePath);
                        } else {
                            continue;
                        }
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
        handleCrawling("Shirts");
    }
}
