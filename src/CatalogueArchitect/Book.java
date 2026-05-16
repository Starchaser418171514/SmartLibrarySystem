package SmartLibrarySystem.src.CatalogueArchitect;

public class Book {
    private long isbn; // Used as the key for the BST
    private String title;
    private String author;

    public Book(long isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public long getIsbn() { return isbn; }
    
    @Override
    public String toString() {
        return String.format("[ISBN: %d] %s by %s", isbn, title, author);
    }
}
