package main.java.com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
	public static void main(String[] args) {
		System.out.println("Welcome To AddressBook");
	    //create database
		 createDB();
		// create table
		createTable();
		//insert contact
		insertContact("Rahul", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234", "rahul.deshmukh@example.com");
	}

	private static Connection getConnection(String database) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String jdbcURL = database == null || database.isEmpty()
                ? "jdbc:mysql://localhost:3306/"
                : "jdbc:mysql://localhost:3306/" + database;
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(jdbcURL, username, password);
    }
	private static void createTable() {
		String tableQuery = "CREATE TABLE IF NOT EXISTS AddressBook (" + "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "first_name VARCHAR(50) NOT NULL, " + "last_name VARCHAR(50) NOT NULL, " + "address VARCHAR(255), "
				+ "city VARCHAR(50), " + "state VARCHAR(50), " + "zip VARCHAR(10), " + "phone_number VARCHAR(15), "
				+ "email VARCHAR(100) UNIQUE" + ");";
		try (Connection con = getConnection("address_book"); Statement stmt = con.prepareStatement(tableQuery)) {

			if (stmt.executeUpdate(tableQuery) == 1)
				System.out.println("Table created successfully");

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	private static void createDB() {
		String dbQuery = "Create Database If Not Exists address_book";
		try (Connection con = getConnection(""); Statement stmt = con.prepareStatement(dbQuery)) {

			if (stmt.executeUpdate(dbQuery) == 1)
				System.out.println("Table created successfully");

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static void insertContact(String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber, String email) {
        String insertQuery = "INSERT INTO AddressBook (first_name, last_name, address, city, state, zip, phone_number, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection("address_book"); PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, address);
            pstmt.setString(4, city);
            pstmt.setString(5, state);
            pstmt.setString(6, zip);
            pstmt.setString(7, phoneNumber);
            pstmt.setString(8, email);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new contact was inserted successfully!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
