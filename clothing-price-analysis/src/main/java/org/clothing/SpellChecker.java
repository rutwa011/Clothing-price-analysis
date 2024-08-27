package org.clothing;

import org.clothing.scraper.StoreDataInFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SpellChecker {
    // Edit distance threshold for suggesting corrections
    private static final Integer EDIT_DISTANCE_THRESHOLD = 4;
    // Path to the directory containing dictionary file
    static Path currentPath = Paths.get(System.getProperty("user.dir"));
    static Path dirpath = Paths.get(currentPath.toString(), "assets");
    // Path to the dictionary file
    private static final String DICTIONARY_PATH = dirpath + "/" + "dictionary.txt";
    // Set to store words from the dictionary
    private static Set<String> dictionary = new HashSet<>();

    // Constructor to load dictionary
    public SpellChecker() {
        dictionary = loadDictionary();
    }

    // Method to calculate edit distance between two strings
    private static int calculateEditDistance(String str1, String str2) {
        // Dynamic programming approach for calculating edit distance
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }

    // Main method for spell checking
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a word to spell check:");
        String word = scanner.nextLine().trim();
        SpellChecker obj = new SpellChecker();
        List<String> result = obj.checker(word);
        System.out.println(result);
        scanner.close();
    }

    // Method to perform spell checking for a category
    public static String checkSpelling(Scanner scanner, String category) {
        SpellChecker spellChecker = new SpellChecker();
        List<String> results = spellChecker.checker(category);

        while (!results.isEmpty()) {
            for (String ele : results) {
                System.out.println(ele);
            }

            do {
                // Prompt the user to enter a corrected category
                System.out.println("Enter a corrected category: ");
                category = scanner.nextLine().trim();
            } while (!StoreDataInFile.isValidInput(category));

            // Update the result list with the corrected category
            results = spellChecker.checker(category);
        }
        return category;
    }

    // Method to load dictionary from file
    private Set<String> loadDictionary() {
        Set<String> dictionary = new HashSet<>();
        File dictionaryFile = new File(DICTIONARY_PATH);

        // Check if the dictionary file exists
        if (!dictionaryFile.exists()) {
            try {
                // Create the dictionary file if it doesn't exist
                if (dictionaryFile.createNewFile()) {
                    System.out.println("Dictionary file created successfully.");
                } else {
                    System.err.println("Error: Unable to create dictionary file.");
                }
            } catch (IOException e) {
                System.err.println("Error occurred while creating dictionary file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        try (Scanner scanner = new Scanner(new File(DICTIONARY_PATH))) {
            // Read words from the dictionary file and add them to the set
            while (scanner.hasNextLine()) {
                dictionary.add(scanner.nextLine().trim().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Dictionary file not found!");
            e.printStackTrace();
        }

        return dictionary;
    }

    // Method to perform spell checking and suggest corrections
    public List<String> checker(String word) {
        String lowerCaseWord = word.toLowerCase();
        List<String> suggestions = getSuggestions(lowerCaseWord);
        if (!suggestions.isEmpty()) {
            System.out.println("Suggestions for \"" + word + "\": ");
        }
        return suggestions;
    }

    // Method to get suggestions for misspelled word
    public List<String> getSuggestions(String word) {
        List<String> suggestions = new ArrayList<>();
        for (String dictWord : dictionary) {
            // Calculate edit distance between the input word and dictionary words
            if (calculateEditDistance(word, dictWord) == 0) {
                return Collections.emptyList(); // Exact match found, no suggestions needed
            } else if (calculateEditDistance(word, dictWord) <= EDIT_DISTANCE_THRESHOLD) {
                suggestions.add(dictWord); // Add words within edit distance threshold as suggestions
            }
        }
        return suggestions;
    }
}
