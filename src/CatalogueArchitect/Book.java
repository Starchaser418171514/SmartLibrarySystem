package CatalogueArchitect;

public class Book {
    private long isbn;  // Used as the key for the BST
    private String title;
    private String author;

    // NEW FEATURES: Availability Status Flags
    private boolean isBorrowed;

    public Book(long isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;  // Default to available when a book is created
    }

    public long getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }

    // Getters and setters for the new status fields
    public boolean getIsBorrowed() { return isBorrowed; }
    public void setIsBorrowed(boolean borrowed) { this.isBorrowed = borrowed; }
    
    @Override
    public String toString() {
        // Updated to show book status in the catalogue display
        return String.format("[ISBN: %d] \"%s\" by %s", isbn, title, author);
    }
    
}