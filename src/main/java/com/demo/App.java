package main.java.com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome To AddressBook");
        String q="Create Database address_book";
        try(Connection con =getConnection();Statement stmt =con.prepareStatement(q)){
        	
        	if(stmt.executeUpdate(q)==1)
        	System.out.println("Database created successfully");
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
    	Class.forName("com.mysql.cj.jdbc.Driver");
        String jdbcURL = "jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(jdbcURL, username, password);
    }
}
