# 📚 Smart Library System

A Java-based console application that demonstrates the practical use of **Abstract Data Types (ADTs)** and **Data Structures** in a university library environment. The system allows librarians and students to manage books, perform efficient searches, track borrowing activities, and maintain transaction histories through a menu-driven interface.

---

## 🎯 Project Overview

The Smart Library System was developed as part of a Data Structures and Algorithms assignment to demonstrate how different data structures solve different access requirements:

* **Binary Search Tree (BST)** for efficient book storage and searching by ISBN.
* **Stack** for maintaining borrowing history in Last-In-First-Out (LIFO) order.
* **Interface (ADT)** to promote information hiding and abstraction.
* **File Handling** for persistent storage of library records and borrowing transactions.

---

## 📖 Assignment Scenario

A university library requires a system where:

* Books are stored efficiently for fast searching.
* Students can borrow and return books.
* Borrowing activities are recorded and displayed with the most recent transactions first.
* Librarians can manage the catalogue without modifying source code.

The project demonstrates the relationship between:

| Data Structure           | Purpose                    |
| ------------------------ | -------------------------- |
| Binary Search Tree (BST) | Fast ISBN-based searching  |
| Stack                    | Borrowing history tracking |
| Interface (ADT)          | Information hiding         |
| File Storage             | Data persistence           |

---

## ✨ Features

### 📚 Book Catalogue Management

* Add books to the library catalogue
* Validate ISBN, title, and author information
* Prevent duplicate ISBN registrations
* Display all available books in sorted order

### 🔍 Fast Book Search

* Recursive BST search implementation
* Search books by ISBN
* O(log n) average-case search performance

### 📥 Borrow Books

* Remove books from the active catalogue
* Mark books as borrowed
* Store borrowing records in student history files

### 📤 Return Books

* Restore books to the active catalogue
* Update borrowing records
* Maintain accurate availability status

### 📜 Borrowing History

* View student borrowing history
* Display records in LIFO order using a Stack
* Track both borrowed and returned transactions

### 💾 Persistent Storage

* Automatically saves catalogue information
* Loads existing catalogue on startup
* Maintains permanent library records

---

## 🏗️ System Architecture

### Package Structure

```text
src/
│
├── CatalogueArchitect/
│   ├── Book.java
│   ├── BookNode.java
│   └── LibraryCatalogue.java
│
├── Library/
│   ├── LibraryInterface.java
│   ├── BorrowStack.java
│   └── SmartLibrary.java
│
└── Main.java
```

---

## 📊 Data Structures Used

### 1. Binary Search Tree (BST)

Used to store books indexed by ISBN.

Each node contains:

```java
ISBN
Title
Author
```

Operations:

* Insert Book
* Search Book
* Delete Book
* Display Catalogue

Advantages:

* Efficient searching
* Ordered traversal
* Dynamic insertion/removal

---

### 2. Stack

Used for borrowing history.

Purpose:

* Most recent borrowing records appear first.
* Supports LIFO access pattern.

Example:

```text
Borrow Book A
Borrow Book B
Borrow Book C

History Output:
Book C
Book B
Book A
```

---

### 3. Abstract Data Type (ADT)

Implemented using:

```java
LibraryInterface
```

Benefits:

* Information hiding
* Modular design
* Easier maintenance

---

## 💾 File Storage

The system stores data using text files.

### book_catalogue.txt

Stores all currently available books.

Format:

```text
ISBN,Title,Author
```

Example:

```text
9780134685991,Effective Java,Joshua Bloch
```

---

### allBooks.txt

Stores all books ever registered.

Format:

```text
ISBN,Title,Author,isBorrowed
```

Example:

```text
9780134685991,Effective Java,Joshua Bloch,true
```

---

### Student History Files

Each student receives a unique history file:

```text
<StudentID>_history.txt
```

Example:

```text
A12345_history.txt
```

Format:

```text
ISBN,Title,Author,Status
```

Example:

```text
9780134685991,Effective Java,Joshua Bloch,Borrowed
```

---

## 🚀 How to Run

### Requirements

* Java JDK 8 or newer
* Command Line / Terminal
* Any Java IDE (Eclipse, IntelliJ IDEA, NetBeans, VS Code)

### Compile

```bash
javac Main.java
```

### Run

```bash
java Main
```

---

## 🖥️ Console Menu

```text
=========================================
  Welcome to the Smart Library System
=========================================

1. Add Book to Catalogue
2. Search Book by ISBN
3. Borrow Book
4. Return Book
5. View History by Student ID
6. View Available Library Catalogue
7. Exit System
```

---

## 🔐 Administrative Access

Adding books requires librarian authentication.

Default password:

```text
admin123
```

---

## 👥 Team Responsibilities

### Catalogue Architect

* BST implementation
* Book insertion and deletion

### Borrowing History Manager

* Stack implementation
* Transaction history tracking

### Record Finder

* Recursive BST search algorithm

### ADT Designer

* LibraryInterface design
* Information hiding

### Admin Logic Developer

* Borrow/return workflow
* Catalogue synchronization

---

## 🎓 Learning Outcomes

This project demonstrates:

* Binary Search Tree implementation
* Recursive algorithms
* Stack operations
* Abstract Data Types (ADT)
* Object-Oriented Programming (OOP)
* File handling in Java
* Information hiding and encapsulation
* Menu-driven application design
