package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost/library2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException exception) {
            System.out.println("something went wrong");
            System.out.println(exception.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch(SQLException exception) {
            System.out.println("something went wrong while closing connection");
            System.out.println(exception.getMessage());
        }
    }
}
