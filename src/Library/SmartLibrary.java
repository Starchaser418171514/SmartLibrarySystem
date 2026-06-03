package SmartLibrarySystem.src.Library;

import SmartLibrarySystem.src.CatalogueArchitect.*;

public class SmartLibrary implements LibraryInterface {
    private LibraryCatalogue catalogue;
    private BorrowStack borrowHistory;

    /**
     * Function to add a book to the library's catalogue
     * 
     * @param isbn   The unique ISBN number of the book
     * @param title  The title of the book
     * @param author The author of the book
     */
    public void addBook(int isbn, String title, String author) {
        catalogue.addBook(isbn, title, author);
    }

    /**
     * Function to borrow a book from the library
     * 
     * @param isbn The unique ISBN number of the book to borrow
     * @return true if the book was successfully borrowed, false otherwise
     */
    public boolean borrowBook(int isbn) {
        Book book = findBook(isbn);
        if (book != null) {
            borrowHistory.addBorrowedBook(book);
            return true;
        }
        return false;
    }

    /**
     * Function to view the latest borrowing history
     */
    public void viewLatestHistory() {
        borrowStack.showHistory();
    }

    /**
     * Function to find a book in the library's catalogue
     * 
     * @param isbn The unique ISBN number of the book to find
     * @return The Book object if found, null otherwise
     */
    public Book findBook(int isbn) {
        return catalogue.findBook(isbn);
    }
}
