import library.Book;
import library.Client;
import library.DBConnection;
import library.Librarian;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    private static String bufferDump = null;

    public static void main(String[] args)  {
        login();
        System.out.println("Welcome to E-Library");
        int choice;
        do {
            choice = displayMenu();
            switch (choice) {
                case 1: {
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
                    String id = scanner.next();
                    if(!isInteger(id)) {
                        intError();
                        break;
                    }
                    if(Book.findBook(Integer.parseInt(id))) {
                        Book book = new Book();
                        System.out.println("Enter new title");
                        bufferDump = scanner.nextLine();
                        book.setTitle(scanner.nextLine());
                        System.out.println("Enter new author");
                        book.setAuthor(scanner.nextLine());
                        System.out.println("Enter new ISBN code");
                        book.setIsbn(scanner.nextLine());
                        if(book.updateBook(book, Integer.parseInt(id))) {
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
                    System.out.println("Enter book id");
                    String id = scanner.next();
                    if(!isInteger(id)) {
                        intError();
                        break;
                    }
                    if(Book.findBook(Integer.parseInt(id))) {
                        Book.deleteBook(Integer.parseInt(id));
                    } else {
                        System.out.println("Book not found");
                    }
                    break;
                }
                case 4: {
                    Book.displayBooks();
                    break;
                }
                case 5: {
                    Book.displayBorrowedBooks();
                    break;
                }
                case 6: {
                    System.out.println("Enter book code");
                    bufferDump = scanner.nextLine();
                    String isbn = scanner.nextLine();
                    int bookId = Book.findBook(isbn);
                    if(bookId != 0) {
                        System.out.println("Is client new or a member ?");
                        System.out.println("1. A new client");
                        System.out.println("2. A member");
                        String type = scanner.next();
                        if(!isInteger(type)) {
                            intError();
                            break;
                        }
                        switch (Integer.parseInt(type)) {
                            case 1: {
                                System.out.print("Enter full name: ");
                                bufferDump = scanner.nextLine();
                                String clientName = scanner.nextLine();
                                int membership = Client.insertClient(clientName);
                                System.out.println("New client membership number: " + membership);
                                System.out.print("Enter end date (YYYY-MM-DD): ");
                                String end_date = scanner.nextLine();
                                if(Book.borrowBook(end_date, bookId, membership)) {
                                    System.out.println("Operation done successfully!");
                                } else {
                                    System.out.println("Operation failed");
                                }
                                break;
                            }
                            case 2: {
                                System.out.print("Enter membership number: ");
                                int membership = scanner.nextInt();
                                if(!Client.checkClientPresence(membership)) {
                                    System.out.println("Client not found");
                                    break;
                                } else if(Client.checkClientHistory(membership)) {
                                    System.out.println("Client already borrowed a book");
                                    break;
                                }
                                System.out.print("Enter end date (YYYY-MM-DD): ");
                                bufferDump = scanner.nextLine();
                                String end_date = scanner.nextLine();
                                if(Book.borrowBook(end_date, bookId, membership)) {
                                    System.out.println("Operation done successfully!");
                                } else {
                                    System.out.println("Operation failed");
                                }
                                break;
                            }
                            default:
                                System.out.println("invalid choice");
                                break;
                        }
                    }
                    else {
                        System.out.println("This book is unavailable!");
                    }
                    break;
                }
                case 7: {
                    System.out.print("Enter book code: ");
                    bufferDump = scanner.nextLine();
                    String isbn = scanner.nextLine();
                    if(Book.returnBook(isbn)) {
                        System.out.println("Book Returned Successfully!");
                    } else {
                        System.out.println("Book not found!");
                    }
                    break;
                }
                case 8: {
                    System.out.print("Enter book title or isbn code: ");
                    bufferDump = scanner.nextLine();
                    String target = scanner.nextLine();
                    Book.search(target);
                    break;
                }
                case 9: {
                    Book.getStats();
                    break;
                }
                case 10: {
                    System.out.println("EXIT");
                    DBConnection.closeConnection();
                    break;
                }
                default: {
                    System.out.println("Unavailable option!");
                    break;
                }
            }
        } while (choice != 10);
    }

    private static void login() {
        System.out.print("enter your name: ");
        String name = scanner.nextLine();
        System.out.print("enter your identifier: ");
        String identifier = scanner.nextLine();
        if(!Librarian.login(name, identifier)) {
            System.out.println("logging failed!");
            login();
        }
    }

    private static int displayMenu() {
        System.out.println("|---------------------------------------------------|");
        System.out.println("|             Choose desired operation:             |");
        System.out.println("|             1. Add new book                       |");
        System.out.println("|             2. Update old book                    |");
        System.out.println("|             3. Delete old book                    |");
        System.out.println("|             4. Display all books                  |");
        System.out.println("|             5. Display borrowed books             |");
        System.out.println("|             6. Borrow a book                      |");
        System.out.println("|             7. Return a book                      |");
        System.out.println("|             8. Search a book                      |");
        System.out.println("|             9. Get stats                          |");
        System.out.println("|             10. Exit                              |");
        System.out.println("|---------------------------------------------------|");

        String choice = scanner.next();

        if(!isInteger(choice)) {
            intError();
            return displayMenu();
        }
        return Integer.parseInt(choice);
    }

    private static boolean isInteger(String string) {
        boolean res = false;
        try {
            Integer.parseInt(string);
            res = true;
        } catch (NumberFormatException e) {
            res = false;
        }
        return res;
    }

    private static void intError() {
        System.out.println("Please enter a number");
    }
}
