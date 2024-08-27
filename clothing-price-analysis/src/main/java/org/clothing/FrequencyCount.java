package org.clothing;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FrequencyCount {

    public static void countFrequency(Scanner scanner) {



            System.out.println("Enter brand name / clothes category / product name .");
            String wordToSearch = scanner.nextLine().trim().toLowerCase();

            // Check if the input contains only letters
            if (!wordToSearch.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid input. Please enter letters only.");
                return; // Exit the program
            }

            // Check if the word exists in the stored data
            int frequency = searchFrequency(wordToSearch, "assets\\Flipkart.csv");
            frequency += searchFrequency(wordToSearch, "assets\\Myntra.csv");
            frequency += searchFrequency(wordToSearch, "assets\\Ajio.csv");

            if (frequency > 0) {
                System.out.println("Frequency of '" + wordToSearch + "': " + frequency);
            } else {
                System.out.println("The word '" + wordToSearch + "' was not found in the stored data.");
            }
        }




    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        countFrequency(scanner);
        scanner.close();
    }

    // Method to search for the frequency of a word in product titles
    private static int searchFrequency(String word, String csvFile) {
        int frequency = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Separate all words from the line
                String[] words = line.split("[^a-zA-Z ]+");
                for (String w : words) {
                    // Convert word to lowercase for case-insensitive comparison
                    String lowerCaseWord = w.trim().toLowerCase();
                    if (lowerCaseWord.equals(word)) {
                        // Increment frequency count
                        frequency++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return frequency;
    }


}
