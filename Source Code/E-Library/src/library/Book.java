package library;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    private String title;
    private String author;
    private String status;
    private String isbn;

    @Override
    public String toString() {
        return title + "\n" + author + "\n" + status + "\n" + isbn + "\n";
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsbn() {
        return this.isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public static void displayBooks() {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            ResultSet books = statement.executeQuery("SELECT * from books where status = ?");
            while (books.next()) {
                System.out.println("title: " + books.getString("title"));
                System.out.println("author: " + books.getString("author"));
                System.out.println("status: " + books.getString("status"));
                System.out.println("=====================");
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while fetching books data");
            System.out.println(e.getMessage());
        }
    }

    public void insertBook(Book book) {
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("INSERT INTO books (title, author, status, isbn) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getStatus());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while inserting new book");
            System.out.println(e.getMessage());
        }
    }

    public boolean updateBook(Book book, int id) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("UPDATE books set title = ?, author = ?, status = ?, isbn = ? WHERE id = ?");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getStatus());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setInt(5, id);
            int rowCount = preparedStatement.executeUpdate();
            preparedStatement.close();
            if(rowCount > 0) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while updating book record");
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static boolean updateBook(String isbn, String status) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("UPDATE books set status = ? WHERE isbn = ?");
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, isbn);
            int rowCount = preparedStatement.executeUpdate();
            preparedStatement.close();
            if(rowCount > 0) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while updating book status");
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static boolean deleteBook(int id) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("DELETE FROM books WHERE id = ?");
            preparedStatement.setInt(1, id);
            int rowCount = preparedStatement.executeUpdate();
            if(rowCount > 0) {
                res = true;
            }
            preparedStatement.close();
        } catch(SQLException e) {
            System.out.println("something went wrong while deleting book record");
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static void displayBorrowedBooks() {
        try {
            Statement statement = DBConnection.getConnection().createStatement();
            ResultSet borrowedBooks = statement.executeQuery("SELECT borrows.start_date, borrows.end_date, books.title, books.author, clients.name, clients.membership_number FROM borrows, books, clients WHERE books.id = borrows.book_id AND clients.id = borrows.client_id");
            while(borrowedBooks.next()) {
                System.out.println("Title: " + borrowedBooks.getString("title"));
                System.out.println("Author: " + borrowedBooks.getString("author"));
                System.out.println("Client: " + borrowedBooks.getString("name"));
                System.out.println("Membership Number: " + borrowedBooks.getString("membership_number"));
                System.out.println("Start Date: " + borrowedBooks.getDate("start_date"));
                System.out.println("End Date: " + borrowedBooks.getDate("end_date"));
                System.out.println("====================================");
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while fetching borrowed books");
            System.out.println(e.getMessage());
        }
    }

    public static boolean borrowBook(String endDate, String isbn, int bookId, int clientId) {
        boolean res = false;
        // formatting dates
        Date fEndDate = null;
        java.sql.Date sqlEndDate = null;
        try {
            fEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            sqlEndDate = new java.sql.Date(fEndDate.getTime());
        } catch (ParseException e) {
            System.out.println("something went wrong while formatting date");
            System.out.println(e.getMessage());
        }

        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("INSERT INTO borrows (start_date, end_date, book_id, client_id) VALUES (CURRENT_DATE, ?, ?, ?)");
            preparedStatement.setDate(1, sqlEndDate);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, clientId);
            int rowCount = preparedStatement.executeUpdate();
            if(rowCount > 0) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while saving borrow operation details");
            System.out.println(e.getMessage());
        }

        return res;
    }

    public static boolean returnBook(String isbn) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("DELETE FROM borrows WHERE book_id = ?");
            preparedStatement.setInt(1, getBookId(isbn));
            int numRows = preparedStatement.executeUpdate();
            if(numRows > 0) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while returning book");
            System.out.println(e.getMessage());
        }
        return res;
    }

    private static int getBookId(String isbn) {
        int bookId = 0;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT id FROM books WHERE isbn = ?");
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bookId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while fetching book id");
            System.out.println(e.getMessage());
        }
        return bookId;
    }

    public static boolean findBook(int id) {
        boolean bookPresence = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM books WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                bookPresence = true;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookPresence;
    }

    public static int findBook(String isbn) {
        int bookId = 0;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM books WHERE isbn = ? AND status = ?");
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, "Available");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                bookId = resultSet.getInt(1);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return bookId;
    }

    public static boolean search(String target) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM books WHERE title LIKE CONCAT( '%', ?, '%') or author LIKE CONCAT( '%', ?, '%')");
            preparedStatement.setString(1, target);
            preparedStatement.setString(2, target);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                res = true;
                do {
                    System.out.println("title: " + resultSet.getString("title"));
                    System.out.println("author: " + resultSet.getString("author"));
                    System.out.println("status: " + resultSet.getString("status"));
                } while (resultSet.next());
            } else {
                System.out.println("No books found!");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong while searching books");
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static void getStats() {
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select (select count(*) from books where status = ?) as available, (select count(*) from books where status = ?) as borrowed, (select count(*) from books where status = ?) as lost");
            preparedStatement.setString(1, "Available");
            preparedStatement.setString(2, "Borrowed");
            preparedStatement.setString(3, "Lost");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                System.out.println("Available Books: " + resultSet.getInt("Available"));
                System.out.println("Borrowed Books: " + resultSet.getInt("Borrowed"));
                System.out.println("Lost Books: " + resultSet.getInt("Lost"));
            }
            System.out.println("Report is ready!");
            createFile(resultSet);
        } catch (SQLException e) {
            System.out.println("something went wrong while fetching statistics");
            System.out.println(e.getMessage());
        }
    }

    private static void createFile(ResultSet stats) throws SQLException {
        try {
            File file = new File("C:\\Users\\adm\\Desktop\\E-Library\\Source Code\\E-Library\\statistics report\\report.txt");
            FileWriter writer = new FileWriter("statistics report\\report.txt");
            if(!file.exists()) file.createNewFile();
            writer.write("Library Report: \n");
            writer.write("Available books: " + stats.getString("Available") + "\n");
            writer.write("Borrowed books: " + stats.getString("Borrowed") + "\n");
            writer.write("Lost books: " + stats.getString("Lost") + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("something went wrong while creating a new file");
            System.out.println(e.getMessage());
        }
    }
}
