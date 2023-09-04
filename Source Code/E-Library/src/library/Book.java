package library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            ResultSet books = statement.executeQuery("SELECT * from books");
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
            ResultSet borrowedBooks = statement.executeQuery("SELECT borrows.start_date, borrows.end_date, books.title, books.author, clients.name, clients.membershipNumber FROM borrows, books, clients WHERE books.id = borrows.book_id AND clients.id = borrows.client_id");
            while(borrowedBooks.next()) {
                System.out.println("Title: " + borrowedBooks.getString("title"));
                System.out.println("Author: " + borrowedBooks.getString("author"));
                System.out.println("Client: " + borrowedBooks.getString("name"));
                System.out.println("Membership Number: " + borrowedBooks.getString("membershipNumber"));
                System.out.println("Start Date: " + borrowedBooks.getDate("start_date"));
                System.out.println("End Date: " + borrowedBooks.getDate("end_date"));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while fetching borrowed books");
            System.out.println(e.getMessage());
        }
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

    public static boolean findBook(String isbn) {
        boolean bookPresence = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM books WHERE isbn = ? AND status = ?");
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, "Available");
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
}
