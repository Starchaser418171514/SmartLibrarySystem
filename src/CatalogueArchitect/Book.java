package CatalogueArchitect;

public class Book {
    private long isbn; 
    private String title;
    private String author;
    private boolean isBorrowed;


    public Book(long isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isBorrowed = false;  
    }
    // Getters and setters
    public long getIsbn(){return isbn;}
    public String getTitle(){return title;}
    public String getAuthor(){return author;}
    public boolean getIsBorrowed(){return isBorrowed;}
    public void setIsBorrowed(boolean borrowed){this.isBorrowed = borrowed;}

    @Override
    public String toString() {
        // display book information: isbn,title,author
        return String.format("[ISBN: %d] \"%s\" by %s", isbn, title, author);
    }
    
}