import library.Book;
import library.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static String bufferDump = null;

    public static void main(String[] args) {
        System.out.println("Welcome back to E-Library");

        int choice;
        do {
            choice = displayMenu();
            switch (choice) {
                case 1: {
                    System.out.println("you want to add a new book");
                    Book book = new Book();
                    System.out.println("enter title: ");
                    bufferDump = scanner.nextLine();
                    book.setTitle(scanner.nextLine());
                    System.out.println("enter author: ");
                    book.setAuthor(scanner.nextLine());
                    System.out.println("enter ISBN code: ");
                    book.setIsbn(scanner.nextLine());
                    book.setStatus("Available");
                    book.insertBook(book);
                    System.out.println("Book inserted Successfully");
                    break;
                }
                case 2: {
                    System.out.println("Enter book id");
                    int id = scanner.nextInt();
                    if(Book.checkBookPresence(id)) {
                        Book book = new Book();
                        System.out.println("Enter new title");
                        bufferDump = scanner.nextLine();
                        book.setTitle(scanner.nextLine());
                        System.out.println("Enter new author");
                        book.setAuthor(scanner.nextLine());
                        System.out.println("Enter new status");
                        book.setStatus(scanner.nextLine());
                        System.out.println("Enter new ISBN code");
                        book.setIsbn(scanner.nextLine());
                        if(book.updateBook(book, id)) {
                            System.out.println("Book updated successfully");
                        } else {
                            System.out.println("something went wrong");
                        }
                    } else {
                        System.out.println("Book not found");
                    }
                    break;
                }
                case 3: {
                    System.out.println("you want to delete an old book");
                    System.out.println("Enter book id");
                    int id = scanner.nextInt();
                    if(Book.checkBookPresence(id) && Book.deleteBook(id)) {
                        System.out.println("Book deleted successfully");
                    }
                    break;
                }
                case 4: {
                    System.out.println("you want to display all books");
                    break;
                }
                case 5: {
                    System.out.println("you want to display borrowed books");
                    break;
                }
                case 6: {
                    System.out.println("you want to borrow a book");
                    break;
                }
                case 7: {
                    System.out.println("you want to return a book");
                    break;
                }
                case 8: {
                    System.out.println("you want to search for a book");
                    break;
                }
                case 9: {
                    System.out.println("you want to display stats");
                    break;
                }
                case 10: {
                    System.out.println("you want to exit");
                    break;
                }
                default: {
                    System.out.println("Unavailable option!");
                    break;
                }
            }
        } while (choice != 10);
    }

    private static int displayMenu() {
        System.out.println("Choose desired operation:");
        System.out.println("1. Add new book");
        System.out.println("2. Update old book");
        System.out.println("3. Delete old book");
        System.out.println("4. Display all books");
        System.out.println("5. Display borrowed books");
        System.out.println("6. Borrow a book");
        System.out.println("7. Return a book");
        System.out.println("8. Search a book");
        System.out.println("9. Get stats");
        System.out.println("10. Exit");

        return scanner.nextInt();
    }
}
