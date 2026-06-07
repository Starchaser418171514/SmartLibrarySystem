package Library;

import CatalogueArchitect.Book;

public interface LibraryInterface {
    void addBook(long isbn, String title, String author);  
    boolean borrowBook(String studentId, long isbn); 
    void viewLatestHistory(String studentId);        
    Book findBook(long isbn);
}