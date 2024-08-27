package org.clothing;

import org.clothing.scraper.StoreDataInFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children; // Map to store children nodes
    boolean isEndOfWord; // Flag to mark end of a word

    // Constructor
    public TrieNode() {
        children = new HashMap<>(); // Initialize children map
        isEndOfWord = false; // Set end of word flag to false by default
    }
}

public class WordCompletion {
    private static final String[] CSV_FILE_PATHS = {
            StoreDataInFile.getFilePath("Ajio.csv"),
            StoreDataInFile.getFilePath("Myntra.csv"),
            StoreDataInFile.getFilePath("Flipkart.csv")
    };
    private TrieNode root; // Root node of the Trie
    private List<Map<String, Integer>> wordFrequencyMaps; // List to store word frequency maps for each CSV file

    // Constructor
    public WordCompletion() {
        root = new TrieNode(); // Initialize root node of the Trie
        wordFrequencyMaps = new ArrayList<>(); // Initialize list to store word frequency maps
    }

    // Method to handle word completion for user input
    public static void wordCompletion(Scanner scanner) {
        WordCompletion wordCompletion = new WordCompletion(); // Create WordCompletion object
        wordCompletion.buildWordFrequencyMaps(); // Build word frequency maps from CSV files
        wordCompletion.handleUserInput(scanner); // Handle user input for word completion
    }

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        wordCompletion(scanner); // Call wordCompletion method
        scanner.close();
    }

    // Method to build word frequency maps from CSV files
    public void buildWordFrequencyMaps() {
        for (String csvFileName : CSV_FILE_PATHS) {
            Map<String, Integer> wordFrequencyMap = new HashMap<>(); // Create word frequency map for each CSV file
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(","); // Split CSV line by comma

                    for (String item : data) {
                        String[] words = item.trim().split("\\s+"); // Split item into words

                        for (String word : words) {
                            if (!word.isEmpty()) { // Ignore empty words
                                wordFrequencyMap.put(word.toLowerCase(), wordFrequencyMap.getOrDefault(word.toLowerCase(), 0) + 1); // Update word frequency map
                                insertWord(word.toLowerCase()); // Insert word into Trie
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
            wordFrequencyMaps.add(wordFrequencyMap); // Add word frequency map to the list
        }
    }

    // Method to insert word into the Trie
    private void insertWord(String word) {
        TrieNode current = root; // Start from the root node
        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode()); // Add child node if not present
            current = current.children.get(c); // Move to the child node
        }
        current.isEndOfWord = true; // Mark end of word
    }

    // Method to find word completions for a given partial word
    public List<String> findWordCompletions(String partialWord) {
        List<String> completions = new ArrayList<>();
        TrieNode current = root; // Start from the root node

        // Traverse to the node corresponding to the last character of partialWord
        for (char c : partialWord.toCharArray()) {
            if (current.children.containsKey(c)) {
                current = current.children.get(c); // Move to the child node
            } else {
                return completions; // Return empty list if no completions found
            }
        }

        findAllCompletions(current, partialWord, completions); // Find all completions from the current node
        Collections.sort(completions); // Sort completions alphabetically
        return completions; // Return list of completions
    }

    // Method to find all word completions from a given Trie node
    private void findAllCompletions(TrieNode node, String prefix, List<String> completions) {
        if (node.isEndOfWord) {
            completions.add(prefix); // Add word completion to the list
        }

        // Recursively find completions for each child node
        for (char c : node.children.keySet()) {
            findAllCompletions(node.children.get(c), prefix + c, completions);
        }
    }

    // Method to handle user input for word completion
    public void handleUserInput(Scanner scanner) {
        // Define a regular expression pattern to match word characters
        String wordPattern = "^[a-zA-Z]+$";

        System.out.print("Enter partial word for completion: ");
        String partialWord = scanner.nextLine().trim().toLowerCase(); // Convert to lowercase for case-insensitive matching

        // Validate user input against the word pattern
        if (!partialWord.matches(wordPattern)) {
            System.out.println("Invalid input! Only word characters are allowed.");
        } else {
            List<String> completions = findWordCompletions(partialWord); // Find word completions
            if (completions.isEmpty()) {
                System.out.println("No completions found.");
            } else {
                System.out.println("Word Completions:");
                for (String completion : completions) {
                    System.out.println(completion); // Print word completions
                }
            }
        }
    }
}
