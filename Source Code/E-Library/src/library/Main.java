package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static Connection connection = DBConnection.getConnection();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        displayMenu();

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:

        }
    }

    private static void displayMenu() {
        System.out.println("Welcome to our application:");
        System.out.println("1. Display Students");
        System.out.println("2. Insert Students");
        System.out.println("3. Update Students");
        System.out.println("4. Delete Students");
    }

    private static void insertStudent() {
        String name;
        int age;

        System.out.println("Enter student name:");
        name = scanner.nextLine();
        age = scanner.nextInt();

        try {
            Statement stmt = connection.createStatement();
        } catch(SQLException e) {
            System.out.println("something went wrong while inserting new student record");
            System.out.println(e.getMessage());
        }
    }
}
