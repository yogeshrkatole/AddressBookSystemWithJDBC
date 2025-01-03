package main.java.com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		//update contact
        updateContactByName("Rahul", "Deshmukh", "48 Ganapati Nagar, New Building", "Pune", "Maharashtra", "411027", "9856544321", "rahul.deshmukh@example.com");
        //delete contact
       deleteContactByName("Rahul", "Deshmukh");
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
	private static void updateContactByName(String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber, String email) {
	    String checkNameQuery = "SELECT id FROM AddressBook WHERE first_name = ? AND last_name = ?";
	    try (Connection con = getConnection("address_book"); 
	         PreparedStatement checkStmt = con.prepareStatement(checkNameQuery)) {
	        checkStmt.setString(1, firstName);
	        checkStmt.setString(2, lastName);
	        ResultSet resultSet = checkStmt.executeQuery();

	        if (resultSet.next()) {
	            String updateQuery = "UPDATE AddressBook SET address = ?, city = ?, state = ?, zip = ?, phone_number = ?, email = ? WHERE first_name = ? AND last_name = ?";
	            try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
	                updateStmt.setString(1, address);
	                updateStmt.setString(2, city);
	                updateStmt.setString(3, state);
	                updateStmt.setString(4, zip);
	                updateStmt.setString(5, phoneNumber);
	                updateStmt.setString(6, email);
	                updateStmt.setString(7, firstName);
	                updateStmt.setString(8, lastName);

	                int rowsUpdated = updateStmt.executeUpdate();
	                if (rowsUpdated > 0) {
	                    System.out.println("Contact updated successfully!");
	                }
	            }
	        } else {
	            System.out.println("No contact found with this name. No updates made.");
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	private static void deleteContactByName(String firstName, String lastName) {
	    String deleteQuery = "DELETE FROM AddressBook WHERE first_name = ? AND last_name = ?";
	    try (Connection con = getConnection("address_book"); 
	         PreparedStatement stmt = con.prepareStatement(deleteQuery)) {
	        
	        stmt.setString(1, firstName);
	        stmt.setString(2, lastName);
	        
	        int rowsDeleted = stmt.executeUpdate();
	        if (rowsDeleted > 0) {
	            System.out.println("Contact deleted successfully!");
	        } else {
	            System.out.println("No contact found with the given name.");
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}


}
