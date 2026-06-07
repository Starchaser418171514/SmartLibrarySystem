import java.util.Scanner;
import Library.SmartLibrary;
import CatalogueArchitect.Book;

public class Main {
    public static void main(String[] args) {
        SmartLibrary library = new SmartLibrary();
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=========================================");
        System.out.println("  Welcome to the Smart Library System");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n--- Main System Menu ---");
            System.out.println("1. Add Book to Catalogue");
            System.out.println("2. Search Book by ISBN");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. View History by Student ID");
            System.out.println("6. View Available Library Catalogue");
            System.out.println("7. Exit System");
            System.out.print("Select operational choice: ");

            if (!sc.hasNextInt()) { //validation step if user input is not number
                System.out.println("Input Error: Please pass an option number (1-7).");
                sc.next();
                continue;  //jump back to top of while loop
            }

            int choice = sc.nextInt();
            sc.nextLine(); 
            System.out.println(); 

            if (choice == 7) {
                System.out.println("Exiting System Database. Goodbye.");
                break; //break out of while loop, meaning to stop the program
            }

            switch (choice) {
                case 1:
                    // Simple role verification gate
                    System.out.print("Access Denied: Librarian credentials required.\nEnter Admin Password to proceed: ");
                    String password = sc.nextLine().trim();
                    
                    if (!password.equals("admin123")) { // You can set any password here
                        System.out.println("Authorization Failure: Incorrect password. Returning to main menu.");
                        break;
                    }
                    System.out.println("Access Granted. Proceeding with catalogue insertion...");

                    System.out.print("Enter Book ISBN (Numeric): ");
                    if (!sc.hasNextLong()) { // validate isbn enter is valid long number
                        System.out.println("Validation Failure: ISBN must be a number sequence.");
                        sc.nextLine();
                        break;
                    }
                    long isbn = sc.nextLong();
                    sc.nextLine();

                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();

                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();

                    library.addBook(isbn, title, author);
                    break;

                case 2:
                    System.out.print("Enter search target ISBN: ");
                    if (!sc.hasNextLong()) {
                        System.out.println("Validation Failure: ISBN must be a number sequence.");
                        sc.nextLine();
                        break;
                    }
                    long targetIsbn = sc.nextLong();
                    sc.nextLine();

                    // High-speed O(log n) search through the active catalogue BST
                    Book matchingBook = library.findBook(targetIsbn);
                    
                    if (matchingBook != null) {
                        System.out.println("Query Match Found -> " + matchingBook + " [Status: Available on Shelf]");
                    } else {
                        // Validation in allBooks.txt if missing from the active catalogue
                        String registryStatus = library.checkPermanentRegistry(targetIsbn);
                        
                        if (registryStatus.equals("Borrowed")) {
                            System.out.println("Query Result: Book with ISBN " + targetIsbn + " belongs to this library, but it is currently [Borrowed]!");
                        } else {
                            System.out.println("Query Result: No book found with ISBN " + targetIsbn + " in our library system database.");
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Student ID: ");
                    String idKey = sc.nextLine().trim();
                    if (idKey.isEmpty()) {
                        System.out.println("Validation Failure: ID cannot be empty.");
                        break;
                    }

                    System.out.print("Enter target borrow ISBN: ");
                    if (!sc.hasNextLong()) {
                        System.out.println("Validation Failure: Must be numeric.");
                        sc.nextLine();
                        break;
                    }
                    long borrowIsbn = sc.nextLong();
                    sc.nextLine();

                    library.borrowBook(idKey, borrowIsbn);
                    break;

                case 4: 
                    System.out.print("Enter Student ID: ");
                    String returnId = sc.nextLine().trim();
                    if (returnId.isEmpty()) {
                        System.out.println("Validation Failure: ID cannot be empty.");
                        break;
                    }

                    System.out.print("Enter ISBN of the book to return: ");
                    if (!sc.hasNextLong()) {
                        System.out.println("Validation Failure: Must be numeric.");
                        sc.nextLine();
                        break;
                    }
                    long returnIsbn = sc.nextLong();
                    sc.nextLine();

                    library.returnBook(returnId, returnIsbn);
                    break;

                case 5:
                    System.out.print("Enter tracking Student ID: ");
                    String searchId = sc.nextLine().trim();
                    library.viewLatestHistory(searchId);
                    break;

                case 6:
                    System.out.println("\n--------------------- Complete Library Catalogue ---------------------");
                    library.displayAllCatalogBooks();
                    break;

                default:
                    System.out.println("Option bounds selection error. Pick between numbers 1-7.");
            }
        }
        sc.close();
    }
}