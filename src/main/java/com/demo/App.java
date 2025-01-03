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
		insertContact("Nanesh", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234", "rahul.deshmukh@example.com");
		insertContact("Rahul", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234", "rahul.deshmukh@example.com");
		insertContact("Mahesh", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234", "rahul.deshmukh@example.com");
		//update contact
        updateContactByName("Rahul", "Deshmukh", "48 Ganapati Nagar, New Building", "Pune", "Maharashtra", "411027", "9856544321", "rahul.deshmukh@example.com");
        //delete contact
       deleteContactByName("Rahul", "Deshmukh");
       //get contact by cityorstate
       String state="maharashtra";
       System.out.println("----------get contact by cityorstate("+state+")------------");
       getContactByCityOrState("maharashtra");
       //get size of contact by city and state
       String city = "pune";
       String state1 = "maharashtra";
       System.out.println("---------get size of contact by city and state("+city+""+state1+")----------");
       int contactCount = getContactCountByCityAndState(city, state1);
       System.out.println("Number of contacts in " + city + ", " + state + ": " + contactCount);
       //sort conatct by name for city
       String city1 = "pune";
       getSortedContactsByNameForCity(city1);
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
	private static void getContactByCityOrState(String cityOrState) {
	    String selectQuery = "SELECT * FROM AddressBook WHERE city = ? OR state = ?";
	    
	    try (Connection con = getConnection("address_book"); 
	         PreparedStatement stmt = con.prepareStatement(selectQuery)) {
	        
	        stmt.setString(1, cityOrState);
	        stmt.setString(2, cityOrState);
	        
	        ResultSet rs = stmt.executeQuery();
	        
	        boolean found = false;
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String firstName = rs.getString("first_name");
	            String lastName = rs.getString("last_name");
	            String address = rs.getString("address");
	            String city = rs.getString("city");
	            String state = rs.getString("state");
	            String zip = rs.getString("zip");
	            String phoneNumber = rs.getString("phone_number");
	            String email = rs.getString("email");
	            
	            System.out.println("ID: " + id);
	            System.out.println("Name: " + firstName + " " + lastName);
	            System.out.println("Address: " + address);
	            System.out.println("City: " + city);
	            System.out.println("State: " + state);
	            System.out.println("Zip: " + zip);
	            System.out.println("Phone: " + phoneNumber);
	            System.out.println("Email: " + email);
	            System.out.println("-------------------------");
	            found = true;
	        }
	        
	        if (!found) {
	            System.out.println("No contacts found for the city or state: " + cityOrState);
	        }
	        
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	private static int getContactCountByCityAndState(String city, String state) {
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM AddressBook WHERE city = ? AND state = ?";
        
        try (Connection con = getConnection("address_book"); PreparedStatement stmt = con.prepareStatement(countQuery)) {
            stmt.setString(1, city);
            stmt.setString(2, state);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1); 
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return count;
    }

	private static void getSortedContactsByNameForCity(String city) {
        String query = "SELECT first_name, last_name, address, city, state, zip, phone_number, email "
                     + "FROM AddressBook WHERE city = ? ORDER BY first_name, last_name ASC";
        
        try (Connection con = getConnection("address_book"); PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("---------Contacts in " + city + " sorted by name:--------");
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String state = rs.getString("state");
                String zip = rs.getString("zip");
                String phoneNumber = rs.getString("phone_number");
                String email = rs.getString("email");
                
                System.out.println(firstName + " " + lastName);
                System.out.println("Address: " + address);
                System.out.println("City: " + city);
                System.out.println("State: " + state);
                System.out.println("ZIP: " + zip);
                System.out.println("Phone: " + phoneNumber);
                System.out.println("Email: " + email);
                System.out.println("-----------");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
