package SmartLibrarySystem.src.Library;

public interface LibraryInterface {
    public void addBook(long isbn, String title, String author);
    public void borrowBook(long isbn);
    public void viewLatestHistory();
    public void findBook(long isbn);
}
