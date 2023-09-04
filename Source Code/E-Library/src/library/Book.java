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

    public ResultSet displayBooks() {
        Statement statement = null;
        ResultSet books = null;

        try {
            statement = DBConnection.getConnection().createStatement();
            books = statement.executeQuery("SELECT * from books");
            statement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while fetching books data");
            System.out.println(e.getMessage());
        }
        return books;
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

    public static boolean checkBookPresence(int id) {
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
}
