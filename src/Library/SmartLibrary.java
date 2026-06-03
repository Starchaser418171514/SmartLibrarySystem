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
            // Check if the book is borrowed 
            if (book.getIsBorrowed()) {
                System.out.println("Denial: Blocked processing. \"" + book.getTitle() + "\" is currently borrowed.");
                return false;
            }
            
            // Mark as borrowed and save to the history stack file
            book.setIsBorrowed(true);
            borrowHistory.addBorrowedBook(studentId, book);
            
            // NEW: Update the catalogue file so it remembers this book is now borrowed
            catalogue.updateCatalogueFileState();
            return true;
        }

        System.out.println("Error: Book with ISBN " + isbn + " does not belong to this library.");
        return false;
    }

    // NEW FEATURE: Handles returning a book back into the system
    public boolean returnBook(String studentId, long isbn) {
        Book book = findBook(isbn);
        
        if (book != null) {
            // Validation: Make sure the book is borrowed before allowing return
            if (!book.getIsBorrowed()) {
                System.out.println("Error: \"" + book.getTitle() + "\" is already available in the library.");
                return false;
            }
            
            // Revert availability states
            book.setIsBorrowed(false);
        
            // Update the text file history state
            borrowHistory.returnBookInFile(studentId, isbn);

            // NEW: Update the catalogue file so it remembers this book is available again
            catalogue.updateCatalogueFileState();

            System.out.println("Success: \"" + book.getTitle() + "\" has been returned and is available again!");
            return true;
        }
        System.out.println("Error: Book with ISBN " + isbn + " does not belong to this library.");
        return false;
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
}



