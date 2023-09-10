package library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
    private String name;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static int insertClient(String name) {
        int membershipNumber = 0;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("INSERT INTO clients (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            int rowCount = preparedStatement.executeUpdate();
            if(rowCount == 0) {
                throw new SQLException("Creating client failed, no rows affected");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                membershipNumber = generatedKeys.getInt(1);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while inserting new client");
            System.out.println(e.getMessage());
        }
        return membershipNumber;
    }

    public static boolean checkClientHistory(int membership) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM borrows WHERE membership = ?");
            preparedStatement.setInt(1, membership);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while checking client history");
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static boolean checkClientPresence(int membership) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM clients WHERE membership = ?");
            preparedStatement.setInt(1, membership);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while checking client history");
            System.out.println(e.getMessage());
        }
        return res;
    }
}
