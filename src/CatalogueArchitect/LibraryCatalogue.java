package CatalogueArchitect;

public class LibraryCatalogue {
    private BookNode root;

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
        } else {
            System.out.println("Error: Book with ISBN " + book.getIsbn() + " already exists.");
        }
        return current;
    }

    public Book findBook(long isbn) {
        return searchRecursive(root, isbn);
    }

    private Book searchRecursive(BookNode current, long isbn) {
        if (current == null) return null;
        if (isbn == current.book.getIsbn()) return current.book;

        return isbn < current.book.getIsbn() 
            ? searchRecursive(current.left, isbn) 
            : searchRecursive(current.right, isbn);
    }

    public void displayAllBooks() {
        if (root == null) {
            System.out.println("The library catalogue is currently empty.");
            return;
        }
        inOrderTraversal(root);
    }

    private void inOrderTraversal(BookNode node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.book);      // Print the title alongside its availability status
            inOrderTraversal(node.right);
        }
    }
}