package Library;

import java.util.Stack;
import java.io.*;
import CatalogueArchitect.*;

public class BorrowStack {

    // Tailor-Made Individual File Implementation
    public void addBorrowedBook(String studentId, Book book) {
        // Step 1: Save it instantly to the student's personal custom file
        String personalFileName = studentId + "_history.txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(personalFileName, true))) { // 'true' means append mode
            // Added ",Borrowed" at the end of the line to track status inside the file
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor() + ",Borrowed");            
            System.out.println("Book added to borrowing history for student: " + studentId);
        } catch (IOException e) {
            System.out.println("Warning: Unable to create or write to personal transaction file.");
        }
    }

    // NEW HELPER: Searches a student's history file to retrieve book metadata for return reconstruction
    public Book findBookInHistory(String studentId, long isbn) {
        String personalFileName = studentId + "_history.txt";
        File file = new File(personalFileName);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4 && Long.parseLong(tokens[0]) == isbn && tokens[3].equals("Borrowed")) {
                    return new Book(isbn, tokens[1], tokens[2]); // Rebuild book instance
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading history file for data verification.");
        }
        return null;
    }

    // NEW FEATURE: Updates the status of a specific book inside the student's file to "Returned"
    public void returnBookInFile(String studentId, long isbn) {
        String personalFileName = studentId + "_history.txt";
        File file = new File(personalFileName);

        if (!file.exists()) return;

        Stack<String> updatedLines = new Stack<>();
        boolean foundAndUpdated = false;

        // Read all lines and update the specific book's status
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                // If it matches the ISBN and is currently marked as "Borrowed", update it
                if (tokens.length == 4 && Long.parseLong(tokens[0]) == isbn && tokens[3].equals("Borrowed") && !foundAndUpdated) {
                    updatedLines.push(tokens[0] + "," + tokens[1] + "," + tokens[2] + ",Returned");
                    foundAndUpdated = true; // Only update the most recent one if they borrowed duplicates
                } else {
                    updatedLines.push(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading history file during return process.");
            return;
        }

        // Write the updated stack back into the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : updatedLines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving updated return data to history file.");
        }
    }

    // Displays borrowing history dynamically by opening that student's individual file directly into a temporary Stack
    public void showHistory(String studentId) {
        String personalFileName = studentId + "_history.txt";
        File file = new File(personalFileName);

        if (!file.exists()) {
            System.out.println("No history file found. Student " + studentId + " has no prior borrowing history records.");
            return;
        }

        Stack<String> displayStack = new Stack<>();

        // Read lines and push onto a temporary string stack for LIFO printing order
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                displayStack.push(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading history file.");
            return;
        }

        if (displayStack.isEmpty()) {
            System.out.println("No borrowing history available for Student ID: " + studentId);
            return;
        }

        System.out.println("\n--------------------- Borrowing History for " + studentId + " (Most Recent First) ---------------------\n");
        System.out.printf("%-15s | %-40s | %-20s | %-10s\n", "ISBN", "Title", "Author", "Status");
        System.out.println("---------------------------------------------------------------------------------------------");
        
        // Pop out values to show the most recent actions first (LIFO pattern)
        while (!displayStack.isEmpty()) {
            String[] tokens = displayStack.pop().split(",");
            if (tokens.length == 4) {
                System.out.printf("%-15s | %-40s | %-20s | %-10s\n", tokens[0], tokens[1], tokens[2], tokens[3]);
                System.out.println("---------------------------------------------------------------------------------------------");
            }
        }
    }
}