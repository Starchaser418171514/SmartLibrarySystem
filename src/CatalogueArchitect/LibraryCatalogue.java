package SmartLibrarySystem.src.CatalogueArchitect;

public class LibraryCatalogue {
    private BookNode root;

    // Public method to add a book
    public void addBook(long isbn, String title, String author) {
        Book newBook = new Book(isbn, title, author);
        root = insertRecursive(root, newBook);
    }

    private BookNode insertRecursive(BookNode current, Book book) {
        if (current == null) {
            return new BookNode(book);
        }

        if (book.getIsbn() < current.book.getIsbn()) {
            current.left = insertRecursive(current.left, book);
        } else if (book.getIsbn() > current.book.getIsbn()) {
            current.right = insertRecursive(current.right, book);
        }
        // ISBNs must be unique; if equal, we do nothing (or update)
        return current;
    }

    // Public method to search for a book by ISBN
    public Book findBook(long isbn) {
        return searchRecursive(root, isbn);
    }

    private Book searchRecursive(BookNode current, long isbn) {
        // Base Case: ISBN not found or root is null
        if (current == null) return null;

        // Found it!
        if (isbn == current.book.getIsbn()) return current.book;

        // Navigate Left or Right
        return isbn < current.book.getIsbn() 
            ? searchRecursive(current.left, isbn) 
            : searchRecursive(current.right, isbn);
    }

    // Useful for displaying the full catalogue alphabetically/numerically
    public void displayAllBooks() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(BookNode node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.book);
            inOrderTraversal(node.right);
        }
    }
}
