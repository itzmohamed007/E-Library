package library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
    private String name;
    private int membershipNumber;

    public Client(String name, int membershipNumber) {
        this.name = name;
        this.membershipNumber = membershipNumber;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getMembershipNumber() {
        return this.membershipNumber;
    }
    public void setMembershipNumber(int membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public int insertClient(Client client) {
        int clientId = 0;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("INSERT INTO clients (name, membership_number) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setInt(2, client.getMembershipNumber());
            int rowCount = preparedStatement.executeUpdate();
            if(rowCount == 0) {
                throw new SQLException("Creating client failed, no rows affected");
            }
            // getting new inserted client id
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                clientId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating client failed, no ID obtained");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("something went wrong while inserting new client");
            System.out.println(e.getMessage());
        }
        return clientId;
    }

    public static boolean checkClientPresence(String name, int membershipNumber) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM clients WHERE name = ? OR membership_number = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, membershipNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                res = checkClientHistory(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while checking client presence");
            System.out.println(e.getMessage());
        }
        return res;
    }

    private static boolean checkClientHistory(int ClientId) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM borrows WHERE client_id = ?");
            preparedStatement.setInt(1, ClientId);
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
