package SmartLibrarySystem.src.Library;

import java.util.Stack;
import SmartLibrarySystem.CatalogueArchitect;
public class BorrowStack {

    private Stack <Book> history;

    // Constructor
    public BorrowStack() {
        history = new Stack <>();
    }

    // Add borrowed book to history
    public void addBorrowedBook(Book book) {
        history.push(book);
        System.out.println("Book added to borrowing history.");
    }

    // Display borrowing history
    public void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No borrowing history available.");
            return;
        }

        System.out.println("\n------ Borrowing History (Most Recent First) ------\n");
        System.out.printf("%-10s | %-20s | %-20s\n", "ISBN", "Title", "Author");
        System.out.println("---------------------------------------------------");
        
        for (int i = history.size() - 1; i >= 0; i--) {
            Book b = history.get(i);
            System.out.printf("%-10d | %-20s | %-20s\n", b.isbn, b.title, b.author);
            System.out.println("---------------------------------------------------");
        }
    }
    
}
