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

    public static boolean checkClientHistory(int ClientId) {
        boolean res = false;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT start_date, end_date FROM borrows WHERE client_id = ?");
            preparedStatement.setInt(1, ClientId);
            int numRows = preparedStatement.executeUpdate();
            if(numRows > 0) {
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while checking client history");
            System.out.println(e.getMessage());
        }
        return res;
    }
}
