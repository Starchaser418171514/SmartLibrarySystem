package Library;

import java.util.Stack;
import java.io.*;
import CatalogueArchitect.*;

public class BorrowStack {

    // appends a newly borrowed book to the student's personal history file
    public void addBorrowedBook(String studentId, Book book) {
        // create a unique file name for the student
        String personalFileName = studentId + "_history.txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(personalFileName, true))) { // 'true' means append mode
            // write the book details with status "Borrowed"
            writer.println(book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor() + ",Borrowed");            
            System.out.println("Book added to borrowing history for student: " + studentId);
        } catch (IOException e) {
            System.out.println("Warning: Unable to create or write to personal transaction file.");
        }
    }

    // checks a student's personal history to see if they are borrowing a specific book
    public Book findBookInHistory(String studentId, long isbn) {
        String personalFileName = studentId + "_history.txt";
        File file = new File(personalFileName);

        if (!file.exists()) return null;    // student don't have a history file

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                // check isbn match and check status is "Borrowed"
                if (tokens.length == 4 && Long.parseLong(tokens[0]) == isbn && tokens[3].equals("Borrowed")) {
                    return new Book(isbn, tokens[1], tokens[2]); // rebuild and return the book
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading history file for data verification.");
        }
        return null;
    }

    // update book status to "Returned"
    public void returnBookInFile(String studentId, long isbn) {
        String personalFileName = studentId + "_history.txt";
        File file = new File(personalFileName);

        if (!file.exists()) return;  // student don't have a history file

        // create stack to temporarily hold the file's lines
        Stack<String> updatedLines = new Stack<>(); 
        boolean foundAndUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // read student's history line by line
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                // If match the isbn + marked as "Borrowed" + havent update
                if (tokens.length == 4 && Long.parseLong(tokens[0]) == isbn && tokens[3].equals("Borrowed") && !foundAndUpdated) {
                    // push the updated string marking it as "Returned"
                    updatedLines.push(tokens[0] + "," + tokens[1] + "," + tokens[2] + ",Returned");
                    foundAndUpdated = true; // Only update the most recent one 
                } else {
                    updatedLines.push(line);  // otherwise, keep the line as it was
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

    // Displays borrowing history
    public void showHistory(String studentId) {
        String personalFileName = studentId + "_history.txt";
        File file = new File(personalFileName);

        if (!file.exists()) {
            System.out.println("No history file found. Student " + studentId + " has no prior borrowing history records.");
            return;
        }

        Stack<String> displayStack = new Stack<>();

        // reads the file top to bottom and pushes each line into the stack (LIFO)
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

        // display history in form of table
        System.out.println("\n------------------------ Borrowing History for " + studentId + " (Most Recent First) -----------------------\n");
        System.out.printf("%-15s | %-40s | %-25s | %-10s\n", "ISBN", "Title", "Author", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");
        
        // Pop top item off the stack to show the most recent actions first (LIFO pattern)
        while (!displayStack.isEmpty()) {
            String[] tokens = displayStack.pop().split(",");
            if (tokens.length == 4) {
                System.out.printf("%-15s | %-40s | %-25s | %-10s\n", tokens[0], tokens[1], tokens[2], tokens[3]);
                System.out.println("--------------------------------------------------------------------------------------------------");
            }
        }
    }
}