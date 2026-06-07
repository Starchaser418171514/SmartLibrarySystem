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
    private BookNode root;  //declare root node of BST
    private static final String CATALOGUE_FILE = "book_catalogue.txt";  // sava available book
    private static final String ALL_BOOKS_FILE = "allBooks.txt"; //save all book data

    public LibraryCatalogue() {
        // Automatically load existing books from the file when the catalogue is initialized
        loadCatalogueFromFile();
    }

    //method to prevent registering the exact same physical book twice
    private boolean isbnDuplicated(long isbn) {
        File file = new File(ALL_BOOKS_FILE);
        if (!file.exists()) return false;   //check if file exist

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(","); // split line into tokens separated by ,
                // check if the first token matches the new isbn
                if (tokens.length >= 1 && Long.parseLong(tokens[0].trim()) == isbn) {
                    return true; 
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Warning: Could not read permanent global tracking database.");
        }
        return false;
    }

    //method to add book
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

        // if isbn havent exist,add the book
        if (!isbnDuplicated(isbn)) {
            Book newBook = new Book(isbn, title, author);
            root = insertRecursive(root, newBook);
            
            saveBookToFiles(newBook, false);    // save book to both text file 
            
            System.out.println("Success: \"" + title + "\" added to both active and permanent catalogs.");
        } else {
            System.out.println("Error: An available copy with ISBN " + isbn + " already exists in the library shelf records.");
        }
    }

    // Inserts a new Book object into the tree.
    public void insertBookObject(Book book) {
        root = insertRecursive(root, book);
    }

    // traverses BST to find correct spot for new book
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

    // remove book from BST when book is borrowed
    public void removeBook(long isbn) {
        root = deleteRecursive(root, isbn); //call deleteRecursive method 
        updateActiveCatalogueFileState();   // Overwrites book_catalogue.txt to remove the borrowed book
        updatePermanentRegistryFileState(isbn, true); // Updates book status to true (Borrowed) in allBooks.txt
    }

    //BST deletion logic
    private BookNode deleteRecursive(BookNode current, long isbn) {
        if (current == null) return null;

        if (isbn < current.book.getIsbn()) {
            current.left = deleteRecursive(current.left, isbn);
        } else if (isbn > current.book.getIsbn()) {
            current.right = deleteRecursive(current.right, isbn);
        } else {
            // deleting a node with zero or one child
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            // deleting a node with two children
            current.book = findSmallest(current.right);
            current.right = deleteRecursive(current.right, current.book.getIsbn());
        }
        return current;
    }

    //find smallest in BST
    private Book findSmallest(BookNode root) {
        Book smallest = root.book;
        while (root.left != null) {
            smallest = root.left.book;
            root = root.left;
        }
        return smallest;
    }

    // find book in BST
    public Book findBook(long isbn) {
        return searchRecursive(root, isbn);
    }

    // recursively searches the BST for a specific ISBN
    private Book searchRecursive(BookNode current, long isbn) {
        if (current == null) return null;   //reach dead end
        if (isbn == current.book.getIsbn()) return current.book; //found exact match

        return isbn < current.book.getIsbn() //if target isbn < current isbn, go left, else go right
            ? searchRecursive(current.left, isbn) 
            : searchRecursive(current.right, isbn);
    }

    //print all available book
    public void displayAllBooks() {
        if (root == null) {
            System.out.println("The library catalogue is currently empty.");
            return;
        }
        inOrderTraversal(root);
    }

    // travels BST in-order and print in ascending order
    private void inOrderTraversal(BookNode node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.book);    
            inOrderTraversal(node.right);
        }
    }

    // Appends a newly added book to both file
    private void saveBookToFiles(Book book, boolean isBorrowed) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATALOGUE_FILE, true))) {
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor());
        } catch (IOException e) {
            System.out.println("Warning: Unable to save to active catalogue file.");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ALL_BOOKS_FILE, true))) {
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor() + "," + isBorrowed);
        } catch (IOException e) {
            System.out.println("Warning: Unable to save to permanent catalogue file.");
        }
    }

    //Loads saved books from book_catalogue.txt back into the tree
    private void loadCatalogueFromFile() {
        File file = new File(CATALOGUE_FILE);
        if (!file.exists()) return;  //exit if no file to load

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");  // Break the line apart at ,
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

    // Saves the current state of book tree back into book_catalogue.txt
    public void updateActiveCatalogueFileState() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CATALOGUE_FILE))) {
            writeTreeToActiveFile(root, writer);
        } catch (IOException e) {
            System.out.println("Error saving updated states to active catalogue file.");
        }
    }

    // Recursively goes through the tree to write every book to the file in order
    private void writeTreeToActiveFile(BookNode node, PrintWriter writer) {
        if (node != null) {
            writeTreeToActiveFile(node.left, writer);
            writer.println(node.book.getIsbn() + "," + node.book.getTitle() + "," + node.book.getAuthor());
            writeTreeToActiveFile(node.right, writer);
        }
    }

    
    //Updates a specific book's status
    public void updatePermanentRegistryFileState(long targetIsbn, boolean statusToSet) {
        File file = new File(ALL_BOOKS_FILE);
        if (!file.exists()) return;

        List<String> memoryBuffer = new ArrayList<>();
        boolean updated = false;    // flag to ensure only update 1 copy

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4 && Long.parseLong(tokens[0].trim()) == targetIsbn && !updated) {
                    memoryBuffer.add(tokens[0] + "," + tokens[1] + "," + tokens[2] + "," + statusToSet);
                    updated = true; 
                } else {
                    memoryBuffer.add(line); // if not the one just copy back
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating transactional rows inside permanent registry tracking file.");
            return;
        }

        // write the completely updated list back into the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : memoryBuffer) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving updated modifications back to permanent registry tracking file.");
        }
    }
}