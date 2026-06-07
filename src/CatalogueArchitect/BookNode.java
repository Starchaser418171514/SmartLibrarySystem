package CatalogueArchitect;

class BookNode {
    Book book; 
    BookNode left, right;

    public BookNode(Book book) {
        this.book = book;
        this.left = this.right = null;
    }
}