package main.java.com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
	public static void main(String[] args) {
		System.out.println("Welcome To AddressBook");
	    //create database
		 createDB();
		// create table
		createTable();
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
}
