package org.clothing;

import org.clothing.scraper.StoreDataInFile; // Import statement for a custom class

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class AVLNode {
    String word;
    Set<String> cellLocations; // Stores the locations of cells containing the word
    Set<Integer> lineNumbers; // Stores the line numbers where the word occurs

    int height; // Height of the nodes in the given AVL tree
    AVLNode left, right; //  way towards left to right node

    // Constructor to initialize the AVLNode with a word, cell location, and line number
    AVLNode(String word, String cellLocation, int lineNumber) {
        this.word = word;
        this.cellLocations = new HashSet<>();
        this.cellLocations.add(cellLocation);
        this.lineNumbers = new HashSet<>();
        this.lineNumbers.add(lineNumber);
        this.height = 1;
    }
}

class AVLTree {
    AVLNode root; // R00T node of the AVL tree

    // Method utilized to calculate the heights of nodes
    int height(AVLNode node) {
        if (node == null) return 0;
        return node.height;
    }

    // Method used to calculate the balancing nodes
    int balanceFactor(AVLNode node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    // Method used to perform a Right-sideed rotation on the AVL tree
    AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Method to perform a left rotation on the AVL tree
    AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Method to inserting a node in the AVL tree
    AVLNode insert(AVLNode node, String word, String cellLocation, int lineNumber) {
        if (node == null) return new AVLNode(word, cellLocation, lineNumber);

        if (word.compareTo(node.word) < 0) {
            node.left = insert(node.left, word, cellLocation, lineNumber);
        } else if (word.compareTo(node.word) > 0) {
            node.right = insert(node.right, word, cellLocation, lineNumber);
        } else {
            node.cellLocations.add(cellLocation);
            node.lineNumbers.add(lineNumber); // Store line number
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = balanceFactor(node);

        if (balance > 1 && word.compareTo(node.left.word) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && word.compareTo(node.right.word) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && word.compareTo(node.left.word) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && word.compareTo(node.right.word) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Method utilized for  performing an inorder-traversal given in AVL tree
    void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.println(node.word + ": " + node.cellLocations);
            inorder(node.right);
        }
    }
}

public class InvertedIndexing {

    // this is the Main method to initilize a program
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        findInvertedIndexing(scanner);
        scanner.close();
    }

    // Method to prompt the user to enter a word for inverted indexing
    public static void findInvertedIndexing(Scanner scanner) {
        System.out.println("Enter a word to locate in the CSV files (Inverted Indexing):");
        String searchWord = scanner.nextLine().trim().toLowerCase();
        // Check if the input is empty or contains only whitespace
        if (!searchWord.isEmpty()) {
            handleInvertedIndexing(searchWord);
        } else {
            System.out.println("Input cannot be empty or contain only whitespace.");
        }

    }

    // Method to handle inverted indexing for a given search word
    public static void handleInvertedIndexing(String searchWord) {
        String[] csvFiles = {
                "Ajio.csv", "Flipkart.csv", "Myntra.csv"
        };
        for (String fileName : csvFiles) {
            String filePath = StoreDataInFile.getFilePath(fileName);
            AVLTree avlTree = buildInvertedIndex(filePath);

            AVLNode node = search(avlTree.root, searchWord);
            System.out.println("-------------------------------------------------\n" + " >>> " +
                    fileName + ": Searching \"" + searchWord + "\"\n" +
                    "-------------------------------------------------");
            if (node != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    int lineNumber = 1;
                    while ((line = reader.readLine()) != null) {
                        if (node.lineNumbers.contains(lineNumber)) {
                            System.out.println("Line " + lineNumber + ": " + line);
                        }
                        lineNumber++;
                    }
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Word '" + searchWord + "' not found in the " + fileName + " file\n");
            }
        }
    }

    // Method to build an inverted index from a CSV file
    public static AVLTree buildInvertedIndex(String filePath) {
        AVLTree avlTree = new AVLTree();
        int lineNumber = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(","); // Assuming CSV uses comma as delimiter, adjust if needed
                for (String cellContent : cells) {
                    if (cellContent != null && !cellContent.isEmpty()) {
                        String[] words = cellContent.split("\\s+");
                        String cellLocation = Arrays.toString(cells); // You might need to adjust this based on how you want to get cell location
                        for (String word : words) {
                            word = word.toLowerCase();
                            avlTree.root = avlTree.insert(avlTree.root, word, cellLocation, lineNumber);
                        }
                    }
                }
                lineNumber++; // Increment line number after reading each line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return avlTree;
    }

    // functions used to searching in AVL tree
    static AVLNode search(AVLNode node, String word) {
        if (node == null || node.word.equals(word)) {
            return node;
        }

        if (word.compareTo(node.word) < 0) {
            return search(node.left, word);
        } else {
            return search(node.right, word);
        }
    }
}
