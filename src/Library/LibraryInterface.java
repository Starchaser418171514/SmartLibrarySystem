package Library;

import CatalogueArchitect.Book;

public interface LibraryInterface {
    void addBook(long isbn, String title, String author);
    boolean borrowBook(String studentId, long isbn); // Updated for unique tracking
    void viewLatestHistory(String studentId);        // Updated for unique tracking
    Book findBook(long isbn);
}