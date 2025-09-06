import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Book {
    String bookId;
    String title;
    String author;
    boolean available;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    @Override
    public String toString() {
        String status = available ? "Available" : "Issued";
        return bookId + " - " + title + " by " + author + " (" + status + ")";
    }
}

class LibrarySystem {
    ArrayList<Book> books = new ArrayList<>();
    private static final String FILE_NAME = "library_data.txt";

    // Load books from file
    public void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1); // bookId,title,author,available
                if (parts.length == 4) {
                    Book book = new Book(parts[0], parts[1], parts[2]);
                    book.available = Boolean.parseBoolean(parts[3]);
                    books.add(book);
                }
            }
            System.out.println("Books loaded from file successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found. Starting with empty library.");
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    // Save books to file
    public void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                pw.println(book.bookId + "," + book.title + "," + book.author + "," + book.available);
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        for (Book b : books) {
            if (b.bookId.equals(book.bookId)) {
                System.out.println("Book ID already exists. Try again with a different ID.");
                return;
            }
        }
        books.add(book);
        saveBooks();
        System.out.println("Book '" + book.title + "' added successfully!");
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    public void searchBook(String keyword) {
        boolean found = false;
        for (Book book : books) {
            if (book.title.toLowerCase().contains(keyword.toLowerCase()) ||
                    book.author.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching book found.");
        }
    }

    public void issueBook(String bookId) {
        for (Book book : books) {
            if (book.bookId.equals(bookId) && book.available) {
                book.available = false;
                saveBooks();
                System.out.println("Book '" + book.title + "' issued successfully.");
                return;
            }
        }
        System.out.println("Book not available or invalid ID.");
    }

    public void returnBook(String bookId) {
        for (Book book : books) {
            if (book.bookId.equals(bookId) && !book.available) {
                book.available = true;
                saveBooks();
                System.out.println("Book '" + book.title + "' returned successfully.");
                return;
            }
        }
        System.out.println("Invalid Book ID or Book was not issued.");
    }
}

public class Library {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LibrarySystem library = new LibrarySystem();

        // Load saved books
        library.loadBooks();

        while (true) {
            System.out.println("\n--- Library Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Book Author: ");
                    String author = sc.nextLine();
                    library.addBook(new Book(id, title, author));
                    break;
                case 2:
                    library.displayBooks();
                    break;
                case 3:
                    System.out.print("Enter keyword to search: ");
                    String keyword = sc.nextLine();
                    library.searchBook(keyword);
                    break;
                case 4:
                    System.out.print("Enter Book ID to issue: ");
                    String issueId = sc.nextLine();
                    library.issueBook(issueId);
                    break;
                case 5:
                    System.out.print("Enter Book ID to return: ");
                    String returnId = sc.nextLine();
                    library.returnBook(returnId);
                    break;
                case 6:
                    System.out.println("Exiting Library System. Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }
}
