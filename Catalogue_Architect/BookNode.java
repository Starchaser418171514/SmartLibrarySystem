package SmartLibrarySystem.Catalogue_Architect

class BookNode {
    BookData book;
    BookNode left, right;

    public BookNode(BookData book) {
        this.book = book;
        this.left = this.right = null;
    }
}