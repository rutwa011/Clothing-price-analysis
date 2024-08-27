package org.clothing.scraper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StoreDataInFile {

    // Instance variables to store product information
    private String productName;
    private String productPrice;
    private String productBrand;
    private String productCategory;

    // Constructors used in initializing product information
    StoreDataInFile(String pName, String pPrice, String pBrand, String pCategory) {
        productName = pName;
        productPrice = pPrice;
        productBrand = pBrand;
        productCategory = pCategory;
    }

    // Static method to get the file path for a given file name
    public static String getFilePath(String fileName) {
        // Getting  present directories
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        // Creating the way for a directory where files will be stored
        Path dirpath = Paths.get(currentPath.toString(), "assets");

        // Concatenate the directory way and file name to get the complete file path
        return dirpath + "/" + fileName;
    }

    // Static method to delete the file if it exists
    public static void deleteIfFileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    // Static method to validate input (accepts only alphabets and spaces)
    public static boolean isValidInput(String input) {

        return input.matches("^([a-zA-Z&]+(\\s[a-zA-Z&]+)*)$");

    }

    // Method to format product data in CSV format
    public String getDataInCsvFormat() {
        return productName.replace(",", "") + " , " +
                productPrice.replace(",", "") + " , " +
                productBrand.replace(",", "") + " , " +
                productCategory.replace(",", "") + "\n";
    }

    // functions used to save product in given CSV file
    public void saveDataToCsv(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Write the formatted data to the file
            writer.write(this.getDataInCsvFormat());
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
}
