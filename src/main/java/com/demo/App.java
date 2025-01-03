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
		// create database
		createDB();
		// create table
		createTable();
		// insert contact
		insertContact("Nanesh", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234",
				"rahul.deshmukh@example.com", "Profession");
		insertContact("Rahul", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234",
				"rahul.deshmukh@example.com", "Friends");
		insertContact("Mahesh", "Deshmukh", "45 Ganapati Nagar", "Pune", "Maharashtra", "411027", "9856541234",
				"rahul.deshmukh@example.com", "Family");
		// update contact
		updateContactByName("Rahul", "Deshmukh", "48 Ganapati Nagar, New Building", "Pune", "Maharashtra", "411027",
				"9856544321", "rahul.deshmukh@example.com","Friends");
		// delete contact
		deleteContactByName("Rahul", "Deshmukh");
		// get contact by cityorstate
		String state = "maharashtra";
		System.out.println("----------get contact by cityorstate(" + state + ")------------");
		getContactByCityOrState("maharashtra");
		// get size of contact by city and state
		String city = "pune";
		String state1 = "maharashtra";
		System.out.println("---------get size of contact by city and state(" + city + "" + state1 + ")----------");
		int contactCount = getContactCountByCityAndState(city, state1);
		System.out.println("Number of contacts in " + city + ", " + state + ": " + contactCount);
		// sort conatct by name for city
		String city1 = "pune";
		getSortedContactsByNameForCity(city1);
		//alter addressbook
		addTypeColumnIfNotExists();
		//get contact count by type
		System.out.println("----------get contact count by type--------");
		getContactCountByType("Profession");
		//add contact in friends and family
		System.out.println("--------add contact in friends and family--------");
	    addContactToBothTypes("Rahul", "Deshmukh", "456 MG Road", "Hyderabad", "Telangana", "500001", "555-5678", "rahul.deshmukh@example.com");
	}

	private static Connection getConnection(String database) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String jdbcURL = database == null || database.isEmpty() ? "jdbc:mysql://localhost:3306/"
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

	private static void insertContact(String firstName, String lastName, String address, String city, String state,
			String zip, String phoneNumber, String email, String type) {
		String insertQuery = "INSERT INTO AddressBook (first_name, last_name, address, city, state, zip, phone_number, email, type) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = getConnection("address_book"); PreparedStatement stmt = con.prepareStatement(insertQuery)) {
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			stmt.setString(3, address);
			stmt.setString(4, city);
			stmt.setString(5, state);
			stmt.setString(6, zip);
			stmt.setString(7, phoneNumber);
			stmt.setString(8, email);
			stmt.setString(9, type);

			stmt.executeUpdate();
			System.out.println("Contact inserted successfully");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void updateContactByName(String firstName, String lastName, String address, String city, 
            String state, String zip, String phoneNumber, String email, String type) {
String updateQuery = "UPDATE AddressBook SET first_name = ?, last_name = ?, address = ?, city = ?, "
+ "state = ?, zip = ?, phone_number = ?, email = ?, type = ? WHERE first_name = ? AND last_name = ?";
try (Connection con = getConnection("address_book"); 
PreparedStatement stmt = con.prepareStatement(updateQuery)) {

stmt.setString(1, firstName);
stmt.setString(2, lastName);
stmt.setString(3, address);
stmt.setString(4, city);
stmt.setString(5, state);
stmt.setString(6, zip);
stmt.setString(7, phoneNumber);
stmt.setString(8, email);
stmt.setString(9, type);
stmt.setString(10, firstName);
stmt.setString(11, lastName);

int rowsAffected = stmt.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Contact updated successfully!");
} else {
System.out.println("No contact found to update.");
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

		try (Connection con = getConnection("address_book");
				PreparedStatement stmt = con.prepareStatement(countQuery)) {
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
	private static void getContactsByNameAndType(String firstName, String lastName, String type) {
	    String query = "SELECT first_name, last_name, address, city, state, zip, phone_number, email, type "
	                 + "FROM AddressBook WHERE first_name = ? AND last_name = ? AND type = ?";

	    try (Connection con = getConnection("address_book"); PreparedStatement stmt = con.prepareStatement(query)) {
	        stmt.setString(1, firstName);
	        stmt.setString(2, lastName);
	        stmt.setString(3, type);
	        ResultSet rs = stmt.executeQuery();

	        System.out.println("Contacts of " + firstName + " " + lastName + " in type " + type + ":");
	        while (rs.next()) {
	            String address = rs.getString("address");
	            String city = rs.getString("city");
	            String state = rs.getString("state");
	            String zip = rs.getString("zip");
	            String phoneNumber = rs.getString("phone_number");
	            String email = rs.getString("email");
	            String contactType = rs.getString("type");

	            System.out.println("Address: " + address);
	            System.out.println("City: " + city);
	            System.out.println("State: " + state);
	            System.out.println("ZIP: " + zip);
	            System.out.println("Phone: " + phoneNumber);
	            System.out.println("Email: " + email);
	            System.out.println("Address Book Type: " + contactType);
	            System.out.println("-----------");
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	private static void addTypeColumnIfNotExists() {
        String checkColumnQuery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'AddressBook' AND COLUMN_NAME = 'type'";

        try (Connection con = getConnection("address_book"); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(checkColumnQuery)) {

            if (!rs.next()) { 
                String alterTableQuery = "ALTER TABLE AddressBook ADD COLUMN type VARCHAR(50) NOT NULL";
                stmt.executeUpdate(alterTableQuery);
                System.out.println("Column 'type' added successfully!");
            } else {
                System.out.println("Column 'type' already exists.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
	private static void getContactCountByType(String type) {
	    String query = "SELECT COUNT(*) FROM AddressBook WHERE type = ?";
	    try (Connection con = getConnection("address_book"); 
	         PreparedStatement stmt = con.prepareStatement(query)) {

	        stmt.setString(1, type);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            int count = rs.getInt(1);
	            System.out.println("Number of contacts in type '" + type + "': " + count);
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}

	private static void addContactToBothTypes(String firstName, String lastName, String address, String city, 
            String state, String zip, String phoneNumber, String email) {
String query1 = "INSERT INTO AddressBook (first_name, last_name, address, city, state, zip, phone_number, email, type) "
+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
String query2 = "INSERT INTO AddressBook (first_name, last_name, address, city, state, zip, phone_number, email, type) "
+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection con = getConnection("address_book"); 
PreparedStatement stmt1 = con.prepareStatement(query1); 
PreparedStatement stmt2 = con.prepareStatement(query2)) {

stmt1.setString(1, firstName);
stmt1.setString(2, lastName);
stmt1.setString(3, address);
stmt1.setString(4, city);
stmt1.setString(5, state);
stmt1.setString(6, zip);
stmt1.setString(7, phoneNumber);
stmt1.setString(8, email);
stmt1.setString(9, "Friend");

stmt2.setString(1, firstName);
stmt2.setString(2, lastName);
stmt2.setString(3, address);
stmt2.setString(4, city);
stmt2.setString(5, state);
stmt2.setString(6, zip);
stmt2.setString(7, phoneNumber);
stmt2.setString(8, email);
stmt2.setString(9, "Family");

stmt1.executeUpdate();
stmt2.executeUpdate();

System.out.println("Contact added to both 'Friend' and 'Family' types.");
} catch (SQLException | ClassNotFoundException e) {
e.printStackTrace();
}
}

}
