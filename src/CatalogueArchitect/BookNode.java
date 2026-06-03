package CatalogueArchitect;

class BookNode {
    Book book; // Fixed from BookData to Book
    BookNode left, right;

    public BookNode(Book book) {
        this.book = book;
        this.left = this.right = null;
    }
}