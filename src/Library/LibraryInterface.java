package SmartLibrarySystem.src.Library;

public interface LibraryInterface {
    public void addBook(int isbn, String title, String author);
    public void borrowBook(int isbn);
    public void viewLatestHistory();
    public void findBook(int isbn);
}
