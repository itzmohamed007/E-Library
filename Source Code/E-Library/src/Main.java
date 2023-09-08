import library.Book;
import library.Client;
import library.Libraryan;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    private String args[] = new String[10];
    private static String bufferDump = null;

    public static void main(String[] args)  {
        System.out.print("enter your name: ");
//        bufferDump = scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("enter your identifier: ");
        String identifier = scanner.nextLine();
        if(Libraryan.login(name, identifier)) {
            System.out.println("logged in successfully!");
        } else {
            System.out.println("logging failed!");
            main(args);
        }

        System.out.println("Welcome to E-Library");
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
                    if(Book.findBook(id)) {
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
                    if(Book.findBook(id)) {
                        Book.deleteBook(id);
                    } else {
                        System.out.println("Book not found");
                    }
                    break;
                }
                case 4: {
                    System.out.println("Here are all books");
                    Book.displayBooks();
                    break;
                }
                case 5: {
                    System.out.println("Here are all the borrowed books");
                    Book.displayBorrowedBooks();
                    break;
                }
                case 6: {
                    System.out.println("you want to borrow a book");
                    System.out.println("Enter book code");
                    bufferDump = scanner.nextLine();
                    String isbn = scanner.nextLine();
                    int bookId = Book.findBook(isbn);
                    if(bookId != 0) {
                        System.out.println("This book is available!");
                        System.out.print("Enter client name: ");
                        String clientName = scanner.nextLine();
                        System.out.print("Enter client membership number: ");
                        int membershipNumber = scanner.nextInt();
                        // checking client presence
                        boolean clientHistory = Client.checkClientPresence(membershipNumber);

                        if(clientHistory) {
                            System.out.println("You have already borrowed a book");
                            break;
                        }

                        // inserting new client
                        Client client = new Client(clientName, membershipNumber);
                        int clientId = client.insertClient(client);

                        System.out.print("Enter end date (YYYY-MM-DD): ");
                        bufferDump = scanner.nextLine();
                        String end_date = scanner.nextLine();

                        if(Book.borrowBook(end_date, isbn, bookId, clientId)) {
                            System.out.println("Operation passed successfully!");
                        } else {
                            System.out.println("Operation failed");
                        }
                    } else {
                        System.out.println("This book is unavailable!");
                    }
                    break;
                }
                case 7: {
                    System.out.println("you want to return a book");
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

        return scanner.nextInt();
    }
}
