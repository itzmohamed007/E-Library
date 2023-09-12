package library;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Librarian {
    private String name;
    private String identifier;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return this.identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public static boolean login(String name, String identifier) {
        boolean status = false;
        int dbIdentifier = getIdentifier(name);
        if(dbIdentifier != 0 && identifier.hashCode() == dbIdentifier) {
            status = true;
        }
        return status;
    }

    private static int getIdentifier(String username) {
        int res = 0;
        try {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("select identifier from librarians where name = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                res = resultSet.getInt("identifier");
            }
        } catch (SQLException e) {
            System.out.println("something went wrong while authentication first phase");
            System.out.println(e.getMessage());
        }
        return res;
    }
}
