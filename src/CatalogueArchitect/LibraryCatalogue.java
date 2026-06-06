package CatalogueArchitect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LibraryCatalogue {
    private BookNode root;
    private static final String CATALOGUE_FILE = "book_catalogue.txt";  // sava available book
    private static final String ALL_BOOKS_FILE = "allBooks.txt"; //save all book data

    public LibraryCatalogue() {
        // Automatically load existing books from the file when the catalogue is initialized
        loadCatalogueFromFile();
    }

    /**
     * STRICT UNIQUE VALIDATION: Checks the permanent global registry file.
     * Returns true if the ISBN exists ANYWHERE in the library archive, 
     * completely blocking duplicates even if the book is currently borrowed.
     */
    private boolean isBookCurrentlyAvailableInLibrary(long isbn) {
        File file = new File(ALL_BOOKS_FILE);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                // STRIP THE BOOLEAN CHECK: If the ISBN matches, it exists in the library!
                if (tokens.length >= 1 && Long.parseLong(tokens[0].trim()) == isbn) {
                    return true; // Stop immediately; this ISBN is already registered
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Warning: Could not read permanent global tracking database.");
        }
        return false;
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

        // Validate against the permanent tracking file status
        if (!isBookCurrentlyAvailableInLibrary(isbn)) {
            Book newBook = new Book(isbn, title, author);
            root = insertRecursive(root, newBook);
            
            // UNIFIED METHOD CALL: Writes to both storage files simultaneously upon addition
            saveBookToFiles(newBook, false); 
            
            System.out.println("Success: \"" + title + "\" added to both active and permanent catalogs.");
        } else {
            System.out.println("Error: An available copy with ISBN " + isbn + " already exists in the library shelf records.");
        }
    }

    // Direct insertion method used for loading from file or re-inserting returns
    public void insertBookObject(Book book) {
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

    /**
     * PHYSICAL DELETION LOGIC: Removes a node entirely from the Binary Search Tree
     * and synchronizes the change to both storage tracking files.
     */
    public void removeBook(long isbn) {
        root = deleteRecursive(root, isbn);
        updateActiveCatalogueFileState();             // Overwrites active file to remove the borrowed book from active catalogue
        updatePermanentRegistryFileState(isbn, true); // Updates state line to true (Borrowed) in global file
    }

    private BookNode deleteRecursive(BookNode current, long isbn) {
        if (current == null) return null;

        if (isbn < current.book.getIsbn()) {
            current.left = deleteRecursive(current.left, isbn);
        } else if (isbn > current.book.getIsbn()) {
            current.right = deleteRecursive(current.right, isbn);
        } else {
            // Node found: handle deletion cases
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            current.book = findSmallest(current.right);
            current.right = deleteRecursive(current.right, current.book.getIsbn());
        }
        return current;
    }

    private Book findSmallest(BookNode root) {
        Book smallest = root.book;
        while (root.left != null) {
            smallest = root.left.book;
            root = root.left;
        }
        return smallest;
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
     * UNIFIED FILE SAVING METHOD: Handles writing to both tracking files simultaneously
     */
    private void saveBookToFiles(Book book, boolean isBorrowed) {
        // 1. Write the clean 3-column format to the active catalogue file
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATALOGUE_FILE, true))) {
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor());
        } catch (IOException e) {
            System.out.println("Warning: Unable to save to active catalogue file.");
        }

        // 2. Write the 4-column status-tracked format to the permanent catalogue file
        try (PrintWriter writer = new PrintWriter(new FileWriter(ALL_BOOKS_FILE, true))) {
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor() + "," + isBorrowed);
        } catch (IOException e) {
            System.out.println("Warning: Unable to save to permanent catalogue file.");
        }
    }

    /**
     * SYSTEM INITIALIZATION LOADER: Rebuilds the active available BST on boot using the clean 3-column records (active catalogue file)
     */
    private void loadCatalogueFromFile() {
        File file = new File(CATALOGUE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 3) {
                    long isbn = Long.parseLong(tokens[0]);
                    Book book = new Book(isbn, tokens[1], tokens[2]);
                    insertBookObject(book);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Notice: Error parsing active catalogue file registry.");
        }
    }

    /**
     * ACTIVE TRACK DATABASE SYNCHRONIZER: Wipes and completely overwrites active catalogue file entries 
     * using the current remaining in-memory live tree structure state
     */
    public void updateActiveCatalogueFileState() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATALOGUE_FILE))) {
            writeTreeToActiveFile(root, writer);
        } catch (IOException e) {
            System.out.println("Error saving updated states to active catalogue file.");
        }
    }

    private void writeTreeToActiveFile(BookNode node, PrintWriter writer) {
        if (node != null) {
            writeTreeToActiveFile(node.left, writer);
            writer.println(node.book.getIsbn() + "," + node.book.getTitle() + "," + node.book.getAuthor());
            writeTreeToActiveFile(node.right, writer);
        }
    }

    /**
     * PERMANENT TRACK DATABASE SYNCHRONIZER: Sifts through and flips state values only for lines 
     * matching our current ISBN targets inside your permanent text registry database
     */
    public void updatePermanentRegistryFileState(long targetIsbn, boolean statusToSet) {
        File file = new File(ALL_BOOKS_FILE);
        if (!file.exists()) return;

        List<String> memoryBuffer = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4 && Long.parseLong(tokens[0].trim()) == targetIsbn && !updated) {
                    memoryBuffer.add(tokens[0] + "," + tokens[1] + "," + tokens[2] + "," + statusToSet);
                    updated = true; // Protect duplicate copy profiles by shifting only the targeted instance entry
                } else {
                    memoryBuffer.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating transactional rows inside permanent registry tracking file.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : memoryBuffer) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving updated modifications back to permanent registry tracking file.");
        }
    }
}