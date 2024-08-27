package org.clothing;

import org.clothing.scraper.Ajio;
import org.clothing.scraper.Flipkart;
import org.clothing.scraper.Myntra;
import org.clothing.scraper.StoreDataInFile;
import java.util.InputMismatchException;

import java.util.Scanner;

import static org.clothing.SearchFrequency.wordFrequencyMap;

public class Main {


    public static void collectAndProcessInput(Scanner scanner) {
        String category;
        do {
            System.out.println("\nEnter a clothing category\nRemember, no numbers or hitting 'Enter' like it owes you money! Just letters, please:");
            category = scanner.nextLine().trim();
        } while (!StoreDataInFile.isValidInput(category));

        category = SpellChecker.checkSpelling(scanner, category);

        // Increment the count of entered category in word frequency map
        wordFrequencyMap.put(category, wordFrequencyMap.getOrDefault(category, 0) + 1);

        // Perform web scraping based on the entered category
        webScrapping(category);

        // Update the CSV file with the new word frequency
        SearchFrequency.saveWordFrequencyToCSV();

    }



    public static void webScrapping(String category) {
        Myntra.handleCrawling(category);
        Flipkart.handleCrawling(category);
        Ajio.handleCrawling(category);
    }


    public static void showTopSearchedResult() {
        SearchFrequency.displayTopWords();
    }

    public static void checkFrequency(Scanner scanner) {
        FrequencyCount.countFrequency(scanner);
    }

    public static void invertedIndexing(Scanner scanner) {
        InvertedIndexing.findInvertedIndexing(scanner);
    }


    public static void pageRanking() {
        PageRanking.handlePageRanking();
    }

    public static void wordCompletion(Scanner scanner) {
        WordCompletion.wordCompletion(scanner);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Displaying the top categories based on word frequencies
        System.out.println("\n" +
                "  _    _                  _    _                       _     _____                        _____   _____    ____   _____    ____  \n" +
                " | |  | |                | |  | |                     | |   |_   _|                      |  __ \\ |  __ \\  / __ \\ |  __ \\  / __ \\ \n" +
                " | |__| |  ___  _   _    | |_ | |__    ___  _ __  ___ | |     | |     __ _  _ __ ___     | |__) || |__) || |  | || |  | || |  | |\n" +
                " |  __  | / _ \\| | | |   | __|| '_ \\  / _ \\| '__|/ _ \\| |     | |    / _` || '_ ` _ \\    |  ___/ |  _  / | |  | || |  | || |  | |\n" +
                " | |  | ||  __/| |_| |   | |_ | | | ||  __/| |  |  __/|_|    _| |_  | (_| || | | | | |   | |     | | \\ \\ | |__| || |__| || |__| |\n" +
                " |_|  |_| \\___| \\__, |    \\__||_| |_| \\___||_|   \\___|(_)   |_____|  \\__,_||_| |_| |_|   |_|     |_|  \\_\\ \\____/ |_____/  \\____/ \n" +
                "                 __/ |                                                                                                           \n" +
                "                |___/                                                                                                            \n");
        System.out.println(">>> YOUR OWN PRODUCT ANALYZER.");
        System.out.println(">>> I BRING YOU THE BEST INFO IN BEST POSSIBLE WAY FROM MYNTRA.COM, FLIPKART.COM & AJIO.COM");



        // Features menu
        System.out.println(">>> HERE'S WHAT I CAN DO. CHOOSE ANY ONE AND SEE HOW I WORK - LIGHTNING SPEEEEEEED!\uD83D\uDCA8 \uD83D\uDCA8");
        while (true) {
            // Features menu
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1  \uD83D\uDD0D Display Top Searches by User");
            System.out.println("2. \uD83D\uDCC4 Start Scrapping");
            System.out.println("3. \uD83D\uDD24 Word Completion");
            System.out.println("4. #\uFE0F⃣ Check Frequency");
            System.out.println("5. \uD83C\uDF10 Inverted Indexing");
            System.out.println("6. \uD83D\uDCC3 Page Ranking");
            System.out.println("7. \uD83D\uDC4B Give me exit PRODO!! I'm done");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

            System.out.print("Enter the number of the feature you want to run: ");

            int choice = 0;
            boolean validInput = false;
            while (!validInput) {
                String input = scanner.nextLine();
                try {
                    choice = Integer.parseInt(input);
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Error: Please enter a valid integer.");
                    System.out.print("Enter the number of the feature you want to run: ");
                }
            }

            switch (choice) {
                case 1:
                    showTopSearchedResult();
                    break;
                case 2:
                    collectAndProcessInput(scanner);
                    break;
                case 3:
                    wordCompletion(scanner);
                    break;
                case 4:
                    checkFrequency(scanner);
                    break;
                case 5:
                    invertedIndexing(scanner);
                    break;
                case 6:
                    pageRanking();
                    break;
                case 7:
                    System.out.println("Exiting program... ");
                    System.out.println("ON A SERIOUS NOTE - You made me consume a lot of water. Save Environment. Please.\uD83D\uDCA7 \uD83D\uDCA7");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
}






}
