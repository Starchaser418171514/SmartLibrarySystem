package Library;

import CatalogueArchitect.*;

public class SmartLibrary implements LibraryInterface {
    private LibraryCatalogue catalogue;
    private BorrowStack borrowHistory;

    public SmartLibrary() {
        this.catalogue = new LibraryCatalogue();  
        this.borrowHistory = new BorrowStack();
    }

    //add book 
    public void addBook(long isbn, String title, String author) {
        catalogue.addBook(isbn, title, author);
    }

    // check out a book, log the history, and remove it from the active library tree
    public boolean borrowBook(String studentId, long isbn) {
        Book book = findBook(isbn);  //check if book exist in nook_catalogue.txt
        
        if (book != null) {
            book.setIsBorrowed(true); // updates book status to "borrowed"
            borrowHistory.addBorrowedBook(studentId, book); // save this checkout to student's personal text file
            
            // remove the book node from the tree
            catalogue.removeBook(isbn);
            System.out.println("Success: Book has been physically checked out and removed from the active catalogue.");
            return true;
        }

        System.out.println("Error: Book with ISBN " + isbn + " is not available in the catalogue. ");
        return false;
    }

    // processes book return, update the student's record and put the book back in the library
    public boolean returnBook(String studentId, long isbn) {        
        // find the book data from student's file history
        Book bookContext = borrowHistory.findBookInHistory(studentId, isbn);
        
        if (bookContext != null) {
            // mark the book as available
            bookContext.setIsBorrowed(false);
        
            //insert the book node back into the Binary Search Tree
            catalogue.insertBookObject(bookContext);
            catalogue.updateActiveCatalogueFileState(); // save the updated tree back to book_catalogue.txt

            // mark book status as "Returned" in student's personal history file
            borrowHistory.returnBookInFile(studentId, isbn);

            System.out.println("Success: \"" + bookContext.getTitle() + "\" has been returned and re-inserted into the active catalogue!");
            return true;
        } else {
            System.out.println("Error: No active matching borrow record found for ISBN " + isbn + " under Student ID: " + studentId);
            return false;
        }
    }

    //view the latest borrowing history
    public void viewLatestHistory(String studentId) {
        borrowHistory.showHistory(studentId);
    }

    //find a book in book_catalogue.txt
    public Book findBook(long isbn) {
        return catalogue.findBook(isbn);
    }

    // print the currently available books (in book_catalogue.txt)
    public void displayAllCatalogBooks() {
        catalogue.displayAllBooks();
    }

    // Scans allBooks.txt to check if a specific book is currently checked out
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