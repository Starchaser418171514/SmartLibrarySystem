package CatalogueArchitect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LibraryCatalogue {
    private BookNode root;
    private static final String CATALOGUE_FILE = "book_catalogue.txt";

    public LibraryCatalogue() {
        // Automatically load existing books from the file when the catalogue is initialized
        loadCatalogueFromFile();
    }

    public void addBook(long isbn, String title, String author) {
        // Validation Checks
        if (isbn <= 0) {
            System.out.println("Validation Failure: ISBN must be a positive number.");
            return;
        }
        
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Validation Failure: Title cannot be null or empty.");
            return;
        }
        if (author == null || author.trim().isEmpty()) {
            System.out.println("Validation Failure: Author cannot be null or empty.");
            return;
        }

        // Only write to the text file if the book doesn't already exist in our tree
        if (findBook(isbn) == null) {
            Book newBook = new Book(isbn, title, author);
            root = insertRecursive(root, newBook);
            saveBookToFile(newBook); // Save to file database instantly
            System.out.println("Success: \"" + title + "\" added to catalog file.");
        } else {
            System.out.println("Error: Book with ISBN " + isbn + " already exists in system records.");
        }
    }

    /**
     * Helper for loading: Inserts books into the BST silently without rewriting them back to the file
     */
    private void insertFromFile(Book book) {
        root = insertRecursive(root, book);
    }

    private BookNode insertRecursive(BookNode current, Book book) {
        if (current == null) {
            return new BookNode(book);
        }

        if (book.getIsbn() < current.book.getIsbn()) {
            current.left = insertRecursive(current.left, book);
        } else if (book.getIsbn() > current.book.getIsbn()) {
            current.right = insertRecursive(current.right, book);
        } else {
            System.out.println("Error: Book with ISBN " + book.getIsbn() + " already exists.");
        }
        return current;
    }

    public Book findBook(long isbn) {
        return searchRecursive(root, isbn);
    }

    private Book searchRecursive(BookNode current, long isbn) {
        if (current == null) return null;
        if (isbn == current.book.getIsbn()) return current.book;

        return isbn < current.book.getIsbn() 
            ? searchRecursive(current.left, isbn) 
            : searchRecursive(current.right, isbn);
    }

    public void displayAllBooks() {
        if (root == null) {
            System.out.println("The library catalogue is currently empty.");
            return;
        }
        inOrderTraversal(root);
    }

    private void inOrderTraversal(BookNode node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.book);      // Print the title alongside its availability status
            inOrderTraversal(node.right);
        }
    }

    /**
     * EXTRA FEATURE: Instantly appends a newly created book to the catalogue file
     */
    private void saveBookToFile(Book book) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATALOGUE_FILE, true))) {
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getIsBorrowed());
        } catch (IOException e) {
            System.out.println("Warning: Unable to save book to catalogue file.");
        }
    }

    /**
     * EXTRA FEATURE: Reads the catalogue file line by line to completely rebuild the BST on startup
     */
    private void loadCatalogueFromFile() {
        File file = new File(CATALOGUE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    long isbn = Long.parseLong(tokens[0]);
                    String title = tokens[1];
                    String author = tokens[2];
                    boolean isBorrowed = Boolean.parseBoolean(tokens[3]);

                    Book book = new Book(isbn, title, author);
                    book.setIsBorrowed(isBorrowed);
                    
                    // Direct insertion to bypass the saveBookToFile loop
                    insertFromFile(book);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Notice: Resetting catalogue stream due to read errors.");
        }
    }

    /**
     * EXTRA FEATURE: Rewrites the file to save updated states (like when a book is borrowed or returned)
     */
    public void updateCatalogueFileState() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATALOGUE_FILE))) {
            // Traverse the tree and rewrite all books with their updated statuses
            writeTreeToFile(root, writer);
        } catch (IOException e) {
            System.out.println("Error saving updated book states to catalogue file.");
        }
    }

    private void writeTreeToFile(BookNode node, PrintWriter writer) {
        if (node != null) {
            writeTreeToFile(node.left, writer);
            writer.println(node.book.getIsbn() + "," + node.book.getTitle() + "," + node.book.getAuthor() + "," + node.book.getIsBorrowed());
            writeTreeToFile(node.right, writer);
        }
    }
}