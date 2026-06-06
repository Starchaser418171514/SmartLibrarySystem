package Library;

import CatalogueArchitect.*;

public class SmartLibrary implements LibraryInterface {
    private LibraryCatalogue catalogue;
    private BorrowStack borrowHistory;

    public SmartLibrary() {
        this.catalogue = new LibraryCatalogue();
        this.borrowHistory = new BorrowStack();
    }

    /**
     * Function to add a book to the library's catalogue
     * 
     * @param isbn   The unique ISBN number of the book
     * @param title  The title of the book
     * @param author The author of the book
     */
    public void addBook(long isbn, String title, String author) {
        catalogue.addBook(isbn, title, author);
    }

    /**
     * Function to borrow a book from the library
     * 
     * @param isbn The unique ISBN number of the book to borrow
     * @return true if the book was successfully borrowed, false otherwise
     */
    public boolean borrowBook(String studentId, long isbn) {
        Book book = findBook(isbn);
        
        if (book != null) {
            // 1. Mark status and append to user text file stack
            book.setIsBorrowed(true);
            borrowHistory.addBorrowedBook(studentId, book);
            
            // 2. Physically remove the book node from the tree
            catalogue.removeBook(isbn);
            System.out.println("Success: Book has been physically checked out and removed from the active catalogue.");
            return true;
        }

        System.out.println("Error: Book with ISBN " + isbn + " is not available in the catalogue. ");
        return false;
    }

    // Upgraded Admin Logic: Recovers book data from borrower's history file and re-inserts it back into the BST catalogue
    public boolean returnBook(String studentId, long isbn) {        
        // Find the book data inside the student's file history since it doesn't exist in the tree anymore
        Book bookContext = borrowHistory.findBookInHistory(studentId, isbn);
        
        if (bookContext != null) {
            // Revert availability flag state
            bookContext.setIsBorrowed(false);
        
            // 1. Physically insert the book node back into the Binary Search Tree
            catalogue.insertBookObject(bookContext);
            catalogue.updateActiveCatalogueFileState(); // Sync up main text database file

            // 2. Mark the row status as "Returned" inside the student's personal history file
            borrowHistory.returnBookInFile(studentId, isbn);

            System.out.println("Success: \"" + bookContext.getTitle() + "\" has been returned and re-inserted into the active catalogue!");
            return true;
        } else {
            System.out.println("Error: No active matching borrow record found for ISBN " + isbn + " under Student ID: " + studentId);
            return false;
        }
    }

    /**
     * Function to view the latest borrowing history
     */
    public void viewLatestHistory(String studentId) {
        borrowHistory.showHistory(studentId);
    }

    /**
     * Function to find a book in the library's catalogue
     * 
     * @param isbn The unique ISBN number of the book to find
     * @return The Book object if found, null otherwise
     */
    public Book findBook(long isbn) {
        return catalogue.findBook(isbn);
    }

    // Expose catalog printing to system view loops
    public void displayAllCatalogBooks() {
        catalogue.displayAllBooks();
    }

    /**
     * NEW SEARCH HELPER: Scans the permanent global file to determine 
     * if a missing BST book is borrowed or completely non-existent.
     * Returns "Borrowed" if owned but out on loan, or "Not Owned" if missing completely.
     */
    public String checkPermanentRegistry(long isbn) {
        java.io.File file = new java.io.File("allBooks.txt");
        if (!file.exists()) return "Not Owned";

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4 && Long.parseLong(tokens[0].trim()) == isbn) {
                    boolean isBorrowed = Boolean.parseBoolean(tokens[3].trim());
                    if (isBorrowed) {
                        return "Borrowed"; // Found in global records, and it's out on loan
                    }
                }
            }
        } catch (java.io.IOException | NumberFormatException e) {
            System.out.println("Warning: Error accessing global history registry.");
        }
        return "Not Owned";
    }
}



