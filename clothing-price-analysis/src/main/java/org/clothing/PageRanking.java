package org.clothing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.clothing.scraper.StoreDataInFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PageRanking {

    // Flag to track if header has been displayed
    private static boolean isHeaderDisplayed = false;

    // Variable to store the file with max occurrences
    private static String fileWithMaxOccurrences = "";

    // Main method to start the page ranking process
    public static void main(String[] args) {
        handlePageRanking();
    }

    // Method to handle the page ranking process
    public static void handlePageRanking() {
        try {
            // Array of CSV file paths
            String[] csvFiles = {
                    StoreDataInFile.getFilePath("Ajio.csv"),
                    StoreDataInFile.getFilePath("Myntra.csv"),
                    StoreDataInFile.getFilePath("Flipkart.csv")
            };

            // Reader to get input from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String keyword;
            do {
                // Prompt the user to enter the keyword to search for
                System.out.println("Enter the keyword to search for: ");
                keyword = reader.readLine().trim();
            } while (!isValidKeyword(keyword));

            // Map to store the max occurrences of keyword in each file
            Map<String, Integer> maxOccurrencesMap = new HashMap<>();

            // Iterate through each CSV file
            for (String csvFile : csvFiles) {
                // Get the page ranking for the keyword in the current file
                Map<String, Integer> pageRanking = getPageRanking(csvFile, keyword);
                // Display filtered page ranking for the current file
                displayFilteredPageRanking(pageRanking, csvFile);

                // Update max occurrences map
                for (Map.Entry<String, Integer> entry : pageRanking.entrySet()) {
                    int occurrences = entry.getValue();
                    maxOccurrencesMap.put(entry.getKey(), Math.max(maxOccurrencesMap.getOrDefault(entry.getKey(), 0), occurrences));
                }
            }

            // Find file with max occurrences of the keyword
            int maxOccurrences = 0;
            for (Map.Entry<String, Integer> entry : maxOccurrencesMap.entrySet()) {
                if (entry.getValue() > maxOccurrences) {
                    maxOccurrences = entry.getValue();
                    fileWithMaxOccurrences = getFileName(entry.getKey());
                }
            }

            // Display the file with max occurrences of the keyword
            if (!fileWithMaxOccurrences.isEmpty()) {
                System.out.println("The word '" + keyword + "' has the maximum occurrences of " + maxOccurrences + " in the file: " + fileWithMaxOccurrences);
            } else {
                System.out.println("No data found for the specified keyword.");
            }

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    // Method to get the page ranking for the keyword in a CSV file
    public static Map<String, Integer> getPageRanking(String filePath, String keyword) throws IOException {
        Map<String, Integer> keywordOccurrences = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for (String cellValue : nextLine) {
                    // Check if the cell value contains the keyword
                    if (cellValue.toLowerCase().contains(keyword.toLowerCase())) {
                        // Increment occurrences count for the file
                        keywordOccurrences.put(filePath, keywordOccurrences.getOrDefault(filePath, 0) + 1);
                        break;
                    }
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return keywordOccurrences;
    }

    // Method to display filtered page ranking for a file
    public static void displayFilteredPageRanking(Map<String, Integer> pageRanking, String fileName) {
        if (pageRanking.isEmpty()) {
            // Display message if no data found for the keyword in the file
            System.out.println("No data found for the specified keyword in file: " + getFileName(fileName));
        } else {
            // Display header if not displayed yet
            if (!isHeaderDisplayed) {
                System.out.println("\n-----------------------------------------");
                System.out.println("Page Ranking based on the provided input:");
                System.out.println("-----------------------------------------");
                isHeaderDisplayed = true;
            }
            // Display occurrences for the file
            for (Map.Entry<String, Integer> entry : pageRanking.entrySet()) {
                System.out.println("File: " + getFileName(fileName) + ", Occurrences: " + entry.getValue());
            }
        }
    }

    // Method to extract filename from the full file path
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    // Method to validate the keyword entered by the user
    public static boolean isValidKeyword(String keyword) {
        // Check if the keyword contains only alphabets and spaces
        if (!keyword.matches("[a-zA-Z ]+")) {
            System.err.println("Invalid keyword. Please enter alphabets and spaces only.");
            return false;
        }
        return true;
    }
}
