package org.clothing;

import org.clothing.scraper.StoreDataInFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SearchFrequency {
    // Map for storing words occurences
    static final Map<String, Integer> wordFrequencyMap = new HashMap<>();
    // Path utilized for CSV file to store word frequencies
    private static final String CSV_FILE = StoreDataInFile.getFilePath("SearchFrequency.csv");

    // Method to save word frequencies to CSV filings
    public static void saveWordFrequencyToCSV() {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            // Writing word frequencies to CSV file
            for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                writer.append(entry.getKey()).append(",").append(String.valueOf(entry.getValue())).append("\n");
            }
//            System.out.println("Word frequencies saved to " + CSV_FILE + " successfully.");
        } catch (IOException e) {
            // Handle IOException
            System.out.println("An error occurred while writing to the CSV file: " + e.getMessage());
        }
    }

    // Method to display top words searched by users
    public static void displayTopWords() {
        // Reading word frequencies from the CSV file
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // Handle IOException
                System.out.println("An error occurred while creating the CSV file: " + e.getMessage());
                return;
            }
        }

        System.out.println("\nTop categories searched by users:");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Splitting the line into word and frequency
                String[] parts = line.split(",");
                String word = parts[0];
                int frequency = Integer.parseInt(parts[1]);
                wordFrequencyMap.put(word, frequency);
            }
        } catch (IOException e) {
            // Handle IOException
            System.out.println("An error occurred while reading from the CSV file: " + e.getMessage());
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Handle ArrayIndexOutOfBoundsException
            System.out.println("No Frequent Search found");
        }

        // Displaying top 3 words based on frequency
        wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}
